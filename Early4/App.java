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
            for (Integer i = 0; i < 30; i++) {
                featureNames.add("open" + Integer.toString(i+1));
                featureNames.add("high" + Integer.toString(i+1));
                featureNames.add("low" + Integer.toString(i+1));
                featureNames.add("close" + Integer.toString(i+1));
                featureNames.add("volume" + Integer.toString(i+1));
            }

            // build dataset & label set
            for (int i = 0; i < 150; i++) {
                data.add(new ArrayList<Double>());
            }
            for (int i = 30; i < dataPointsAsBuckets.size(); i++) {
                JSONBucket currentDataPoint = dataPointsAsBuckets.get(i);
                // get label from current datapoint
                labelData.add(Double.parseDouble((String)currentDataPoint.getValue("close")));
                // get data from previous 30 days
                int dataIterator = 0;
                for (int j = i-30; j < i; j++) {
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
            Model m = modeler.model(ds, 0.000000000000000001, 50);


            // Make a prediction using the Model
            // first we have to build the data used for the prediction
            ArrayList<Double> predictionData = new ArrayList<Double>();
            for (int i = dataPointsAsBuckets.size()-30; i < dataPointsAsBuckets.size(); i++) {
                JSONBucket currentPreviousDataPoint = dataPointsAsBuckets.get(i);
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("open")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("high")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("low")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("close")));
                predictionData.add(Double.parseDouble((String)currentPreviousDataPoint.getValue("volume")));
            }
            System.out.println(predictionData);
            // then we can actually make the prediction
            System.out.println(m.predict(predictionData));
            
        } catch (IOException e) {
            System.out.println("Error...lol");
        }

    }
}
