package p08;

public class T02 implements T01 {
    @Override
    public void m01(double a) {
        double b = Math.ceil(a);
        System.out.println(b);
    }
}
