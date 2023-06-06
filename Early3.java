import java.util.ArrayList;
import java.util.Random;

// This version of Early advances the concept a bit further in terms of automation.  Still just doing linear regression.
// Goal 1: Create randomized sets of data to skip manual data creation
// Goal 2: Build a system that can handle any number of features / weights
// Bonus: Create noise in the randomly generated data

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
    
    Early3() {
        // Data creation
        System.out.println("Generating random data ...");
        this.random = new Random();
        // random num of features from 1 - 15
        this.numOfFeatures = this.random.nextInt(15) + 1;
        System.out.println(this.numOfFeatures + " features generated...");
        // random actual weight between 0 (inclusive) and 50 (exclusive) attributed to each feature
        this.actualFeatureWeights = new ArrayList<Double>();
        for (int i = 0; i < this.numOfFeatures; i++) {
            this.actualFeatureWeights.add(this.random.nextDouble() * 50);
        }
        // random actual bias, same constraints as actual weights
        this.actualBias = this.random.nextDouble() * 50;
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
            // generate 500 random Doubles between 0 (inclusive) and 200 (exclusive) in each 
            for (int j = 0; j < 500; j++) {
                current.add(this.random.nextDouble() * 200);
            }
        }
        System.out.println("Feature datasets generated ...");
        // calculate labels
        for (int i = 0; i < 500; i++) {
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
        this.lr = 0.01;
    }

    public void model() {
        // compute loss for each feature and adjust model weight accordingly
        for (int i = 0; i < this.numOfFeatures; i++) {
            ArrayList<Double> current = this.datasets.get(i);
            Double totalLoss = 0.0;
            for (int j = 0; j < 500; j++) {
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
            Double lossAdj = totalLoss / 500;
            this.modelWeights.set(i, this.modelWeights.get(i) - (this.lr * lossAdj));
        }
        // perform same process as above, but for model bias
        Double totalLossB = 0.0;
        for (int i = 0; i < 500; i++) {
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
        Double lossAdjB = totalLossB / 500;
        this.modelBias = this.modelBias - (this.lr * lossAdjB);
    }

    public static void main(String[] args) {
        Early3 t = new Early3();
        //t.model();
    }
}
