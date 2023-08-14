import time
import requests
import pandas as pd

startTime = time.time()
lastRequest = 0.0

def handleTwelveDataRequests(requestString):
    global lastRequest
    while time.time() - lastRequest < (7.6):
        wasteTime = True
        # wait
    #print(time.time())
    response = requests.get(requestString)
    lastRequest = time.time()
    return response

def createPatternResultDF(df, ps, rs):
    patternsArray = []
    resultsArray = []

    
    for index, row in df.iterrows():
        # print("Row " + str(index) + " of " + str(len(df.index)))
        for patternSize in range(ps, ps+1):
            for resultSize in range(rs, rs+1):
                if (len(df.index) > (index + patternSize + resultSize -1)):
                    pattern = []
                    result = []
                    for i in range(patternSize):
                        if (df.iloc[index + i]['pcVector'] > 0.0):
                            pattern.append(True)
                        else:
                            pattern.append(False)
                    for i in range(resultSize):
                        if (df.iloc[index + patternSize + i]['pcVector'] > 0.0):
                            result.append(True)
                        else:
                            result.append(False)
                    try:
                        patternIndex = patternsArray.index(pattern)
                        resultsArray[patternIndex].append(result)
                    except:
                        patternsArray.append(pattern)
                        resultsArray.append([result])
    
    newDF = pd.DataFrame()
    newDF['Pattern'] = []
    newDF['Result'] = []
    newDF['ResultNum'] = []
    newDF['Percentage'] = []
    
    for i in range(len(patternsArray)):
        pa = patternsArray[i]

        previousResults = []
        resultPercentages = []
        for res in resultsArray[i]:
            if (previousResults.count(res) == 0):
                previousResults.append(res)
                percentage = resultsArray[i].count(res) / len(resultsArray[i])
                resultPercentages.append(percentage)
        highest = 0
        for p in resultPercentages:
            if p > highest: highest = p
        
        r = previousResults[resultPercentages.index(highest)]
        rn = len(resultsArray[i])
        pe = highest * 100.0

        newDF.loc[len(newDF.index)] = [pa, r, rn, pe]

    return newDF.sort_values('Percentage')

def developStockSpread(stocks, money):
    prices = []
    shares = []
    portfolioPercentage = []
    total = 0

    lowest = 0.0
    lowestIndex = 0

    for stock in stocks:
        response = handleTwelveDataRequests("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&type=stock&outputsize=1&previous_close=true&symbol=" + stock + "&format=JSON")
        json = response.json()
        # print(json)
        close = float(json['values'][0]['close'])
        prices.append(close)
        shares.append(0)
        portfolioPercentage.append(0.0)
        total += close

    while total > money:
        print("Cannot purchase all stocks with current amount of cash! Running without most expensive stock.")
        highest = 0.0
        highestIndex = 0
        for index in range(len(stocks)):
            if prices[index] > highest:
                highest = prices[index]
                highestIndex = index
            if prices[index] < lowest:
                lowest = prices[index]
                lowestIndex = index
        stocks.pop(highestIndex)
        prices.pop(highestIndex)
        shares.pop(highestIndex)
        portfolioPercentage.pop(highestIndex)
        total = 0
        for price in prices:
            total += price

    totalInvested = 0

    while money >= lowest:
        # purchase 1 share of the stock which makes up the smallest portion of the portfolio (and is purchasable with money that is left)

        # first find which stock that is
        smallestPortion = 1.0
        smallestPortionStockIndex = 0
        nextPurchaseFound = False
        for index in range(len(stocks)):
            if portfolioPercentage[index] <= smallestPortion and prices[index] <= money:
                smallestPortionStockIndex = index
                smallestPortion = portfolioPercentage[index]
                nextPurchaseFound = True
        
        if not nextPurchaseFound:
            break;

        # 'purchase' 1 share of that stock
        money = money - prices[smallestPortionStockIndex]
        shares[smallestPortionStockIndex] = shares[smallestPortionStockIndex] + 1
        totalInvested += prices[smallestPortionStockIndex]

        # re evaluate portfolio percentages
        for index in range(len(stocks)):
            moneyInStock = prices[index] * shares[index]
            portfolioPercentage[index] = moneyInStock / totalInvested

    """ print(stocks)
    print(prices)
    print(shares)
    print(portfolioPercentage)
    print("Money left: " + str(money)) """

    purchaseString = "Todays purchases: "
    for index in range(len(stocks)):
        purchaseString += str(shares[index]) + " " + stocks[index]
        if not index == len(stocks)-1:
            purchaseString += ", "
        else:
            purchaseString += "."
    
    print(purchaseString)

def run(money, stocks, maxPatternSize, maxResultPatternSize, minPercentHistoricalConfidenceInPatternResultPair, minHistoricalOccurancesOfPatternResultPair):
    print("Running...")

    finalDF = pd.DataFrame()
    finalDF['Stock'] = []
    finalDF['Pattern'] = []
    finalDF['Result'] = []
    finalDF['ResultNum'] = []
    finalDF['Percentage'] = []

    for stock in stocks:
        response = handleTwelveDataRequests("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&format=JSON&symbol=" + stock + "&previous_close=true&outputsize=1000")
        json = response.json()
        originalDF = pd.DataFrame(json['values'])
        originalDF["pcVector"] = 100.0 * ((originalDF['close'].astype(float) - originalDF['previous_close'].astype(float)) / originalDF["previous_close"].astype(float))

        mostRecentPatterns = []
        for r in range(2, maxPatternSize+1):
            pattern = []
            for i in range(r):
                if (originalDF.iloc[i]['pcVector'] > 0.0):
                    pattern.insert(0, True)
                else:
                    pattern.insert(0, False)
            mostRecentPatterns.append(pattern)
        # print(mostRecentPatterns)

        allDataFrames = []

        for ps in range(2, maxPatternSize+1):
            for rs in range(1,maxResultPatternSize+1):
                # print("Pattern Size: " + str(ps) + ", Result Size: " + str(rs))
                allDataFrames.append(createPatternResultDF(originalDF, ps, rs))

        for df in allDataFrames:
            for index in range(len(df.index)):
                if (mostRecentPatterns.count(df.iloc[index]['Pattern']) > 0):
                    if (df.iloc[index]['ResultNum'] > minHistoricalOccurancesOfPatternResultPair and df.iloc[index]['Percentage'] > minPercentHistoricalConfidenceInPatternResultPair):
                        finalDF.loc[len(finalDF.index)] = [stock, df.iloc[index]['Pattern'], df.iloc[index]['Result'], df.iloc[index]['ResultNum'], df.iloc[index]['Percentage']]
    
    finalStocks = []
    for index in range(len(finalDF.index)):
        if finalStocks.count(finalDF.iloc[index]['Stock']) == 0:
            finalStocks.append(finalDF.iloc[index]['Stock'])

    developStockSpread(finalStocks, money)

basicStocks = ["AAPL", "ADPT", "AMC", "AMD", "BA", "CRSP", "LUV", "SBUX", "SPCE", "TSLA"]
stocksTest = ["AAPL"]
stocksTest2 = ["AI"]
moreStocks = ["CCL", "BNTX", "TSLA", "NFLX", "MSFT", "META", "DIS", "SBUX", "F", "GE", "BA", "QCOM", "TM", "INTC", "NKE", "AMZN", "CRSP",
              "LUV", "IBM", "LMT", "CAT", "NOC", "GM", "AAPL", "NVDA", "SONY", "EA", "DE", "AMD", "ATVI", "HON", "DAL", "AAL", "ALK", "JBLU",
              "UAL", "SGEN", "ADPT", "SPOT", "NTLA", "SNAP", "GOOG", "FCEL", "BYND", "SPCE", "MCD", "XOM", "STNG", "ORGO", "NKLA", "RTX",
              "AMC", "M", "REAL", "MRNA", "ACB", "MP", "PLUG", "AI", "PLL", "GME", "PFE"]

run(1000, moreStocks, 14, 1, 70.0, 9)
totalTimeRun = time.time() - startTime
print("Run time: " + str(totalTimeRun / 60.0))