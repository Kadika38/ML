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
            ArrayList<JSONBucket> dataPointsAsBuckets = (ArrayList<JSONBucket>) bucket.getValue("values");
            ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
            ArrayList<Double> labelData = new ArrayList<Double>();
            ArrayList<String> featureNames = new ArrayList<String>();
            boolean buildFeatureNameArray = true;
            for (int i = 0; i < dataPointsAsBuckets.size()-30; i++) {
                labelData.add(Double.parseDouble((String)dataPointsAsBuckets.get(i).getValue("close")));
                ArrayList<Double> datapoint = new ArrayList<Double>();
                for (int j = 1; j < 31; j++) {
                    JSONBucket current = dataPointsAsBuckets.get(i+j);
                    datapoint.add(Double.parseDouble((String)current.getValue("open")));
                    datapoint.add(Double.parseDouble((String)current.getValue("high")));
                    datapoint.add(Double.parseDouble((String)current.getValue("low")));
                    datapoint.add(Double.parseDouble((String)current.getValue("close")));
                    datapoint.add(Double.parseDouble((String)current.getValue("volume")));

                    if (buildFeatureNameArray) {
                        String num = Integer.toString(j);
                        featureNames.add("open" + num);
                        featureNames.add("high" + num);
                        featureNames.add("low" + num);
                        featureNames.add("close" + num);
                        featureNames.add("volume" + num);
                    }
                }
                data.add(datapoint);

                buildFeatureNameArray = false;
            }
            // Finally build the Dataset
            Dataset ds = new Dataset(data, labelData, featureNames);

            // Model the dataset
            Modeler modeler = new Modeler();
            Model m = modeler.model(ds, 0.000000000000000001, 100);

        } catch (IOException e) {
            System.out.println("Error...lol");
        }

    }
}
