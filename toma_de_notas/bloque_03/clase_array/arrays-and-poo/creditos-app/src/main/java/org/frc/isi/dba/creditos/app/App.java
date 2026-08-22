package org.frc.isi.dba.creditos.app;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        int[] vector = new int[10];
        vector[0] = (int) (Math.random() * 100 + 2);

        for (int i = 0; i < vector.length; i++) {
            vector[i] = (int) (Math.random() * 100 + 2);
        }

        int [] prueba = {10, 20, 30, 40, 50};
        int suma = 0;
        for (int i = 0; i < prueba.length; i++) {
            System.out.println(prueba[i] * 2 );
            suma += prueba[i];
        }

        System.out.println(vector.length);
        System.out.println(prueba.length);
        System.out.println(suma);

    }

}


