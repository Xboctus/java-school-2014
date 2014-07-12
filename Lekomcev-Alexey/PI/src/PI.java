import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class PI {

    public static void main(String[] args)
    {
        double pid;
        double s1;
        double s2;
        double s3;
        double s4;
        final int NHX = 16;
        int id = 1000000;
        char[] chx = new char[NHX];

/*  id is the digit position.  Digits generated follow immediately after id. */

        s1 = series(1, id);
        s2 = series(4, id);
        s3 = series(5, id);
        s4 = series(6, id);
        pid = 4. * s1 - 2. * s2 - s3 - s4;
        pid = pid - (int) pid + 1.;
        ihex(pid, NHX, chx);
        System.out.printf(" position = %d\n fraction = %.15f \n hex digits =  %10.10s\n", id, pid, chx);
    }

    /*  This returns, in chx, the first nhx hex digits of the fraction of x. */
    static void ihex (double x, int nhx, char[] chx)
    {
        int i;
        double y;
        String hx = "0123456789ABCDEF";
        y = abs(x);

        for (i = 0; i < nhx; i++){
            y = 16. * (y - floor (y));
            chx[i] = hx.charAt((int) y);
        }
    }

    /*  This routine evaluates the series  sum_k 16^(id-k)/(8*k+m)
    using the modular exponentiation technique. */
    static double series (int m, int id)
    {
        int k;
        double ak;
        double eps = 1e-17;
        double p;
        double s = 0;
        double t;

        /*Sum the series up to id. */
        for (k = 0; k < id; k++) {
            ak = 8 * k + m;
            p = id - k;
            t = expm(p, ak);
            s = s + t / ak;
            s = s - (int) s;
        }

    /*  Compute a few terms where k >= id. */
        for (k = id; k <= id + 100; k++) {
            ak = 8 * k + m;
            t = pow(16., (double) (id - k)) / ak;
            if (t < eps) break;
            s = s + t;
            s = s - (int) s;
        }
        return s;
    }

    /*  expm = 16^p mod ak.  This routine uses the left-to-right binary
    exponentiation scheme. */
    static double expm(double p, double ak) {
        int i;
        int j;
        double p1;
        double pt;
        double r;
        int ntp = 25;
        double[] tp = new double[ntp];
        int tp1 = 0;

    /*  If this is the first call to expm, fill the power of two table tp. */
        if (tp1 == 0) {
            tp1 = 1;
            tp[0] = 1.;
            for (i = 1; i < ntp; i++) tp[i] = 2. * tp[i - 1];
        }

        if (ak == 1.) return 0.;

    /*  Find the greatest power of two less than or equal to p. */

        for (i = 0; i < ntp; i++) if (tp[i] > p) break;

        pt = tp[i - 1];
        p1 = p;
        r = 1.;

    /*  Perform binary exponentiation algorithm modulo ak. */

        for (j = 1; j <= i; j++) {
            if (p1 >= pt) {
                r = 16. * r;
                r = r - (int) (r / ak) * ak;
                p1 = p1 - pt;
            }
            pt = 0.5 * pt;
            if (pt >= 1.) {
                r = r * r;
                r = r - (int) (r / ak) * ak;
            }
        }
    return r;
    }
}
