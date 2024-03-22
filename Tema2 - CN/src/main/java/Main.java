import Jama.Matrix;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    
    public static void descompunereLU(double[][] A) {
        int n = A.length;
        final double epsilon = 1e-6;
        // 1. La fiecare pas se calculeaza simultan cate o coloana din matricea L si cate o linie din matricea U
        // 1.1: Det. coloana p a matricei L

        for (int p = 0; p < n; p++) { // p apartine [1,n]
            //System.out.println("P = " + (p + 1));

            for (int i = p; i < n; i++) {
                double sum = 0;
                for (int k = 0; k < p; k++) {
                    sum += A[i][k] * A[k][p];
                }
                A[i][p] = A[i][p] - sum;
                //System.out.println("A[" + i + "][" + p + "] = " + A[i][p]);
            }

            // 1.2: Det. linia p a matricei U

            for (int i = p + 1; i < n; i++) {
                double sum2 = 0;
                for (int k = 0; k < p; k++) {
                    sum2 += A[p][k] * A[k][i];
                }
                if (Math.abs(A[p][p]) > epsilon) { //FOLOSIM EPSILON !! = A[p][p] != 0
                    A[p][i] = (A[p][i] - sum2) / A[p][p];
                    //System.out.println("A[" + p + "][" + i + "] = " + A[p][i]);
                } else {
                    System.out.println("Matricea A are un minor nul => nu se poate calcula descompunerea.");
                    p = n + 10; // break
                }
            }
        }

        // Afiseaza matricea A, care contine atat L, cat si U
        System.out.println("Matricea A (LU):");
        for (double[] row : A) {
            for (double element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }


    // Formula det(A)=l11*...*lnn
    public static double calculDeterminant(double[][] A){
        double det = 1;
        int n = A.length;
        for (int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i==j){
                    det = det * A[i][j];
                }
            }
        }
        return det;
    }

    private static double[] substitutieDirecta(double[][] L, double[] b) {
        int n = L.length;
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = (b[i] - sum) / L[i][i];
        }

        return y;
    }

    private static double[] substitutieInversa(double[][] U, double[] y) {
        int n = U.length;
        double[] x = new double[n];

        for (int i = n-1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = y[i]-sum;
        }

        return x;
    }


    private static double normaEuclidiana(double[] vector) {
        double sum = 0;
        for (double element : vector) {
            sum += Math.pow(element, 2);
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

    private static double[] scadereVectori(double[] A,double[] B){
        double[] result = new double[B.length];
        for (int i = 0; i < B.length; i++) {
            result[i] = A[i] - B[i];
        }
        return result;
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

    private static double[] createRandomVector(int size) {
        double[] vector = new double[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            vector[i] = random.nextDouble();
        }

        return vector;
    }



    public static void main(String[] args) {

        int size = 100;
//        double[][] A = createMatrix(size); //PENTRU TESTAREA CU DIMENSIUNI MARI
        double[][] A = {
                {2.5, 2, 2},
                {5, 6, 5},
                {5, 6, 6.5}
        };
//       double[] B = createRandomVector(size);
        double[] B = {2, 2, 2};

        // Ainit este copia matricii A initiale
        double[][] Ainit = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            Ainit[i] = A[i].clone();
        }

        descompunereLU(A);
        System.out.println("\nCalcul determinant matricea A = "+ calculDeterminant(A));

        // Substitutia directa
        double[] y = substitutieDirecta(A, B);

        // Substitutia inversa
        double[] xLU = substitutieInversa(A, y);

        // Aflam solutia xLU
        System.out.println("\nxLU =");
        for (double element : xLU) {
            System.out.println(element);
        }

        //Verificam sol cu norma euclidiana ||Ainit*xLU - b||

        //1. Inmultim matricile
        double[] multiplicationResult = inmultireMatrici(Ainit,xLU);

        //2. Facem scaderea vectorilor
        double[] result = scadereVectori(multiplicationResult,B);

        //3. Aflam norma
        double euclideanNorm = normaEuclidiana(result);
        System.out.printf("%nNorma euclidiana: %.25f%n", euclideanNorm);

        if (euclideanNorm < 1e-8){
            System.out.println("Rezultatul este corect deoarece norma euclidiana este mai mica decat 10 la puterea -8.");
        }

        //FOLOSIM BIBLIOTECA JAMA
        Matrix A2 = new Matrix(Ainit);
        Matrix B2 = new Matrix(B, B.length);

        // Aflam solutia
        Matrix xLIB = A2.solve(B2);
        // Afisarea solutiei x
        System.out.println("Solutia sistemului Ax = b:");
        xLIB.print(10, 5);

        // Facem norma1
        double normaJAMA1 = normaEuclidiana(scadereVectori(xLU, xLIB.getColumnPackedCopy()));
        System.out.printf("%nNorma euclidiana JAMA: %.25f%n", normaJAMA1);

        if (normaJAMA1 < 1e-8){
            System.out.println("Rezultatul este corect deoarece norma euclidiana este mai mica decat 10 la puterea -8.");
        }

        // Facem inversa
        Matrix inverseA = A2.inverse();

//        // Afisarea inversei matricei A2
//        System.out.println("\nInversa matricei A2:");
//        inverseA.print(10, 5);

        // Facem norma2
        double[] rezInmultireLIB = inmultireMatrici(inverseA.getArray(),B); //un fel de castare de la Matrix la double[][]
        double normaJAMA2 = normaEuclidiana(scadereVectori(xLU, rezInmultireLIB));
        System.out.printf("%nNorma euclidiana JAMA (inversa): %.25f%n", normaJAMA2);

        if (normaJAMA2 < 1e-8){
            System.out.println("Rezultatul este corect deoarece norma euclidiana este mai mica decat 10 la puterea -8.");
        }

    }
}
