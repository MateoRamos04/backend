package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                case 2:
                case 3:
                case 4:
                    System.out.print("Ingrese el primer numero: ");
                    double num1 = teclado.nextDouble();
                    System.out.print("Ingrese el segundo numero: ");
                    double num2 = teclado.nextDouble();
                    double resultado = 0;
                    boolean error = false;

                    switch (opcion) {
                        case 1:
                            resultado = Calculadora.sumar(num1, num2);
                            break;
                        case 2:
                            resultado = Calculadora.restar(num1, num2);
                            break;
                        case 3:
                            resultado = Calculadora.multiplicar(num1, num2);
                            break;
                        case 4:
                            if (num2 == 0) {
                                System.out.println("Error: no se puede dividir por cero.");
                                error = true;
                            } else {
                                resultado = Calculadora.dividir(num1, num2);
                            }
                            break;
                    }

                    if (!error) {
                        System.out.println("El resultado es: " + resultado);
                    }
                    break;

                case 5:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opcion invalida. Intente de nuevo.");
            }

            System.out.println();

        } while (opcion != 5);

        teclado.close();
    }

    public static void mostrarMenu() {
        System.out.println("===== MENU =====");
        System.out.println("1. Sumar");
        System.out.println("2. Restar");
        System.out.println("3. Multiplicar");
        System.out.println("4. Dividir");
        System.out.println("5. Salir");
        System.out.print("Elija una opcion: ");
    }
}
