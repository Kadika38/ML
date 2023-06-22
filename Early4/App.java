package Early4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class App {
    
    public static void main(String[] args) {
        // get data from api (manually for now)
        // use DatasetBuilder to create a Dataset from the data
        // use Modeler to create a Model using the Dataset
        // make a prediction using the Model

        //TESTING
        // Create a randomized Dataset object using the DataGenerator class
        /* DataGenerator dg = new DataGenerator(15);
        Dataset theDataset = dg.getDataset();
        Modeler modeler = new Modeler();
        modeler.model(theDataset, 0.000001, 1000); */
        // ^ the above commented out code runs well

        //TESTING2
        // take real json data and build a dataset with it
        // then create a model of it
        // then make a prediction with the model
        /* int numOfDays = 2;
        try {
            Path filename = Path.of("/home/kadika/Coding/ML/Early4/json.txt");
            String json = Files.readString(filename);
            JSONBucket bucket = new JSONBucket(json);
            ArrayList<JSONBucket> dataPointsAsBucketsReversed = (ArrayList<JSONBucket>) bucket.getValue("values");
            // rearrange datapoints to be in chronological order (they are currently in reverse chronological order)
            ArrayList<JSONBucket> dataPointsAsBuckets = new ArrayList<JSONBucket>();
            for (int i = dataPointsAsBucketsReversed.size()-1; i >= 0; i--) {
                dataPointsAsBuckets.add(dataPointsAsBucketsReversed.get(i));
            }
            ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> labelData = new ArrayList<Double>();
            ArrayList<String> featureNames = new ArrayList<String>();

            // name features
            for (Integer i = 0; i < numOfDays; i++) {
                featureNames.add("open" + Integer.toString(i+1));
                featureNames.add("high" + Integer.toString(i+1));
                featureNames.add("low" + Integer.toString(i+1));
                featureNames.add("close" + Integer.toString(i+1));
                featureNames.add("volume" + Integer.toString(i+1));
            }

            // build dataset & label set
            for (int i = 0; i < numOfDays*5; i++) {
                data.add(new ArrayList<Double>());
            }
            for (int i = numOfDays; i < dataPointsAsBuckets.size(); i++) {
                JSONBucket currentDataPoint = dataPointsAsBuckets.get(i);
                // get label from current datapoint
                labelData.add(Double.parseDouble((String)currentDataPoint.getValue("close")));
                // get data from previous days
                int dataIterator = 0;
                for (int j = i-numOfDays; j < i; j++) {
                    JSONBucket currentPreviousDataPoint = dataPointsAsBuckets.get(j);
                    data.get(0 + (dataIterator * 5)).add(Double.parseDouble((String)currentPreviousDataPoint.getValue("open")));
                    data.get(1 + (dataIterator * 5)).add(Double.parseDouble((String)currentPreviousDataPoint.getValue("high")));
                    data.get(2 + (dataIterator * 5)).add(Double.parseDouble((String)currentPreviousDataPoint.getValue("low")));
                    data.get(3 + (dataIterator * 5)).add(Double.parseDouble((String)currentPreviousDataPoint.getValue("close")));
                    data.get(4 + (dataIterator * 5)).add(Double.parseDouble((String)currentPreviousDataPoint.getValue("volume")));
                    dataIterator++;
                }

            }
            Dataset ds = new Dataset(data, labelData, featureNames);

            // Model the dataset
            Modeler modeler = new Modeler();
            Model m = modeler.model(ds, 0.00000000000000001, 100);


            // Make a prediction using the Model
            // first we have to build the data used for the prediction
            ArrayList<Double> predictionData = new ArrayList<Double>();
            for (int i = dataPointsAsBuckets.size()-numOfDays; i < dataPointsAsBuckets.size(); i++) {
                JSONBucket currentPreviousDataPoint = dataPointsAsBuckets.get(i);
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("open")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("high")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("low")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("close")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("volume")));
            }

            ArrayList<ArrayList<Double>> pvr = modeler.predictionVersusReality(ds, m);
            for (int i = 0; i < pvr.size(); i++) {
                System.out.println(pvr.get(i));
            }
            ArrayList<Double> w = m.getWeights();
            Double h = w.get(0);
            Double l = w.get(0);
            for (int i = 1; i < w.size(); i++) {
                if (h < w.get(i)) {
                    h = w.get(i);
                }
                if (l > w.get(i)) {
                    l = w.get(i);
                }
            }
            System.out.println(w);
            System.out.println(ds.getFeatureNames());
            System.out.println(m.getBias());
            System.out.println(w.size());
            System.out.println("H: " + h);
            System.out.println("L: " + l);

            // then we can actually make the prediction
            System.out.println(m.predict(predictionData));
            
        } catch (IOException e) {
            System.out.println("Error...lol");
        } */


        //TESTING3
        // this time using the data to build other data, such as percent daily change, bollinger band relationships, and RSI relationships
        // main idea here is to use mostly yes/no data (0 or 1) and small number, as opposed to the earlier test which used raw data that included some large numbers
        // this will allow for a different label to be used - instead of trying to predict the price at close the next day, the label will simply be whether the daily change was an increase (yes or no, 0 or 1)
        // hopefully this approaches simplifies things as well as allowing for a more reasonable relationship to be explored between the data and the label
        try {
            Path filename = Path.of("/home/kadika/Coding/ML/Early4/json.txt");
            String json = Files.readString(filename);
            JSONBucket bucket = new JSONBucket(json);
            ArrayList<JSONBucket> dataPointsAsBucketsReversed = (ArrayList<JSONBucket>) bucket.getValue("values");
            // rearrange datapoints to be in chronological order (they are currently in reverse chronological order)
            ArrayList<JSONBucket> dataPointsAsBuckets = new ArrayList<JSONBucket>();
            for (int i = dataPointsAsBucketsReversed.size()-1; i >= 0; i--) {
                dataPointsAsBuckets.add(dataPointsAsBucketsReversed.get(i));
            }
            ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> labelData = new ArrayList<Double>();
            ArrayList<String> featureNames = new ArrayList<String>();

            // this time we will piece by piece build each feature's dataset using the raw data

            // earliestUsableDataNum is necessary since most of the features will require calculations involving one or more previous day's data
            // (can't use first data point since you cant make those calculations, etc.)
            int earliestUsableDataNum = 20;
            // Feature 1: Percent increase or decrease
            //  = ( Current day closing price / Previous day closing price ) - 1
            featureNames.add("PercentDailyPriceChange");
            ArrayList<Double> pdpc = new ArrayList<Double>();
            for (int i = earliestUsableDataNum; i < dataPointsAsBuckets.size(); i++) {
                Double currentDayClose = Double.parseDouble((String)dataPointsAsBuckets.get(i).getValue("close"));
                Double previousDayClose = Double.parseDouble((String)dataPointsAsBuckets.get(i-1).getValue("close"));
                pdpc.add((currentDayClose / previousDayClose) - 1);
            }
            data.add(pdpc);
            
            // Features 2 and 3: Bollinger Band Relationships
            // Feature 2: Went above upper bollinger band? yes == 1, no == 0
            // Feature 3: Went below lower bollinger band? yes == 1, no == 0
            int bollingerPeriod = 20;
            featureNames.add("WentAboveUpperBollingerBand");
            ArrayList<Double> aubb = new ArrayList<Double>();
            featureNames.add("WentBelowLowerBollingerBand");
            ArrayList<Double> blbb = new ArrayList<Double>();

            for (int i = earliestUsableDataNum; i < dataPointsAsBuckets.size(); i++) {
                // calculate simple moving average
                Double sma = 0.0;
                for (int j = bollingerPeriod; j > 0; j--) {
                    sma += Double.parseDouble((String)dataPointsAsBuckets.get(i-j).getValue("close"));
                }
                sma = sma / bollingerPeriod;
                // calulate standard deviation of last [bollingerPeriod] close prices
                // sum squared difference between sma and each closing price
                Double sumSquaredDifferenceCPandSMA = 0.0;
                for (int j = bollingerPeriod; j > 0; j--) {
                    sumSquaredDifferenceCPandSMA += Math.pow(Double.parseDouble((String)dataPointsAsBuckets.get(i-j).getValue("close")) - sma, 2);
                }
                // divide by [bollingerPeriod] and take the square root
                Double standardDeviation = sumSquaredDifferenceCPandSMA / bollingerPeriod;
                standardDeviation = Math.sqrt(standardDeviation);

                Double currentDaysClosingPrice = Double.parseDouble((String)dataPointsAsBuckets.get(i).getValue("close"));
                Double upperBollinger = sma + (2 * standardDeviation);
                Double lowerBollinger = sma - (2 * standardDeviation);
                // calculate Feature2
                if (currentDaysClosingPrice > upperBollinger) {
                    aubb.add(1.0);
                } else {
                    aubb.add(0.0);
                }
                // calculate Feature3
                if (currentDaysClosingPrice < lowerBollinger) {
                    blbb.add(1.0);
                } else {
                    blbb.add(0.0);
                }
            }

            data.add(aubb);
            data.add(blbb);

            // Features 4 & 5: RSI Relationships

            // Feature 4: RSI above 70? yes == 1, no == 0
            featureNames.add("RSIAbove70");
            ArrayList<Double> rsia = new ArrayList<Double>();
            // Feature 5: RSI below 30? yes == 1, no == 0
            featureNames.add("RSIBelow30");
            ArrayList<Double> rsib = new ArrayList<Double>();

            // RSI = 100 - (100 / (1 + RS))
            // RS = AvgU / AvgD
            // AvgU = Average of upwards moves in period
            // AvgD = Average of downward moves in period

            // calculate RSI for each day
            int rsiPeriod = 14;
            for (int i = earliestUsableDataNum; i < dataPointsAsBuckets.size(); i++) {
                // calculate AvgU & AvgD
                Double sumUpMoves = 0.0;
                Double sumDownMoves = 0.0;
                for (int j = i; j > i - rsiPeriod; j--) {
                    Double currentDaysClosingPrice = Double.parseDouble((String)dataPointsAsBuckets.get(j).getValue("close"));
                    Double previousDaysClosingPrice = Double.parseDouble((String)dataPointsAsBuckets.get(j-1).getValue("close"));
                    Double cpDif = currentDaysClosingPrice - previousDaysClosingPrice;
                    if (cpDif > 0.0) {
                        sumUpMoves += cpDif;
                    } else if (cpDif < 0.0) {
                        sumDownMoves += Math.abs(cpDif);
                    }
                }
                Double RSI;
                if (sumDownMoves != 0.0) {
                    Double AvgU = sumUpMoves / rsiPeriod;
                    Double AvgD = sumDownMoves / rsiPeriod;
                    Double RS = AvgU / AvgD;
                    RSI = 100.0 - (100.0 / (1.0 + RS));
                } else {
                    RSI = 100.0;
                }

                if (RSI > 70.0) {
                    rsia.add(1.0);
                } else {
                    rsia.add(0.0);
                }
                if (RSI < 30.0) {
                    rsib.add(1.0);
                } else {
                    rsib.add(0.0);
                }
            }

            data.add(rsia);
            data.add(rsib);
            
            

        } catch (IOException e) {
            System.out.println("Error... lol");
        }

    }
}
