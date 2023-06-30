import pandas as pd

file = open("Sidequests/predictionTests.txt", "a")

aapl = pd.read_csv(filepath_or_buffer="Sidequests/rvps/aaplrvp2.csv")
aapl["predictionDif"] = aapl["real"] - aapl["prediction"]

file.write("\naapl: " + str(aapl["predictionDif"].std()))

adpt = pd.read_csv(filepath_or_buffer="Sidequests/rvps/adptrvp2.csv")
adpt["predictionDif"] = adpt["real"] - adpt["prediction"]

file.write("\nadpt: " + str(adpt["predictionDif"].std()))

amc = pd.read_csv(filepath_or_buffer="Sidequests/rvps/amcrvp2.csv")
amc["predictionDif"] = amc["real"] - amc["prediction"]

file.write("\namc: " + str(amc["predictionDif"].std()))

amd = pd.read_csv(filepath_or_buffer="Sidequests/rvps/amdrvp2.csv")
amd["predictionDif"] = amd["real"] - amd["prediction"]

file.write("\namd: " + str(amd["predictionDif"].std()))

ba = pd.read_csv(filepath_or_buffer="Sidequests/rvps/barvp2.csv")
ba["predictionDif"] = ba["real"] - ba["prediction"]

file.write("\nba: " + str(ba["predictionDif"].std()))

crsp = pd.read_csv(filepath_or_buffer="Sidequests/rvps/crsprvp2.csv")
crsp["predictionDif"] = crsp["real"] - crsp["prediction"]

file.write("\ncrsp: " + str(crsp["predictionDif"].std()))

luv = pd.read_csv(filepath_or_buffer="Sidequests/rvps/luvrvp2.csv")
luv["predictionDif"] = luv["real"] - luv["prediction"]

file.write("\nluv: " + str(luv["predictionDif"].std()))

sbux = pd.read_csv(filepath_or_buffer="Sidequests/rvps/sbuxrvp2.csv")
sbux["predictionDif"] = sbux["real"] - sbux["prediction"]

file.write("\nsbux: " + str(sbux["predictionDif"].std()))

spce = pd.read_csv(filepath_or_buffer="Sidequests/rvps/spcervp2.csv")
spce["predictionDif"] = spce["real"] - spce["prediction"]

file.write("\nspce: " + str(spce["predictionDif"].std()))

tsla = pd.read_csv(filepath_or_buffer="Sidequests/rvps/tslarvp2.csv")
tsla["predictionDif"] = tsla["real"] - tsla["prediction"]

file.write("\ntsla: " + str(tsla["predictionDif"].std()))

file.close()