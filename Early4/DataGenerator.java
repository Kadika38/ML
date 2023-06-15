package Early4;

import java.util.ArrayList;
import java.util.Random;

// Used to generate randomized data for testing purposes

public class DataGenerator {
    public Integer numOfFeatures;
    public Double weightMax;
    public Integer dataCount;
    public Double dataMax;
    private Random random;
    public ArrayList<Double> actualFeatureWeights;
    public Double actualBias;
    public ArrayList<ArrayList<Double>> datasets;
    public ArrayList<Double> labels;
    public ArrayList<String> featureNames;
    public Dataset d;

    
    DataGenerator(Integer numOfFeatures) {
        // Parameters
        this.numOfFeatures = numOfFeatures;
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

        // initialize datasets and labels
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
        for (int i = 0; i < this.dataCount; i++) {
            Double labelVal = this.actualBias;
            for (int j = 0; j < this.numOfFeatures; j++) {
                ArrayList<Double> thisFeatureDataset = this.datasets.get(j);
                labelVal += this.actualFeatureWeights.get(j) * thisFeatureDataset.get(i);
            }
            this.labels.add(labelVal);
        }
        System.out.println("Labels calculated...");

        // Build Dataset object

        // 'Name' the features (not necessary for testing, but necessary for the Dataset object constructor)
        this.featureNames = new ArrayList<String>();
        for (Integer i = 1; i < this.numOfFeatures + 1; i++) {
            String name = "Feature";
            name += i.toString();
            this.featureNames.add(name);
        }

        this.d = new Dataset(this.datasets, this.labels, this.featureNames);

        System.out.println("Random data creation complete.");
    }

    public Dataset getDataset() {
        return this.d;
    }

    public String getActualFunction() {
        String f = "Label =";
        for (int i = 0; i < this.numOfFeatures; i++) {
            f += " (";
            f += this.actualFeatureWeights.get(i).toString();
            f += "*";
            f += this.featureNames.get(i);
            f += ") +";
        }
        f += " ";
        f += this.actualBias.toString();

        return f;
    }

    public Double computeTestLabel(ArrayList<Double> testData) {
        if (this.numOfFeatures != testData.size()) {
            throw new Error("Invalid input - number of datapoints does not match number of features");
        }

        Double l = this.actualBias;
        for (int i = 0; i < this.numOfFeatures; i++) {
            l += (this.actualFeatureWeights.get(i) * testData.get(i));
        }

        return l;
    }
}
