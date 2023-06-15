package Early4;

import java.util.ArrayList;

public class Modeler {
    Model model;

    Modeler() {

    }

    public Model model(Dataset dataset) {
        // initialize and set weights and bias to 0
        int numOfFeatures = dataset.getNumOfFeatures();
        this.model = new Model(numOfFeatures);
        for (int i = 0; i < numOfFeatures; i++) {
            this.model.setWeight(i, 0.0);
        }
        this.model.setBias(0.0);

        Double lr = 0.001;

        // Compute loss for each feature and adjust model weight accordingly
        for (int i = 0; i < numOfFeatures; i++) {
            ArrayList<Double> current = dataset.getSingleFeatureDataset(i);
            Double totalLoss = 0.0;
            for (int j = 0; j < dataset.getNumOfDatapoints(); j++) {
                Double holder = 0.0;
                Double coefficient = (-2.0) * current.get(j);
                Double label = dataset.getLabels().get(j);
                for (int f = 0; f < numOfFeatures; f++) {
                    ArrayList<Double> weightAdjCurrent = dataset.getSingleFeatureDataset(f);
                    holder += (this.model.getWeights().get(f) * weightAdjCurrent.get(j));
                }
                holder += this.model.getBias();
                totalLoss += coefficient * (label - holder);
            }
            Double lossAdj = totalLoss / dataset.getNumOfDatapoints();
            // Adjust model weight
            this.model.setWeight(i, this.model.getWeights().get(i) - (lr * lossAdj));
        }

        // perform same process as above, but for model bias
        Double totalLossB = 0.0;
        for (int i = 0; i < dataset.getNumOfDatapoints(); i++) {
            Double holder = 0.0;
            Double coefficient = (-2.0);
            Double label = dataset.getLabels().get(i);
            for (int j = 0; j < numOfFeatures; j++) {
                ArrayList<Double> current = dataset.getSingleFeatureDataset(j);
                holder += (this.model.getWeights().get(j) * current.get(i));
            }
            holder += this.model.getBias();
            totalLossB += coefficient * (label - holder);
        }
        Double lossAdjB = totalLossB / dataset.getNumOfDatapoints();
        this.model.setBias(this.model.getBias() - (lr * lossAdjB));
    }
    
}
