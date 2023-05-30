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

        //populate data
        this.x1data.add(1.0);
        this.x2data.add(1.0);
        this.x3data.add(1.0);
        this.ydata.add(44.95);

        this.x1data.add(1.0);
        this.x2data.add(2.0);
        this.x3data.add(3.0);
        this.ydata.add(112.2);

        this.x1data.add(2.0);
        this.x2data.add(2.0);
        this.x3data.add(2.0);
        this.ydata.add(81.2);

        this.x1data.add(3.0);
        this.x2data.add(3.0);
        this.x3data.add(3.0);
        this.ydata.add(117.45);

        this.x1data.add(4.0);
        this.x2data.add(4.0);
        this.x3data.add(4.0);
        this.ydata.add(153.7);

        this.x1data.add(16.0);
        this.x2data.add(3.0);
        this.x3data.add(0.0);
        this.ydata.add(44.45);

        this.x1data.add(5.0);
        this.x2data.add(5.0);
        this.x3data.add(5.0);
        this.ydata.add(189.95);

        this.x1data.add(6.0);
        this.x2data.add(6.0);
        this.x3data.add(6.0);
        this.ydata.add(226.2);

        this.x1data.add(7.0);
        this.x2data.add(7.0);
        this.x3data.add(7.0);
        this.ydata.add(262.45);
    }

    public void recomputeModel() {
        Double totalLossW1 = 0.0;
        Double totalLossW2 = 0.0;
        Double totalLossW3 = 0.0;
        Double totalLossB = 0.0;

        for (int i = 0; i < this.x1data.size(); i++) {
            totalLossW1 += ((-2) * this.x1data.get(i) * (this.ydata.get(i) - ((this.w1 * this.x1data.get(i)) + (this.w2 * this.x2data.get(i)) + (this.w3 * this.x3data.get(i)) + this.b)));
            totalLossW2 += ((-2) * this.x2data.get(i) * (this.ydata.get(i) - ((this.w1 * this.x1data.get(i)) + (this.w2 * this.x2data.get(i)) + (this.w3 * this.x3data.get(i)) + this.b)));
            totalLossW3 += ((-2) * this.x3data.get(i) * (this.ydata.get(i) - ((this.w1 * this.x1data.get(i)) + (this.w2 * this.x2data.get(i)) + (this.w3 * this.x3data.get(i)) + this.b)));
            totalLossB += ((-2) * (this.ydata.get(i) - ((this.w1 * this.x1data.get(i)) + (this.w2 * this.x2data.get(i)) + (this.w3 * this.x3data.get(i)) + this.b)));
        }

        Double lossAdjW1 = totalLossW1 / this.x1data.size();
        Double lossAdjW2 = totalLossW2 / this.x2data.size();
        Double lossAdjW3 = totalLossW3 / this.x3data.size();
        Double lossAdjB = totalLossB / this.x1data.size();

        this.w1 = this.w1 - (this.lr * lossAdjW1);
        this.w2 = this.w2 - (this.lr * lossAdjW2);
        this.w3 = this.w3 - (this.lr * lossAdjW3);
        this.b = this.b - (this.lr * lossAdjB);
    }

    public void printModel() {
        System.out.println("Model: w1 = " + this.w1 + " w2 = " + this.w2 + " w3 = " + this.w3 + " b = " + this.b);
    }

    public static void main(String[] args) {
        Early2 t = new Early2();
        t.printModel();
        for (int i = 0; i < 50; i++) {
            t.recomputeModel();
            t.printModel();
        }
    }
}