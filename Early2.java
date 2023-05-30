import java.util.ArrayList;


// This version of Early is my first attempt at linear regression with multiple features / weights
public class Early2 {
    ArrayList<Double> x1data;
    ArrayList<Double> x2data;
    ArrayList<Double> x3data;
    ArrayList<Double> ydata;

    Double w1;
    Double w2;
    Double w3;
    Double b;

    Double lr = 0.01;
    

    Early2() {
        this.x1data = new ArrayList<Double>();
        this.x2data = new ArrayList<Double>();
        this.x3data = new ArrayList<Double>();
        this.ydata = new ArrayList<Double>();
        this.w1 = 0.0;
        this.w2 = 0.0;
        this.w3 = 0.0;
        this.b = 0.0;
    }

    public void recomputeModel() {
        
    }

    public void printModel() {
        System.out.println("Model: w1 = " + this.w1 + " w2 = " + this.w2 + " w3 = " + this.w3 + " b = " + this.b);
    }

    public static void main(String[] args) {
        Early2 t = new Early2();
        for (int i = 0; i < 20; i++) {
            t.recomputeModel();
            t.printModel();
        }
    }
}