package Early4;

import java.util.ArrayList;

public class Dataset {
    private ArrayList<ArrayList<Double>> datasets;
    private ArrayList<Double> labels;
    private ArrayList<String> featureNames;

    Dataset(ArrayList<ArrayList<Double>> datasets, ArrayList<Double> labels, ArrayList<String> featureNames) {
        // later add validation here to counter any possible errors
        this.datasets = datasets;
        this.labels = labels;
        this.featureNames = featureNames;
    }

    public int getNumOfFeatures() {
        return this.featureNames.size();
    }

    public ArrayList<Double> getSingleFeatureDataset(Integer index) {
        return this.datasets.get(index);
    }

    public ArrayList<Double> getLabels() {
        return this.labels;
    }

    public Integer getNumOfDatapoints() {
        return this.datasets.get(0).size();
    }

    public ArrayList<String> getFeatureNames() {
        return this.featureNames;
    }
}
