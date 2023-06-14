package Early4;

import java.util.ArrayList;

public class Dataset {
    private ArrayList<ArrayList<Double>> dataset;
    private ArrayList<Double> labels;
    private ArrayList<String> features;

    Dataset(ArrayList<ArrayList<Double>> dataset, ArrayList<Double> labels, ArrayList<String> features) {
        // later add validation here to counter any possible errors
        this.dataset = dataset;
        this.labels = labels;
        this.features = features;
    }
}
