package utnfc.isi.backend;

public class Prueba
{
    public static void main(String[] args)
    {
        //Declaro una referencia a Fraccion
        Fraccion f1 = null;

//        f1 = new Fraccion();
//         Instancio el objeto.
//         f1 = new Fraccion(); // Utiliza el constructor por defecto y no compila porque al agregar constructor con
         f1 = new Fraccion(2, 7); // Utiliza el constructor por defecto y no compila porque al agregar constructor con
                                // parámetros el constructor por defecto deja de existir
        System.out.println(f1.toString());
        System.out.println("\tValor real f1: " + f1.valorReal()); // provocaría un error ya que aún no creamos el objeto.


        Fraccion f2 = new Fraccion(f1);


//        f2.setNumerador(9);
//        System.out.println("["+ f2.getNumerador() + "/" + f2.getDenominador() + "]");
        System.out.println(f2);
        System.out.println("\tValor real f2: " + f2.valorReal()); // provocaría un error ya que aún no creamos el objeto.
        System.out.println("\tValor real f1: " + f1.valorReal()); // provocaría un error ya que aún no creamos el objeto.

//        f1.numerador = 3; // compila porque los atributos están definidos com privados.
//        f1.denominador = 5;


//        f1 = new Fraccion(2, 3); // creamos la fracción 2 tercios
//        System.out.println(f1); // al imprimir invocamos el método toString, notar que se invoca de manera implícita
//        System.out.println(f1.valorReal()); // mostramos el valor real de la fracción al usar el comportamiento para eso

        System.out.println("Fin!");

    }

}



