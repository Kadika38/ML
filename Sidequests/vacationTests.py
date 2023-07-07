import math
import requests
from matplotlib import pyplot as plt
import pandas as pd

def makePrediction(df, patternSize):
    # calculate most similar vector pattern (most similar to the most recent pattern)
    lowestAverageVectorDif = 10000000.0
    lowestAverageVectorDifIndex = -1
    firstRow = True
    for index, row in df.iterrows():
        # skip the first row because that's what we are comparing all the other rows to
        if (firstRow):
            firstRow = False
            continue
        try:
            if (math.isnan(df['closeVector'][index+patternSize-1])):
                continue
            averageVectorDif = 0.0
            for i in range(patternSize):
                #print(df)
                averageVectorDif += abs(df['closeVector'][i] - df['closeVector'][index+i])
                #print("gets here")
            averageVectorDif = averageVectorDif / patternSize
            #print(lowestAverageVectorDif)
            if (lowestAverageVectorDif > averageVectorDif):
                lowestAverageVectorDif = averageVectorDif
                lowestAverageVectorDifIndex = index
        except:
            continue
    
    #print(lowestAverageVectorDifIndex)
    return df['closeVector'][lowestAverageVectorDifIndex-1]

def getAverageVectorDifference(df, patternSize):
    lowestAverageVectorDif = 10000000.0
    firstRow = True
    for index, row in df.iterrows():
        # skip the first row because that's what we are comparing all the other rows to
        if (firstRow):
            firstRow = False
            continue
        try:
            if (math.isnan(df['closeVector'][index+patternSize-1])):
                continue
            averageVectorDif = 0.0
            for i in range(patternSize):
                averageVectorDif += abs(df['closeVector'][i] - df['closeVector'][index+i])
            averageVectorDif = averageVectorDif / patternSize
            if (lowestAverageVectorDif > averageVectorDif):
                lowestAverageVectorDif = averageVectorDif
        except:
            continue
    
    return lowestAverageVectorDif

stocks = ["AAPL", "ADPT", "AMC", "AMD", "BA", "CRSP", "LUV", "SBUX", "SPCE", "TSLA"]
stocksTest = ["AAPL"]

for stock in stocks:
    response = requests.get("https://api.twelvedata.com/time_series?apikey=c3efaf8bc4d14828a7574cf215662e7f&interval=1day&format=JSON&symbol=" + stock + "&previous_close=true&outputsize=1000")
    json = response.json()
    originalDF = pd.DataFrame(json['values'])
    vectorDF = pd.DataFrame()
    vectorDF["closeVector"] = 100.0 * ((originalDF['close'].astype(float) - originalDF['close'].shift(-1).astype(float)) / originalDF['close'].shift(-1).astype(float))
    vectorDF = vectorDF.drop(vectorDF.index[-1])

    patternSize = 2

    prediction = makePrediction(vectorDF, patternSize)
    prediction = round(prediction, 2)

    predictionVectorDif = round(getAverageVectorDifference(vectorDF, patternSize), 4)

    predictionStatisticsDF = pd.DataFrame()
    predictionStatisticsDF['prediction'] = ''
    predictionStatisticsDF['real'] = ''
    predictionStatisticsDF['vectorDif'] = ''

    for i in range(1, len(vectorDF.index)-patternSize):
        print("Running..." + stock + "..." + str(i))
        slicedVectorDF = vectorDF.drop(vectorDF.index[:i])
        slicedVectorDF = slicedVectorDF.reset_index()
        
        predictioni = makePrediction(slicedVectorDF, patternSize)
        vectorDifi = getAverageVectorDifference(slicedVectorDF, patternSize)
        reali = vectorDF['closeVector'][i-1]
        predictionStatisticsDF.loc[len(predictionStatisticsDF.index)] = {'prediction':predictioni, 'real':reali, 'vectorDif':vectorDifi}
    
    predictionStatisticsDF['predictionDif'] = predictionStatisticsDF['real'] - predictionStatisticsDF['prediction']
    predictionDifMean = round(predictionStatisticsDF['predictionDif'].mean(), 2)
    predictionDifStd = round(predictionStatisticsDF['predictionDif'].std(), 2)
    adjustedPrediction = prediction + predictionDifMean

    file = open("Sidequests/epa-" + originalDF['datetime'][0] + ".txt", "a")
    file.write("\n")
    file.write(stock + ":\n")
    file.write("Prediction: " + str(prediction) + "%\n")
    file.write("Prediction Difference Mean: " + str(predictionDifMean) + "%\n")
    file.write("Prediction Diferrence Std: " + str(predictionDifStd) + "%\n")
    file.write("Adjusted Prediction: " + str(adjustedPrediction) + "%\n")
    file.write("Prediction 1Std Range: (" + str(round(adjustedPrediction - predictionDifStd, 2)) + "%, " + str(round(adjustedPrediction + predictionDifStd, 2)) + "%)\n")
    file.write("Vector Difference: " + str(predictionVectorDif) + "\n")
    file.write("Vector Difference Mean: " + str(round(predictionStatisticsDF['vectorDif'].mean(), 4)) + "\n")
    file.close()

    