import pandas as pd

stocks = ["aapl", "adpt", "amc", "amd", "ba", "crsp", "luv", "sbux", "spce", "tsla"]

file = open("Sidequests/predictionTests2.txt", "a")

for stock in stocks:
    df = pd.read_csv(filepath_or_buffer="Sidequests/rvps/" + stock + "rvp2.csv")
    df["predictionDif"] = df["real"] = df["prediction"]

    """ file.write("\n")
    file.write(stock + "\n")
    file.write("Prediction:\n")
    file.write("Prediction Difference Std: 1 = " + str(df["predictionDif"].std()) + "%, 2 = " + str(df["predictionDif"].std() * 2) + "%\n")
    file.write("Vector Difference:\n")
    file.write("Vector Difference Std: 1 = " + str(df["vectorDif"].std()) + ", 2 = " + str(df["vectorDif"].std() * 2) + "\n")

    file.write("\n") """

    print(stock + ": predictionDif mean:" + str(df["predictionDif"].mean()) + ", vectorDif mean: " + str(df["vectorDif"].mean()) + "\n")

file.close()