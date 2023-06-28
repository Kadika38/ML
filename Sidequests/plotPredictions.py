import pandas as pd
from matplotlib import pyplot as plt

for i in range(13):
    thedata = pd.read_csv(filepath_or_buffer="Sidequests/rvpcsvs/rvp" + str(i+1) + ".csv")
    plt.xlabel("predictionDif")
    plt.ylabel("vectorDif")
    plt.scatter((thedata["real"]-thedata["prediction"]), thedata["vectorDif"])
    plt.figure()

plt.show()