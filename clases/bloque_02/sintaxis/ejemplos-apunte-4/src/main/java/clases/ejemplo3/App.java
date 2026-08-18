package clases.ejemplo3;

/**
 * Ejemplo simple de programa en Java. El método main contiene una serie de variables
 * de tipos primitivos para mostrar el uso de los mismos, y la forma de realizar casting
 * ("moldeo") de valores.
 *
 * El único propósito de este programa es probar situaciones de compilación con asignaciones
 * cruzadas. No tiene más salida por consola que un mensaje final y unos pocos mensajes de test.
 *
 */
public class App
{
    public static void main (String args[])
    {
        /*
         * Caso 1
         * Con los valores integrales, las asignaciones son tomadas a menos que el valor supere
         * el rango aceptado para el tipo de la variable
         */
        byte  bX = 127;         // entero (un byte), con signo entre -128 y 127. Con 128 no compila!
        short sX = 32767;       // entero (2 bytes), con signo entre -32768 y 32767.
        int   iX = 1000000;     // entero (4 bytes), con signo entre -2^31 y 2^31 -1
        long  lX = 1000000000;  // entero (8 bytes), con signo entre -9e18 y 9e18 -1

        /*
         *  Caso 2
         *  Con los flotantes, las constantes "sueltas" son double...
         */
        float  fX = 2.34f;  // flotante (4 bytes), valores con hasta 6 - 7 posiciones decimales
        double dX = 3.45;   // flotante (8 bytes), valores con hasta 14 - 15 posiciones decimales

        /*
         * Caso 3
         * Hacemos casting:  Encerramos entre paréntesis el nombre del tipo al que quiero llevar el valor
         * y lo asignamos a la variable. Nos permite pasar de un tipo "mayor" a otro "menor" que sea compatible,
         * aunque podemos perder precisión o truncar valores
         */
        /* sX = iX; */  // no compila... int es mayor que short
        sX = (short)iX; // hacemos casting: compila pero trunca el valor original...
        System.out.println("Caso 3: bX= " + bX + " sX= " + sX);

        /*
         * Caso 4
         * Al sumar variables de tipos integrales, la suma da
         * int si los términos son int o menos que int, y da
         * long si aparece algún término long
         * Si sumamos constantes integrales, lo que importa es el resultado
         */
        byte bX2 = 1;
        bX = 10;
        sX = 1000;
        iX = 10000;
        /*sX = iX + sX; */  // no compila: el resultado es int
        iX = iX + sX;       // ok: los términos son int y short, el resultado es int
        lX = iX + sX;       // ok: el resultado es int (y cabe en long)
        /*sX = sX + bX2;*/  // no compila: la suma de dos variables "menores" que int, da int
        /*int bX3 = bX2 + bX;*/ // no compila: el resultado da int
        bX = 1 + 4;         // ok: los términos son constantes y la suma no sobrepasa el rango byte
        /*bX = 1 + 200; */  // no compila: el resultado sobrepasa el rango byte


        /*
         * Caso 5
         * Casting cruzado: asignamos valores de tipos incompatibles moldeando el valor, pero con posible
         * pérdida por redondeo
         */
        fX = 2.34f;
        /*iX = fX;*/          // no compila...
        iX = (int)fX;         // ok, pero trunca los decimales...
        lX = (long)dX;        // ídem...
        char cX = 'A', cX2;   // caracteres simples: se encierran entre apóstrofos
        //bX = cX;            // no compila
        bX  =  (byte)cX;      // asigna el valor UNICODE del caracter 'A' (que es el número 65)
        cX2 =  (char)bX;             // asigna el caracter que corresponde al UNICODE 65 (que es la 'A'...)
        boolean bolX = true;
        //iX = bolX;       // no compila
        /*iX =(int) bolX; */  // no compila: el tipo boolean no se puede moldear con NADIE
        System.out.println("Caso 5: iX= " + iX + " lX= " + lX + " bX= " + bX + " cX2= " + cX2);

        /* Caso 6
         * Si en una suma aparece un término flotante, el resultado es flotante
         */
        fX = 2.34f;
        iX = 10;
        fX = fX + iX;          // ok
        /*iX = iX + fX;*/      // no compila: el resultado es float
        iX = (int) (iX + fX);  // ok: puede truncar decimales. Notar los paréntesis en (iX + fx): sin ellos no compila
        iX = (int) fX + iX;    // ok: ¿en qué cambió? ¿porqué ahora sí compila y no necesitamos los paréntesis?

        System.out.println("Programa terminado");
    }
}
