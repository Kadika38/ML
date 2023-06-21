package Early4;

import java.util.ArrayList;

public class Modeler {

    Modeler() {

    }

    public Model model(Dataset dataset, Double lr, int numOfEpochs) {
        // initialize the model
        Model model = new Model(dataset);

        Double averageLossPrevious = model.linearRegression(lr);
        Double averageLossNew = model.linearRegression(lr);
        int epochs = 0;

        while (averageLossNew != averageLossPrevious && epochs < numOfEpochs) {
            averageLossPrevious = averageLossNew;
            averageLossNew = model.linearRegression(lr);
            epochs++;
        }
        

        return model;
    }

    public ArrayList<ArrayList<Double>> predictionVersusReality(Dataset d, Model m) {
        ArrayList<ArrayList<Double>> pvr = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < d.getNumOfDatapoints(); i++) {
            pvr.add(new ArrayList<Double>());
        }
        for (int i = 0; i < d.getNumOfDatapoints(); i++) {
            ArrayList<Double> datapoint = new ArrayList<Double>();
            for (int j = 0; j < d.getNumOfFeatures(); j++) {
                datapoint.add(d.getSingleFeatureDataset(j).get(i));
            }
            pvr.get(i).add(d.getLabels().get(i));
            pvr.get(i).add(m.predict(datapoint));
        }

        return pvr;
    }
    
}
