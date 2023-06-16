package Early4;

import java.util.ArrayList;

public class Model {
    private ArrayList<Double> weights;
    private Double bias;
    private Dataset dataset;

    Model(Dataset dataset) {
        this.weights = new ArrayList<Double>();
        for (int i = 0; i < dataset.getNumOfFeatures(); i++) {
            this.weights.add(0.0);
        }
        this.bias = 0.0;
        this.dataset = dataset;
    }

    public ArrayList<Double> getWeights() {
        return this.weights;
    }

    public Double getBias() {
        return this.bias;
    }

    // Performs one iteration of linear regression on the dataset, adjusting the model accordingly
    // Returns the squared average loss of the new model when compared to the data
    public Double linearRegression(Double lr) {
        // Compute loss for each feature and adjust model weight accordingly
            for (int i = 0; i < this.weights.size(); i++) {
                ArrayList<Double> current = this.dataset.getSingleFeatureDataset(i);
                Double totalLoss = 0.0;
                for (int j = 0; j < this.dataset.getNumOfDatapoints(); j++) {
                    Double holder = 0.0;
                    Double coefficient = (-2.0) * current.get(j);
                    Double label = this.dataset.getLabels().get(j);
                    for (int f = 0; f < this.weights.size(); f++) {
                        ArrayList<Double> weightAdjCurrent = this.dataset.getSingleFeatureDataset(f);
                        holder += (this.weights.get(f) * weightAdjCurrent.get(j));
                    }
                    holder += this.bias;
                    totalLoss += coefficient * (label - holder);
                }
                Double lossAdj = totalLoss / this.dataset.getNumOfDatapoints();
                // Adjust model weight
                this.weights.set(i, this.weights.get(i) - (lr * lossAdj));
            }

            // perform same process as above, but for model bias
            Double totalLossB = 0.0;
            for (int i = 0; i < this.dataset.getNumOfDatapoints(); i++) {
                Double holder = 0.0;
                Double coefficient = (-2.0);
                Double label = this.dataset.getLabels().get(i);
                for (int j = 0; j < this.weights.size(); j++) {
                    ArrayList<Double> current = this.dataset.getSingleFeatureDataset(j);
                    holder += (this.weights.get(j) * current.get(i));
                }
                holder += this.bias;
                totalLossB += coefficient * (label - holder);
            }
            Double lossAdjB = totalLossB / this.dataset.getNumOfDatapoints();
            this.bias = (this.bias - (lr * lossAdjB));

            System.out.print("Weights n Bias: ");
            for (int i = 0; i < this.weights.size(); i++) {
                System.out.print("w" + i + ":" + this.weights.get(i) + " ");
            }
            System.out.println("b:" + this.bias);

            // compute average loss and return it
            Double averageLoss = 0.0;
            for (int i = 0; i < this.dataset.getNumOfDatapoints(); i++) {
                Double predictedValue = this.bias;
                for (int j = 0; j < this.weights.size(); j++) {
                    ArrayList<Double> current = dataset.getSingleFeatureDataset(j);
                    predictedValue += (this.weights.get(j) * current.get(i));
                }
                averageLoss += this.dataset.getLabels().get(i) - predictedValue;
            }
            averageLoss = averageLoss / this.dataset.getNumOfDatapoints();
            System.out.println("Average Loss: " + averageLoss);
            return Math.pow(averageLoss, 2);
    }
}