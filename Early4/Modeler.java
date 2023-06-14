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

        // compute loss for each feature and adjust model weight accordingly
        for (int i = 0; i < this.numOfFeatures; i++) {
            ArrayList<Double> current = this.datasets.get(i);
            Double totalLoss = 0.0;
            for (int j = 0; j < this.dataMax; j++) {
                Double holder = 0.0;
                Double coefficient = (-2.0) * current.get(j);
                Double label = this.labels.get(j);
                for (int f = 0; f < this.numOfFeatures; f++) {
                    ArrayList<Double> weightAdjCurrent = this.datasets.get(f);
                    holder += (this.modelWeights.get(f) * weightAdjCurrent.get(j));
                }
                holder += this.modelBias;
                totalLoss += coefficient * (label - holder);
            }
            Double lossAdj = totalLoss / this.dataMax;
            //System.out.println("New weight" + i + ": " + this.modelWeights.get(i) + " - (lr * " + lossAdj + ")");
            this.modelWeights.set(i, this.modelWeights.get(i) - (this.lr * lossAdj));
        }

        // Compute loss for each feature and adjust model weght accordingly
        for (int i = 0; i < numOfFeatures; i++) {
            ArrayList<Double> current = dataset.getSingleFeatureDataset(i);
            Double totalLoss = 0.0;
            // CONTINUE REWORKING THE COPY PASTED MODELING CODE FROM HERE
            // SIMPLY ADJUSTING THE CODE FROM EARLY3 TO WORK HERE
        }




        // perform same process as above, but for model bias
        Double totalLossB = 0.0;
        for (int i = 0; i < this.dataMax; i++) {
            Double holder = 0.0;
            Double coefficient = (-2.0);
            Double label = this.labels.get(i);
            for (int j = 0; j < this.numOfFeatures; j++) {
                ArrayList<Double> current = this.datasets.get(j);
                holder += (this.modelWeights.get(j) * current.get(i));
            }
            holder += this.modelBias;
            totalLossB += coefficient * (label - holder);
        }
        Double lossAdjB = totalLossB / this.dataMax;
        this.modelBias = this.modelBias - (this.lr * lossAdjB);

        // compute new average loss
        Double averageLoss = 0.0;
        for (int i = 0; i < this.dataMax; i++) {
            Double predictedValue = this.modelBias;
            for (int j = 0; j < this.numOfFeatures; j++) {
                ArrayList<Double> current = this.datasets.get(j);
                predictedValue += (this.modelWeights.get(j) * current.get(i));
            }
            averageLoss += this.labels.get(i) - predictedValue;
            //System.out.println("l - p: " + this.labels.get(i) + " - " + predictedValue);
        }
        averageLoss = averageLoss / this.dataMax;
        System.out.println("New Avereage Loss: " + averageLoss);
        return averageLoss;
    }
    
}
