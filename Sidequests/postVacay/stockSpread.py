import requests

# This program simply finds the most even allocation for purchasing stocks with a given amount of money.

money = 1000.0
stocks = ["SBUX", "LMT", "AAPL", "NVDA", "AI", "GME"]
prices = []
shares = []
portfolioPercentage = []
total = 0

lowest = 0.0
lowestIndex = 0

for stock in stocks:
    response = requests.get("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&type=stock&outputsize=1&previous_close=true&symbol=" + stock + "&format=JSON")
    json = response.json()
    close = float(json['values'][0]['close'])
    prices.append(close)
    shares.append(0)
    portfolioPercentage.append(0.0)
    total += close

while total > money:
    print("Cannot purchase all stocks with current amount of cash! Removing most expensive stock.")
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
        if portfolioPercentage[index] < smallestPortion and prices[index] <= money:
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

print(stocks)
print(prices)
print(shares)
print(portfolioPercentage)
print("Money left: " + str(money))