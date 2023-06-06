import java.util.ArrayList;
import java.util.Random;

// This version of Early advances the concept a bit further in terms of automation.  Still just doing linear regression.
// Goal 1: Create randomized sets of data to skip manual data creation
// Goal 2: Build a system that can handle any number of features / weights

public class Early3 {
    Integer numOfFeatures;
    ArrayList<ArrayList<Double>> datasets;
    ArrayList<Double> actualFeatureWeights;
    Double actualBias;
    ArrayList<Double> labels;

    Random random;

    ArrayList<Double> modelWeights;
    Double modelBias;

    Double lr;
    Double weightMax;
    Integer dataCount;
    Double dataMax;
    
    Early3() {
        // Parameters
        this.lr = 0.00001;
        this.numOfFeatures = 15;
        this.weightMax = 50.0;
        this.dataCount = 500;
        this.dataMax = 200.0;
        // Data creation
        System.out.println("Generating random data ...");
        this.random = new Random();
        // random actual weight between 0 (inclusive) and this.weightMax (exclusive) attributed to each feature
        this.actualFeatureWeights = new ArrayList<Double>();
        for (int i = 0; i < this.numOfFeatures; i++) {
            this.actualFeatureWeights.add(this.random.nextDouble() * this.weightMax);
        }
        // random actual bias, same constraints as actual weights
        this.actualBias = this.random.nextDouble() * this.weightMax;
        System.out.println("Weights and bias generated...");
        // initialize datasets
        this.datasets = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < this.numOfFeatures; i++) {
            this.datasets.add(new ArrayList<Double>());
        }
        this.labels = new ArrayList<Double>();
        // populate feature data
        for (int i = 0; i < this.numOfFeatures; i++) {
            ArrayList<Double> current = this.datasets.get(i);
            // generate this.dataCount random Doubles between 0 (inclusive) and this.dataMax (exclusive) in each 
            for (int j = 0; j < this.dataCount; j++) {
                current.add(this.random.nextDouble() * this.dataMax);
            }
        }
        System.out.println("Feature datasets generated ...");
        // calculate labels
        for (int i = 0; i < this.dataMax; i++) {
            Double labelVal = this.actualBias;
            for (int j = 0; j < this.numOfFeatures; j++) {
                ArrayList<Double> thisFeatureDataset = this.datasets.get(j);
                labelVal += this.actualFeatureWeights.get(j) * thisFeatureDataset.get(i);
            }
            this.labels.add(labelVal);
        }
        System.out.println("Labels calculated...");
        System.out.println("Random data creation complete.");

        // Initialize model system
        this.modelWeights = new ArrayList<Double>();
        this.modelBias = 0.0;
        for (int i = 0; i < this.numOfFeatures; i++) {
            this.modelWeights.add(0.0);
        }
    }

    public Double model() {
        // compute loss for each feature and adjust model weight accordingly
        boolean ye = true;
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
                if (ye) {
                    ye = false;
                    //System.out.println(totalLoss + " <- " + coefficient + " * (" + label + " - " + holder + ")");
                }
            }
            Double lossAdj = totalLoss / this.dataMax;
            //System.out.println("New weight" + i + ": " + this.modelWeights.get(i) + " - (lr * " + lossAdj + ")");
            this.modelWeights.set(i, this.modelWeights.get(i) - (this.lr * lossAdj));
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

    public static void main(String[] args) {
        Early3 t = new Early3();
        System.out.print("0: ");
        Double averageLossPrevious = t.model();
        System.out.print("1: ");
        Double newAverageLoss = t.model();
        int n = 2;
        while (averageLossPrevious.intValue() != newAverageLoss.intValue()) {
            System.out.print(n + ": ");
            n++;
            averageLossPrevious = newAverageLoss;
            newAverageLoss = t.model();
        }
    }
}
