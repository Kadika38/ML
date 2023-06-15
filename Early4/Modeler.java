package Early4;

import java.util.ArrayList;

public class Modeler {

    Modeler() {

    }

    public Model model(Dataset dataset) {
        // initialize and set weights and bias to 0
        int numOfFeatures = dataset.getNumOfFeatures();
        Model model = new Model(numOfFeatures);
        for (int i = 0; i < numOfFeatures; i++) {
            model.setWeight(i, 0.0);
        }
        model.setBias(0.0);

        Double lr = 0.001;
        Double lrReductionRate = 0.1;
        Double averageLossPrevious = 0.0;
        Double averageLossNew = 10000.0;

        while (averageLossPrevious.intValue() != averageLossNew.intValue()) {
            // if average loss is flip flopping between being positive and negative or getting larger, reduce learning rate
            if ((averageLossNew > 0.0 && averageLossPrevious < 0.0) || (averageLossNew < 0.0 && averageLossPrevious > 0.0) || (averageLossNew > averageLossPrevious)) {
                lr = lr * lrReductionRate;
                System.out.println("Learning rate reduced");
            }

            averageLossPrevious = averageLossNew;

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
                        holder += (model.getWeights().get(f) * weightAdjCurrent.get(j));
                    }
                    holder += model.getBias();
                    totalLoss += coefficient * (label - holder);
                }
                Double lossAdj = totalLoss / dataset.getNumOfDatapoints();
                // Adjust model weight
                model.setWeight(i, model.getWeights().get(i) - (lr * lossAdj));
            }

            // perform same process as above, but for model bias
            Double totalLossB = 0.0;
            for (int i = 0; i < dataset.getNumOfDatapoints(); i++) {
                Double holder = 0.0;
                Double coefficient = (-2.0);
                Double label = dataset.getLabels().get(i);
                for (int j = 0; j < numOfFeatures; j++) {
                    ArrayList<Double> current = dataset.getSingleFeatureDataset(j);
                    holder += (model.getWeights().get(j) * current.get(i));
                }
                holder += model.getBias();
                totalLossB += coefficient * (label - holder);
            }
            Double lossAdjB = totalLossB / dataset.getNumOfDatapoints();
            model.setBias(model.getBias() - (lr * lossAdjB));

            // compute new average loss
            Double averageLoss = 0.0;
            for (int i = 0; i < dataset.getNumOfDatapoints(); i++) {
                Double predictedValue = model.getBias();
                for (int l = 0; j < numOfFeatures; j++) {
                    ArrayList<Double> current = dataset.getSingleFeatureDataset(j);
                    predictedValue += (model.getWeights().get(j) * current.get(i));
                }
                averageLoss += dataset.getLabels().get(i) - predictedValue;
            }
            averageLoss = averageLoss / dataset.getNumOfDatapoints();

            averageLossNew = averageLoss;
            System.out.println("Average Loss: " + averageLoss);
        }
        
        return model;
    }
    
}
