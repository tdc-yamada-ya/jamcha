package p05;

public class T01 {
    void m01(int n) {
        if (n <= 0) {
            System.out.println(n);
            return;
        }

        m02(n);
    }

    void m02(int n) {
        m03(n);
    }

    void m03(int n) {
        m01(n - 1);
    }
}
