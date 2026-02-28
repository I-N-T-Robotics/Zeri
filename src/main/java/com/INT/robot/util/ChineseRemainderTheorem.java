package com.INT.robot.util;

public final class ChineseRemainderTheorem {
    // private double targetRatio ;

    //TODO: look to reset values at the beginning-ish of the auto
    // public ChineseRemainderTheorem( double teethA, double teethB )
    // {
    //     targetRatio = teethB/ teethA ;
    // }

    // public double getARotationNonLame( double encoderA, double encoderB )
    // {
    //     double stepA, stepB, istepB ;

    //     stepB = encoderB ;
    //     for (int i = 20 ; (i -- ) != 0 ; ) {
    //         istepB = 1 / ( stepB ) ;
    //         stepA = encoderA * istepB ;
    //         for (int j= 20; ( j -- ) != 0 ; ) {
    //             double dv = Math.abs( stepA - Math.floor(stepA) - targetRatio) ;
    //             if ( dv < 0.01 ) {
    //                 return ((20 - j) + encoderA ) ;
    //             }
    //             stepA += istepB ;
    //         }
    //         stepB += 1 ;
    //     }
    //     return 0. ;
    // }

    // static double[] array1;
    // static double[] array2;

    // //A = 60, B = 43, C = 14, ratio = 136, output1 = 60T, output2 = 43T
    // public static double getTurretRotations(double teethA, double teethB, double teethC, double turretRatio, double encoderOutput1, double encoderOutput2) {
    //     double at = encoderOutput1;
    //     double bt = encoderOutput2;

    //     for (int i = 0; i < 10; i++) { //increase 10 to something else
    //         array1 = new double[] { //bigger tooth gear
    //             at/360 + i
    //         };
    //     }
        
    //     for (int j = 0; j < 10; j++) {
    //         array2 = new double[] {
    //             bt/360 + j
    //         };
    //     }

    //     int k1 = 0;
    //     int k2 = 0;
    //     double output = array1[k1]/array2[k2]; // 43/60
    //     double ratio = teethB/teethA;

    //     while (output != ratio) { //add tolerance
    //         if (k2 != 10) { //TODO: optimize search range: ex: (k2-k1 >= 3)
    //             k2++;
    //             output = array1[k1]/array2[k2];
    //         } else {
    //             k1++;
    //             k2 = k1;
    //         }
    //         return k1;
    //     }

    //     double absoluteFinal = (((at/360) + k1) / teethC) * teethA;

    //     return absoluteFinal/turretRatio;
    //     //return rotations
    // }

    //being used
    public static double getTurretRotations(
            double encoderARevs,
            double encoderBRevs,
            int teethA,
            int teethB,
            double turretRatio
    ) {
        // Convert wrapped encoder rotations into modular tooth indices
        double eA = mod((encoderARevs * teethA), teethA);
        double eB = mod((encoderBRevs * teethB), teethB);

        // Reduce turret ratio into each modulus
        double rA = mod(turretRatio, teethA);
        double rB = mod(turretRatio, teethB);

        // Modular inverses
        double invRA = modInverse(rA, teethA);
        double invRB = modInverse(rB, teethB);

        // Undo gear scaling
        double a = mod(invRA * eA, teethA);
        double b = mod(invRB * eB, teethB);

        // CRT solve
        int m1 = teethA;
        int m2 = teethB;
        int M  = m1 * m2;

        int n1 = m2;
        int n2 = m1;

        double invN1 = modInverse(n1, m1);
        double invN2 = modInverse(n2, m2);

        double theta = mod(
                a * n1 * invN1 +
                b * n2 * invN2,
                M
        );

        // Convert CRT space into turret rotations
        return theta / turretRatio;
    }

    /* ===================== MATH HELPERS ===================== */

    private static double mod(double value, int modulus) {
        double result = value % modulus;
        return result < 0 ? result + modulus : result;
    }

    /**
     * Extended Euclidean Algorithm for modular inverse
     * Assumes gcd(a, m) = 1
     */
    private static double modInverse(double a, double m) {
        double m0 = m;
        double x0 = 0;
        double x1 = 1;

        while (a > 1) {
            double q = a / m;

            double temp = m;
            m = a % m;
            a = temp;

            temp = x0;
            x0 = x1 - q * x0;
            x1 = temp;
        }

        return x1 < 0 ? x1 + m0 : x1;
    }
}