import math
import requests
from matplotlib import pyplot as plt
import pandas as pd

stocks = ["AAPL", "ADPT", "AMC", "AMD", "BA", "CRSP", "LUV", "SBUX", "SPCE", "TSLA"]
stocksTest = ["AAPL"]

for stock in stocksTest:
    response = requests.get("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&format=JSON&symbol=" + stock + "&previous_close=true&outputsize=1000")
    json = response.json()
    originalDF = pd.DataFrame(json['values'])
    originalDF["pcVector"] = 100.0 * ((originalDF['close'].astype(float) - originalDF['previous_close'].astype(float)) / originalDF["previous_close"].astype(float))
    
    patternsArray = []
    resultsArray = []

    
    for index, row in originalDF.iterrows():
        print("Row " + str(index) + " of " + str(len(originalDF.index)))
        for patternSize in range(2, 15):
            for resultSize in range(1, 15):
                if (len(originalDF.index) > (index + patternSize + resultSize -1)):
                    pattern = []
                    result = []
                    for i in range(patternSize):
                        if (originalDF.iloc[index + i]['pcVector'] > 0.0):
                            pattern.append(True)
                        else:
                            pattern.append(False)
                    for i in range(resultSize):
                        if (originalDF.iloc[index + patternSize + i]['pcVector'] > 0.0):
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
    
    newDF.sort_values(['Percentage'])

    newDF.to_csv("Sidequests/postVacay/qt.txt")