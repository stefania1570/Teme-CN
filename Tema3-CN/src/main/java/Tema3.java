import Jama.Matrix;
import Jama.QRDecomposition;

import java.util.Random;

public class Tema3 {

    public static void afisareMatrice(double[][] b) {
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static double[][] createMatrix(int size) {
        double[][] matrix = new double[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextDouble(); ;
            }

        }

        return matrix;
    }

    private static double[][] createRandomVector(int size) {
        double[][] vector = new double[size][1];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            vector[i][0] = random.nextDouble();
        }

        return vector;
    }

    public static double[][] calculMatrice_b(double[][] A, double[][] s) {
        int size = A.length;
        double[][] b = new double[size][1];

        for (int i = 0; i < size; i++) {
            b[i][0] = 0;
            for (int j = 0; j < size; j++) {
                b[i][0] += A[i][j] * s[j][0];
            }
        }

        return b;
    }


    public static void descompunereQR(double[][] A, double[][] Q, double[][] b) {
        int n = A.length;
        double y;

        for (int r = 0; r < n-1 ; r++) {
           //System.out.println("R="+r);

            double sigma = 0;
            for (int i = r; i < n; i++) {
                sigma += A[i][r] * A[i][r];
            }

            if (sigma <= 1e-15) {
                break;
            }

            double k = Math.sqrt(sigma);
            if (Math.abs(A[r][r]) > 1e-15){ //if(Arr > 0)
                k = -k;
            }

            double beta = sigma - k * A[r][r];

            double[] u = new double[n];
            u[r] = A[r][r] - k;
            for (int i = r + 1; i < n; i++) {
                u[i] = A[i][r];
            }

//            for(int i=0;i<n;i++){
//                System.out.println(u[i]);
//            }

            for (int j = r + 1; j < n; j++) {
                y = 0;
                for (int i = r; i < n; i++) {
                    y += u[i] * A[i][j];
                }
                y /= beta;

                for (int i = r; i < n; i++) {
                    A[i][j] -= y * u[i];
                }
            }

            A[r][r] = k;
            for (int i = r + 1; i < n; i++) {
                A[i][r] = 0;
            }

            // Facem vectorul b
            y = 0;
            for (int i = r; i < n; i++) {
                y += u[i] * b[i][0];
            }
            y /= beta;

            for (int i = r; i < n; i++) {
                b[i][0] -= y * u[i];
            }

            // Q
            for (int j = 0; j < n; j++) {
                y = 0;
                for (int i = r; i < n; i++) {
                    y += u[i] * Q[i][j];
                }
                y /= beta;

                for (int i = r; i < n; i++) {
                    Q[i][j] -= y * u[i];
                }
            }
            //afisareMatrice(Q);
        }

    }

    private static double[] substitutieInversa(double[][] A, double[][] b) {
        //Ax=b
        int n = A.length;
        double[] x = new double[n];

        for (int i = n-1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i][0]-sum)/A[i][i];
        }

        return x;
    }


    private static double[] scadereVectori(double[] A,double[] B){ //pt aflarea normei
        double[] result = new double[B.length];
        for (int i = 0; i < B.length; i++) {
            result[i] = A[i] - B[i];
        }
        return result;
    }

    //dadea eroare undeva, e pusa doar ca sa mearga implementarea mea pt ca am facut b-ul double[][]
    private static double[] scadereVectori2(double[] A,double[][] B){ //pt aflarea normei
        double[] result = new double[B.length];
        for (int i = 0; i < B.length; i++) {
            result[i] = A[i] - B[i][0];
        }
        return result;
    }

    private static double normaEuclidiana(double[] vector) {
        double sum = 0;
        for (double element : vector) {
            sum += Math.pow(element, 2);
        }
        return Math.sqrt(sum);
    }

    //dadea eroare undeva, e pusa doar ca sa mearga implementarea mea pt ca am facut s-ul double[][]
    private static double normaEuclidiana2(double[][] vector) {
        double sum = 0;
        for (int i=0;i<vector.length;i++) {
            sum += Math.pow(vector[i][0], 2);
        }
        return Math.sqrt(sum);
    }
    private static double[] inmultireMatrici(double[][] A,double[] B) {
        double[] multiplicationResult = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            double sum = 0;
            for (int j = 0; j < A[i].length; j++) {
                sum += A[i][j] * B[j];
            }
            multiplicationResult[i] = sum;
        }
        return multiplicationResult;
    }

    public static double[][] calculateInverse(double[][] Q, double[][] R) {
        int n = Q.length;
        double[][] inversa = new double[n][n];

        for (int j = 0; j < n; j++) {
            // Rezolvam Rx=b pt fiecare coloana din inversa
            double[][] b = new double[n][1];

            for (int i = 0; i < n; i++) {
                b[i][0]=Q[i][j];
                //System.out.println("b["+i+"]="+ b[i][0]);
            }

            double[] x = substitutieInversa(R, b);
            for (int i = 0; i < n; i++) {
                //System.out.println("x["+i+"]="+ x[i]);
            }

            // Punem solutia x in coloana j din inversa
            for (int i = 0; i < n; i++) {
                inversa[i][j] = x[i];
            }
        }

        return inversa;
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


    public static void main(String[] args) {
        int n = 3;
        double[][] A = {
                {0, 0, 4},
                {1, 2, 3},
                {0, 1, 2}
        };
//        double[][] A = createMatrix(n); //PENTRU TESTAREA CU DIMENSIUNI MARI

        //copie la matricea A
        double[][] Ainit = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            Ainit[i] = A[i].clone();
        }

//        double[][] s = createRandomVector(n);
        double[][] s = {
                {3},
                {2},
                {1}
        };

        double[][] b = calculMatrice_b(A, s);

        double[][] bINIT = new double[b.length][];
        for (int i = 0; i < b.length; i++) {
            bINIT[i] = b[i].clone();
        }

        // Afiseaza b-ul
        System.out.println("ex1: Matricea b: ***********************************");
        afisareMatrice(b);

        //SUBPUNCTUL 2 - ALG. HOUSEHOLDER

        //Initializam matricea Q transp = In (1 pe diag principala,0 in rest)
        double[][] Q = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i==j){
                    Q[i][j] = 1;
                }else{
                    Q[i][j] = 0;
                }
            }
        }


        descompunereQR(A, Q, b);
        System.out.println("ex2: Matricea R: *******************************************");
        afisareMatrice(A);
        System.out.println("Matricea Qtr:");
        afisareMatrice(Q);
        //System.out.println("Matricea b dupa aplicarea descompunerii => adica devine Qtr*b");
        //afisareMatrice(b);


        double[] xMEU = substitutieInversa(A,b);
        System.out.println("ex3: Solutie dupa aplicarea alg. lui HOUSEHOLDER, x = : ");
        for (int i = 0; i < xMEU.length; i++) {
            System.out.println(xMEU[i]);
        }

        //Facem descompunerea QR si aflam solutia xQR folosind biblioteca JAMA

        Matrix A2 = new Matrix(A);
        Matrix b2 = new Matrix(b);

        // Obtinem descompunerii QR
        QRDecomposition qr = new QRDecomposition(A2);
        Matrix Q2 = qr.getQ();
        Matrix R2 = qr.getR();

        // Obtinem solutia sistemului
        Matrix y = Q2.transpose().times(b2);
        Matrix xQR = R2.solve(y);

        // Afisarea solutiei
        System.out.println("Solutia pentru Ax=b folosind biblioteca:");
        xQR.print(4, 2);

        //Calcul si afisare norma
        double[] result1 = scadereVectori(xMEU,xQR.getColumnPackedCopy());

        double euclideanNorm1 = normaEuclidiana(result1);
        System.out.printf("%nNorma euclidiana ||xQR - xHouseholder||: %.25f%n", euclideanNorm1);

        if (euclideanNorm1 < 1e-6){
            System.out.println("Rezultatul este corect deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

        System.out.println("ex4: ************************************************");
       //NORMA 1
        double[] multiplicationResult = inmultireMatrici(Ainit,xMEU);
        double[] result2 = scadereVectori2(multiplicationResult,bINIT);

        double euclideanNorm2 = normaEuclidiana(result2);
        System.out.printf("%nNorma euclidiana1 de la ex 4: ||A_init*xHouseholder - b_init|| = %.25f%n", euclideanNorm2);

        if (euclideanNorm2 < 1e-6){
            System.out.println("Rezultatul1 este corect deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

        //NORMA 2
        double[] multiplicationResult2 = inmultireMatrici(Ainit, xQR.getColumnPackedCopy());
        double[] result3 = scadereVectori2(multiplicationResult2,bINIT);

        double euclideanNorm3 = normaEuclidiana(result3);
        System.out.printf("%nNorma euclidiana2 de la ex 4: ||Ainit*xQR - b_init|| =  %.25f%n", euclideanNorm3);

        if (euclideanNorm3 < 1e-6){
            System.out.println("Rezultatul2 este corect deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

        //NORMA 3
        double[] result4 = scadereVectori2(xMEU,s);

        double euclideanNorm4 = normaEuclidiana(result4);
        double euclideanNorm5 = normaEuclidiana2(s);
        euclideanNorm4 /= euclideanNorm5;
        System.out.printf("%nNorma euclidiana3 de la ex 4: ||xHouseholder-s|| / ||s|| = %.25f%n", euclideanNorm4);

        if (euclideanNorm4 < 1e-6){
            System.out.println("Rezultatul3 este corect deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

        //NORMA 4
        double[] result5 = scadereVectori2(xQR.getColumnPackedCopy(), s);

        double euclideanNorm6 = normaEuclidiana(result5);
        euclideanNorm6 /= euclideanNorm5; //folosesc pe aia de mai sus
        System.out.printf("%nNorma euclidiana4 de la ex 4: ||xQR-s|| / ||s|| %.25f%n", euclideanNorm6);

        if (euclideanNorm6 < 1e-6){
            System.out.println("Rezultatul4 este corect deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

        System.out.println("\nex5: INVERSA HOUSEHOLDER: ***************************");
        double[][] inversa = calculateInverse(Q,A);
        afisareMatrice(inversa);

        Matrix A3 = new Matrix(Ainit);
        Matrix inversaBibl = A3.inverse();
        System.out.println("\n    INVERSA BIBLIOTECA: ***************************");
        inversaBibl.print(5, 2);

        double[][] result6 = scadereMatrici(inversa, inversaBibl.getArray());
        double euclideanNorm7 = normaMatricealaFrobenius(result6);
        System.out.printf("%nNorma euclidiana ex 5: ||inversaHouseholder - inversaBibl|| = %.25f%n", euclideanNorm7);

        if (euclideanNorm7 < 1e-6){
            System.out.println("Norma euclidiana inverse este corecta deoarece norma euclidiana este mai mica decat 10 la puterea -6.");
        }

    }
}

