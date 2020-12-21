package p04;

public enum T01 {
    E01(Math.floor(100)),
    E02(Math.ceil(200));

    T01(double a) {
        System.out.println(a);
    }

    double m01(double a, double b) {
        return Math.max(a, b);
    }
}
