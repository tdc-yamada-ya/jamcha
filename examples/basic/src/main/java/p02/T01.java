package p02;

public class T01 {
    void m01(double a, double b) {
        System.out.println(a + b);
    }

    double m02(double a) {
        return Math.floor(a);
    }

    double m03(double a) {
        return Math.ceil(a);
    }

    void m04(double a, double b) {
        double c = m02(a);
        double d = m03(b);
        m01(c, d);
    }
    
    void m05(String a, String b) {
        double c = Double.parseDouble(a);
        double d = Double.parseDouble(b);
        m04(c, d);
    }
}
