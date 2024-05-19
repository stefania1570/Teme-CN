import java.util.Arrays;

public class Tema5 {
    public static void metodaJacobi(double[][] A, int kmax) {
        int n = A.length;
        int k = 0;
        double[][] U = new double[n][n];
        for (int i = 0; i < n; i++) {
            U[i][i] = 1; // initializare U cu matricea identitate
        }

        double[][] Ainit = A.clone();

        int[] pq = calculIndiciPQ(A);
        int p = pq[0];
        int q = pq[1];
        //System.out.println("p=" + p + ", q=" + q);

        double[] unghiTheta = calculUnghiTheta(A, p, q);
        double theta = unghiTheta[0]; 
        double c = unghiTheta[1];
        double s = unghiTheta[2];
        double t = unghiTheta[3];
        //System.out.println("c=" + c + ", s=" + s + ", t=" + t);

        while (!isDiagonal(A) && k < kmax) {

            //Aici facem formula de la (5): A=R*A*R_transp
            calculMatriceDeRotatie(A, p, q, c, s, t);
            
            //formula de la (7) pt U=U*R_transp
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double aux = U[i][p]; // Uip veche
                    U[i][p] = -c * U[i][p] - s * U[i][q];
                    U[i][q] = s * aux - c * U[i][q];
                }
            }
            
            //calculam din nou p,q,theta,c,s,t
            pq = calculIndiciPQ(A);
            p = pq[0];
            q = pq[1];
            unghiTheta = calculUnghiTheta(A, p, q);
            theta = unghiTheta[0];
            c = unghiTheta[1];
            s = unghiTheta[2];
            t = unghiTheta[3];
            //System.out.println("c=" + c + ", s=" + s + ", t=" + t);

            k++;
        }

        double[] diagonala = new double[n];
        double[][] eigenvectors = new double[n][n];
        for (int i = 0; i < n; i++) {
            diagonala[i] = A[i][i];
            for (int j = 0; j < n; j++) {
                eigenvectors[j][i] = U[j][i];
            }
        }

        System.out.println("Valori proprii: " + Arrays.toString(diagonala));
        System.out.println("Vectori proprii:");
        for (double[] vector : eigenvectors) {
            System.out.println(Arrays.toString(vector));
        }

        //verificare
        double[][] rezstanga = inmultireMatrici(Ainit,U);
        double[][] rezdreapta = inmultireMatrici(A,U);
        System.out.printf("Rezultatul este corect deoarece norma matriceala Frobenius ||Ainit*U-U*A|| = %.25f%n ", normaMatricealaFrobenius(scadereMatrici(rezstanga,rezdreapta)));

    }

    private static double[][] inmultireMatrici(double[][] A,double[][] B) {
        double[][] c = new double[A.length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B.length; j++) {
                c[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    c[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return c;
    }

    public static double normaMatricealaFrobenius(double[][] matrix) {
        double sumOfSquares = 0.0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sumOfSquares += Math.pow(matrix[i][j], 2);
            }
        }

        return Math.sqrt(sumOfSquares);
    }

    public static double[][] scadereMatrici(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }

        return result;
    }

    public static void afisareMatrice(double[][] b) {
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static int[] calculIndiciPQ(double[][] A) {
        int n = A.length;
        double maxVal = 0;
        int[] pq = new int[2];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(A[i][j]) > maxVal) {
                    maxVal = Math.abs(A[i][j]);
                    pq[0] = i;
                    pq[1] = j;
                }
            }
        }
        return pq;
    }


    public static double[] calculUnghiTheta(double[][] A, int p, int q) {
        double theta;
        double c;
        double s;

        double alfa = (A[p][p] - A[q][q]) / 2 * A[p][q];
        double t;
        if (Math.signum(alfa) < 10e-10) { //*
            t = -alfa + Math.sqrt(alfa * alfa + 1);
        } else {
            t = -alfa - Math.sqrt(alfa * alfa + 1);
        }

        c = 1 / Math.sqrt(1 + t * t);
        s = t / Math.sqrt(1 + t * t);

        theta = Math.atan(t);
        theta = Math.toDegrees(theta);

        return new double[]{theta, c, s, t};
    }

    public static void calculMatriceDeRotatie(double[][] A, int p, int q, double c, double s, double t) {
        int n = A.length;

        //calculam in A
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j != p && j != q) {
                    A[p][j] = c * A[p][j] + s * A[q][j];
                    A[q][j] = -s * A[j][p] + c * A[q][j];
                    A[j][q] = -s * A[j][p] + c * A[q][j];
                    A[j][p] = A[p][j];
                }
            }
        }

        A[p][p] = A[p][p] + t * A[p][q];
        A[q][q] = A[q][q] - t * A[p][q];
        A[p][q] = 0;
        A[q][p] = 0;

    }

    public static boolean isDiagonal(double[][] A) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && Math.abs(A[i][j]) > 1e-10) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        double[][] A = {{0, 0, 1},
                        {0, 0, 1},
                        {1, 1, 1}};
        int kmax = 100;
        metodaJacobi(A, kmax);

    }
}



