package com.INT.robot.util;

public final class ChineseRemainderTheorem {

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