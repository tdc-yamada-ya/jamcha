package p06;

public class T01 {
    void m01(int n) {
        if (n <= 0) {
            System.out.println(n);
            return;
        }

        T02 a = new T02();
        a.m01(n);
    }
}
