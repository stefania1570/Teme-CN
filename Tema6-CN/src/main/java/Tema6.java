import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Jama.Matrix;

class DateInput {
    int n;
    double x0;
    double xn;
    double xDeAproximat;
    double[] y;
}

public class Tema6 {

    public static void formulaNewtonSchemaAitken(int n, double x0, double xn, double[] y, double xDeAproximat){
        double h = (xn-x0)/n;

        // vectorul cu valorile x
        double[] x = new double[n];
        x[0] = x0;
        x[n - 1] = xn;
        for (int i = 1; i <= n-2; i++) {
            x[i] = x[0]+i*h;
        }

        //calcul schema Aitken -diferentele finite
        for (int i = 0; i <= n; i++){
            for(int j = n;j >= i+1;j--){
                y[j]=y[j]-y[j-1];
            }
        }

        //y la final dupa calcul
//        for (int i = 0; i <= n; i++){
//            System.out.println("y["+i+"] = "+y[i]);
//        }

        double t = (xDeAproximat-x0)/h;
        double s=t; //initial primul s=t adica s1=(x-x0)/h
                    //s-ul se va calcula progresiv in for mai jos
        double Ln=y[0]+y[1]*s; //adunam primii 2 termeni pt Ln

        for (int k = 2; k <= n; k++){
            s = s*((t-k+1)/k);
            Ln += y[k]*s;
        }

        System.out.println("\n ex1: Formula Newton + schema Aitken: L"+n+"("+xDeAproximat+") = "+ Ln);
        System.out.println("|Ln(x)-f(x)| = "+ Math.abs(Ln - 30.3125)); //???asa trebuie??
    }
    public static void citireFisier(String numeFisier,int exempluDorit,DateInput date){
        int contorExemple = 1;  // contor pentru a numara exemplele gasite

        try (BufferedReader br = new BufferedReader(new FileReader(numeFisier))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (contorExemple == exempluDorit) {
                    // citim datele din fisier -> prima linie gasim n,x0,xn,x de aproximat in ordinea asta, despartite prin spatiu. apoi pe urm rand gasim y
                    String[] valori = linie.split(" ");
                    //Arrays.stream(valori).forEach(System.out::println);
                    date.n = Integer.parseInt(valori[0]);
                    date.x0 = Double.parseDouble(valori[1]);
                    date.xn = Double.parseDouble(valori[2]);
                    date.xDeAproximat = Double.parseDouble(valori[3]);

                    // citim vectorul y
                    date.y = new double[date.n+1];
                    String[] vectorYString = br.readLine().split(" ");
                    for (int i = 0; i <= date.n; i++) {
                        date.y[i] = Double.parseDouble(vectorYString[i]);
                    }

                    // am citit datele exemplului, putem iesi din bucla
                    break;
                }else if (linie.trim().isEmpty()) { //continuam cautarea exemplului dorit
                    // daca intalnim o linie goala inseamna ca trecem la un alt exemplu
                    contorExemple++; // incrementam contorul de exemple gasite
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea din fisier: " + e.getMessage());
        }

    }

    public static double schemaHorner(double[] c, double x0) {
        double d = c[c.length-1];
        for (int i = c.length - 2; i >= 0; i--) {
            d = d * x0 + c[i];
        }
        return d;
    }

    public static void metodaCelorMaiMiciPatrate(int n, double x0, double xn, double[] y, double xDeAproximat){

        System.out.println("\n ex2: Metoda celor mai mici patrate:");

        double h = (xn-x0)/n;

        // vectorul cu valorile x
        double[] x = new double[n+1];
        x[0] = x0;
        x[n-1] = xn;
        for (int i = 1; i <= n; i++) {
            x[i] = x[0]+i*h;
        }

        int m=n+1;
        //formam matricea B si vectorul f
        double[][] B = new double[m][m];
        double[] f = new double[m];
        for (int i = 0; i < m; i++) {
            //B*a
            for (int j = 0; j < m; j++) {
                for (int k = 0; k <= n; k++) {
                    B[i][j] += Math.pow(x[k], i + j);
                }
            }
            // f
            for (int k = 0; k <= n; k++) {
                f[i] += y[k] * Math.pow(x[k], i);
            }
        }


        // rezolvam sistemul Ba=f
        Matrix matrixB = new Matrix(B);
        Matrix vectorF = new Matrix(f, f.length);
        Matrix a = matrixB.solve(vectorF);
//        System.out.println("Solutia sistemului:");
//        a.print(5, 3);

        double result = schemaHorner(a.getColumnPackedCopy(), xDeAproximat);
        System.out.println("Rezultatul schemei Horner pentru polinomul dat este Pm(x) = : " + result);
        String rezultatFormatare = String.format("%.5f", result);
        result = Double.parseDouble(rezultatFormatare); //am limitat la 5 zecimale
        System.out.println("|Pm(x)-f(x)| = "+ Math.abs(result - 30.3125));


        double suma2 = 0;
        for(int i=0;i<x.length;i++){
            //aflu Pm(x)
            double rez = Math.round(schemaHorner(a.getColumnPackedCopy(), x[i]));
            //System.out.println("Pm(x"+i+") = " + rez);


            //aflu diferenta in modul dintre Pm si yi
            double dif = Math.abs(rez-y[i]);
            //adaug diferenta la suma
            suma2 += dif;
        }

        System.out.println("Suma de valori absolute este: " + suma2);


    }

    public static void main(String[] args) {

        String numeFisier = "C:\\Users\\Stefania\\OneDrive\\Desktop\\Teme CN\\Tema6-CN\\src\\main\\java\\date.txt";


        DateInput date = new DateInput();

        double[] y = null;

        citireFisier(numeFisier,2,date); // (primul exemplu e cel rezolvat, al doilea e cel de pe ultima pagina de la tema)
        formulaNewtonSchemaAitken(date.n,date.x0,date.xn,date.y,date.xDeAproximat);

        citireFisier(numeFisier,2,date);
        metodaCelorMaiMiciPatrate(date.n,date.x0,date.xn,date.y,date.xDeAproximat);

    }
}
