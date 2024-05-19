import java.util.Random;

public class Tema2BONUS {


    public static void descompunereLU(double[][] A, double[] L, double[] U) {
        int n = A.length;
        final double epsilon = 1e-6;

        // 1. La fiecare pas se calculeaza simultan cate o coloana din matricea L si cate o linie din matricea U
        // 1.1: Det. coloana p a matricei L


        for (int p = 0; p < n; p++) { // p apartine [1,n]
            //System.out.println("P = " + (p));


            for (int i = p; i < n; i++) {
                //System.out.println("i = " + (i));
                double sum = 0;
                for (int k = 0; k < p; k++) {
                    sum += (L[i*(i+1)/2+k] * U[k*(k+1)/2+p]);
                }
                if(i==p){
                    U[i*(i+1)/2+p]=1;
                }
                L[i*(i+1)/2+p] = A[i][p] - sum;
                //System.out.println("L[" + (i*(i+1)/2+p) + "] = " + L[i*(i+1)/2+p]);

            }

            // 1.2: Det. linia p a matricei U
//            System.out.println("\nPENTRU U:\n");

            for (int i = p ; i < n ; i++) {
                //System.out.println("i = " + (i));
                double sum2 = 0;
                for (int k = 0; k < p; k++) {
                    sum2 += (L[p*(p+1)/2+k] * U[k*(k+1)/2+i]);
                }
                if(i==p){
                    U[i*(i+1)/2+p]=1;
                }

                if (Math.abs(L[p*(p+1)/2+p]) > epsilon) { //FOLOSIM EPSILON !! = A[p][p] != 0
                    U[p*(p+1)/2+i] = (A[p][i] - sum2) / L[p*(p+1)/2+p];
                    //System.out.println("U[" + (p*(p+1)/2+i) + "] = " + U[p*(p+1)/2+i]);

                } else {
                    System.out.println("Matricea A are un minor nul => nu se poate calcula descompunerea.");
                    p = n + 10; // break
                }
            }
        }

        // Afiseaza vectorul L si U
        System.out.println("Vectorul L:");
        for (int i=0;i<L.length;i++){
            System.out.println("L[" + i + "] = " + L[i]);
        }

        System.out.println("Vectorul U:");
        for (int i=0;i<U.length;i++){
            System.out.println("U[" + i + "] = " + U[i]);
        }
    }

    private static double[] substitutieDirecta(double[] L, double[] b) {
        int n = b.length;
        double[] y = new double[b.length];
        int j;

        for (int i = 0; i < n; i++) {
            double sum = 0;
            for ( j = 0; j < i; j++) {
                sum += L[i*(i+1)/2+j] * y[j];
            }
            y[i] = (b[i] - sum) / L[i*(i+1)/2+j];
        }

        return y;
    }

    private static double[] substitutieInversa(double[] U, double[] y) {
        int n = y.length;
        double[] x = new double[n];

        for (int i = n-1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i*(i+1)/2+j] * x[j];
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
        int n = 3;

        double[][] A = {
                {2, 0, 2},
                {1, 2, 5},
                {1, 1, 7}
        };

        double[] B = {4, 10, 10};

        // Ainit este copia matricii A initiale
        double[][] Ainit = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            Ainit[i] = A[i].clone();
        }

        double[] L = new double[n*(n+1)/2];
        double[] U = new double[n*(n+1)/2];
        

        descompunereLU(A,L,U);

        // Substitutia directa
        double[] y = substitutieDirecta(L, B);

        // Substitutia inversa
        double[] xLU = substitutieInversa(U, y);

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

    }
}
