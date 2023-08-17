import time
import math
import requests
import pandas as pd
from matplotlib import pyplot as plt

startTime = time.time()
lastRequest = 0.0

def handleTwelveDataRequests(requestString):
    global lastRequest
    while time.time() - lastRequest < (10.0):
        wasteTime = True
        # wait
    #print(time.time())
    response = requests.get(requestString)
    lastRequest = time.time()
    return response

def buildPatternResultArray(df):
    print()

def extractSingleDayPatternResult(allPatternResultsArray):
    print()

def distanceBetweenPoints(point1, point2):
    total = 0
    for dim in range(len(point1)):
        dif = point1[dim] - point2[dim]
        total += dif ** 2
    return math.sqrt(total)    

def comparePatterns(pattern1, pattern2):
    if not len(pattern1) == len(pattern2):
        raise Exception("ERROR: Trying to compare patterns of differring size!")
    
    total = 0.0
    for i in range(len(pattern1)):
        total += distanceBetweenPoints(pattern1[i], pattern2[i])
    
    return total / len(pattern1)

def run(stocks):
    for stock in stocks:
        response = handleTwelveDataRequests("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&format=JSON&symbol=" + stock + "&previous_close=true&outputsize=1000")
        json = response.json()
        #print(json)
        originalDF = pd.DataFrame(json['values'])
        # calculate price change vector values
        originalDF["pcVector"] = 100.0 * ((originalDF['close'].astype(float) - originalDF['previous_close'].astype(float)) / originalDF["previous_close"].astype(float))
        # calculate volume change vector values
        originalDF["previous_volume"] = originalDF['volume'].shift(-1)
        originalDF["volumecVector"] = 100.0 * ((originalDF['volume'].astype(float) - originalDF['previous_volume'].astype(float)) / originalDF["previous_volume"].astype(float))
        # calculate volatility change vector values
        originalDF["volatility"] = originalDF['high'].astype(float) - originalDF['low'].astype(float)
        originalDF["previous_volatility"] = originalDF['volatility'].shift(-1)
        originalDF["volatilitycVector"] = 100.0 * ((originalDF['volatility'].astype(float) - originalDF['previous_volatility'].astype(float)) / originalDF["previous_volatility"].astype(float))
        # drop the last row, since some of its values are empty
        originalDF.drop(index=originalDF.index[-1],axis=0,inplace=True)
        #print(originalDF)


        allPatterns = []
        allResults = []
        for index in originalDF.index:
            result = originalDF['pcVector'][index]
            for patternLength in range(2, 15):
                if (len(originalDF.index)-1 < index + patternLength):
                    break
                pattern = []
                for patternIterator in range(1, patternLength+1):
                    point = []
                    #point.append(originalDF['pcVector'][index+patternIterator])
                    #point.append(originalDF['volumecVector'][index+patternIterator])
                    point.append(originalDF['volatilitycVector'][index+patternIterator])
                    pattern.append(point)
                allPatterns.append(pattern)
                allResults.append(result)
        
        # analyze the relationship between pattern average difference and result difference
        patternDifs = []
        resultDifs = []
        for index in range(len(allPatterns)):
            if not index == 0:
                print("Iteration " + str(index+1))
            for index2 in range(len(allPatterns)):
                if (index >= index2):
                    continue
                if (not len(allPatterns[index]) == len(allPatterns[index2])):
                    continue
                if comparePatterns(allPatterns[index], allPatterns[index2]) < 0.5:
                    patternDifs.append(comparePatterns(allPatterns[index], allPatterns[index2]))
                    resultDifs.append(allResults[index] - allResults[index2])
        plt.xlabel("Vector Pattern Average Difference")
        plt.ylabel("Result Difference")
        plt.scatter(patternDifs, resultDifs)
        plt.show()

            

stocksTest = ["AAPL"]

run(stocksTest)
endTime = time.time()
print("Run time: " + str(endTime-startTime))