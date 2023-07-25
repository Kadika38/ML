import math
import requests
from matplotlib import pyplot as plt
import pandas as pd

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

stocks = ["AAPL", "ADPT", "AMC", "AMD", "BA", "CRSP", "LUV", "SBUX", "SPCE", "TSLA"]
stocksTest = ["AAPL"]

finalDF = pd.DataFrame()
finalDF['Stock'] = []
finalDF['Pattern'] = []
finalDF['Result'] = []
finalDF['ResultNum'] = []
finalDF['Percentage'] = []

for stock in stocks:
    response = requests.get("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&format=JSON&symbol=" + stock + "&previous_close=true&outputsize=1000")
    json = response.json()
    originalDF = pd.DataFrame(json['values'])
    originalDF["pcVector"] = 100.0 * ((originalDF['close'].astype(float) - originalDF['previous_close'].astype(float)) / originalDF["previous_close"].astype(float))

    mostRecentPatterns = []
    for r in range(2, 15):
        pattern = []
        for i in range(r):
            if (originalDF.iloc[i]['pcVector'] > 0.0):
                pattern.insert(0, True)
            else:
                pattern.insert(0, False)
        mostRecentPatterns.append(pattern)
    print(mostRecentPatterns)

    allDataFrames = []

    for ps in range(2, 15):
        for rs in range(1,3):
            print("Pattern Size: " + str(ps) + ", Result Size: " + str(rs))
            allDataFrames.append(createPatternResultDF(originalDF, ps, rs))

    for df in allDataFrames:
        for index in range(len(df.index)):
            if (mostRecentPatterns.count(df.iloc[index]['Pattern']) > 0):
                if (df.iloc[index]['ResultNum'] > 9 and df.iloc[index]['Percentage'] > 60.0):
                    finalDF.loc[len(finalDF.index)] = [stock, df.iloc[index]['Pattern'], df.iloc[index]['Result'], df.iloc[index]['ResultNum'], df.iloc[index]['Percentage']]
    

finalDF.to_csv('Sidequests/postVacay/qt.csv')