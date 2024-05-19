import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.NaN;
import static java.lang.Math.max;
import static java.lang.Math.pow;

public class Tema7 {

    public static Double metodaMuller(double x0,double x1,double x2, double[] coef) {
        double epsilon = 1e-25;
        double deltaX = 0;
        int kmax = 15;

        int k = 3;
        do {
            double h0 = x1 - x0;
            double h1 = x2 - x1;

            double Px1 = schemaHorner(coef, x1); //calcul cu schema lui horner //P(x_k-1)
            double Px0 = schemaHorner(coef, x0);
            double delta0 = (Px1 - Px0) / h0;
            //System.out.println("Px1="+Px1+", Px0="+Px0+", delta0 ="+delta0);


            double Px2 = schemaHorner(coef, x2); //calcul cu schema lui horner
            double delta1 = (Px2 - Px1) / h1;
            //System.out.println("Px2="+Px2+", delta1 ="+delta1);

            // calcul a b c
            double a = (delta1 - delta0) / (h1 + h0);
            double b = a * h1 + delta1;
            double c = Px2;//P(xk)
            //System.out.println("a="+a+", b="+b+", c ="+c);

            double b2minus4ac = pow(b, 2) - 4 * a * c;
            if (b2minus4ac < 0) {
                System.out.println("STOP: Radacini complexe.");
                break;
            }
            if (Math.abs(b + Math.signum(b) * Math.sqrt(b2minus4ac)) < epsilon) {
                System.out.println("STOP.");
                break;
            }
            deltaX = 2 * c / (b + Math.signum(b) * Math.sqrt(b2minus4ac));
            double x3 = x2 - deltaX;
            k = k + 1;

            x0 = x1;
            x1 = x2;
            x2 = x3;
        } while ((Math.abs(deltaX) >= epsilon) && (k <= kmax) && (Math.abs(deltaX) <= pow(10, 8)));

        if (Math.abs(deltaX) < epsilon){
            System.out.println("xk ~ x*: " + x2);
            return x2;
        } else{
            System.out.println("Divergenta.");
            return NaN;
        }
    }


    public static double schemaHorner(double[] c, double x0) {
        double d = c[0];
        for (int i = 1; i < c.length; i++) {
            d = d * x0 + c[i];
        }
        return d;
    }

    public static void main(String[] args) {
       //Calculul intervalului [-R,R]
        double[] a1 = {1.0,-6.0,11.0,-6};
        //int n = a1.length;

        // Aflam A - maximul dintre coeficienti incepand cu pozitia 1
        double A = Arrays.stream(a1, 1, a1.length)
                .max()
                .orElse(Integer.MIN_VALUE);

        double R = (Math.abs(a1[0])+A) / Math.abs(a1[0]);
        System.out.println("\nex1: Toate radacinile reale ale polinomului P1 se afla in intervalul["+(-R)+","+R+"]");

        //ex 2: Metoda Muller de aprox a radacinilor

        List<Double> rezultate = new ArrayList<Double>();
        //exemplul 1
        double x0 = 1.3;
        double x1 = 0.9;
        double x2 = 1.2;

        rezultate.add(metodaMuller(x0,x1,x2,a1)); //pt radacina 1

        x0 = 0.214;
        x1 = 1.2345;
        x2 = 2.1;

        rezultate.add(metodaMuller(x0,x1,x2,a1)); //pt radacina 2

        x0 = 1.214;
        x1 = 2.2345;
        x2 = 3.1;

        rezultate.add(metodaMuller(x0,x1,x2,a1));// pt radacina 3

        double[] a2 = {42.0,-55.0,-42.0,49.0,-6.0};
        A = Arrays.stream(a2, 1, a2.length)
                .max()
                .orElse(Integer.MIN_VALUE);

        R = (Math.abs(a1[0])+A) / Math.abs(a1[0]);
        System.out.println("\nex2:Toate radacinile reale ale polinomului P2 se afla in intervalul["+(-R)+","+R+"].");

        x0 = 0.13;
        x1 = 0.2345;
        x2 = 1.1;

        rezultate.add(metodaMuller(x0,x1,x2,a2));// pt radacina 2/3 = 0.6666

        x0 = 0.8854;
        x1 = 1.875;
        x2 = 0.234;

        rezultate.add(metodaMuller(x0,x1,x2,a2));// pt radacina 1/7=0.1428

        x0 = -0.8854;
        x1 = -1.875;
        x2 = -1.234;

        rezultate.add(metodaMuller(x0,x1,x2,a2));// pt radacina -1

        x0 = 1.0987;
        x1 = 0.98205;
        x2 = 1.789;

        rezultate.add(metodaMuller(x0,x1,x2,a2));// pt radacina 3/2=1.5



        try (PrintWriter writer = new PrintWriter(new FileWriter("rezultate.txt"))) {
            for (Double rezultat : rezultate) {
                writer.println(rezultat);
            }
            System.out.println("Rezultatele distincte au fost scrise in fisierul 'rezultate.txt'");
        } catch (IOException e) {
            System.err.println("A apărut o eroare la scrierea in fisier: " + e.getMessage());
        }
    }
}