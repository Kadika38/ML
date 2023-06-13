package Early4;

import java.util.ArrayList;

public class Model {
    private ArrayList<Double> weights;
    private Double bias;

    Model(int numOfWeights) {
        this.weights = new ArrayList<Double>();
        for (int i = 0; i < numOfWeights; i++) {
            this.weights.add(0.0);
        }
        this.bias = 0.0;
    }

    

}