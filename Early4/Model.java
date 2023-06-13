package Early4;

import java.lang.reflect.Array;
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

    public ArrayList<Double> getWeights() {
        return this.weights;
    }

    public Double getBias() {
        return this.bias;
    }

    public void setWeight(int index, Double value) {
        this.weights.set(index, value);
    }

    public void setBias(Double value) {
        this.bias = value;
    }

}