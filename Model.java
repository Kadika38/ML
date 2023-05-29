import java.util.ArrayList;

public class Model {
    ArrayList<Double> weights;
    Double bias;
    
    Model(int featureNum) {
        this.weights = new ArrayList<Double>();
        for (int i = 0; i < featureNum; i++) {
            this.weights.add(0.0);
        }
        this.bias = 0.0;
    }
}
