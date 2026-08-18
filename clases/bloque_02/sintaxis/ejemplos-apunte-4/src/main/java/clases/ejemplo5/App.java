package clases.ejemplo5;
/**
 * Ejemplo simple de programa en Java. En el método main, mostramos la forma de uso de las
 * principales instrucciones de control de flujo en Java.
 *
 */
public class App {
    public static void main(String args[]) {
        /*
         *  1.) Bien: no hay mucho que comentar ahora... sólo mirar la sintaxis de uso!!
         */

        /*
         * Caso 1: Condiciones. Notar el caracter de control \" para pedir que se muestre
         * una comilla...
         */
        int x = 10;
        boolean b = true;
        // condición doble
        if (x == 10) {
            System.out.println("Entramos por rama \"verdadera\"");
        } else {
            System.out.println("Entramos por rama \"falsa\"");
        }

        // condición simple
        if (b)  // o sea: if(b==true)...  Este chequeo implícito solo vale para variables boolean
        {
            System.out.println("Entramos por la única rama");
        }

        /*
         * Caso 2: Condición múltiple. Sin las instrucciones break al final de cada rama, al entrar por una de ellas
         * se enganchará con las siguientes como si de allí en adelante fueran todas verdaderas!!!
         * La rama default ES OPCIONAL: se activa solo si ningún case fue cierto.
         */
        int op = 2;
        switch (op) {
            case 1:
                System.out.println("Case 1");
                break;

            case 2:
                System.out.println("Case 2");
                break;

            case 3:
                System.out.println("Case 3");
                break;

            default:
                System.out.println("No fue ninguna!");
        }

        /*
         * Caso 3: Ciclos. En Java hay tres tipos: el while, el do while, y el for. Los tres repiten cuando la condición de
         * control es cierta, y cortan cuando es falsa. Se puede salir del interior de un ciclo sin importar el valor de la
         * condición de control usando la instrucción break (normalmente sujeta a un if)
         */
        int y = 1;
        while (y <= 3) {
            System.out.println("while: vuelta " + y);
            y = y + 1;
        }

        y = 1;
        do {
            System.out.println("do - while: vuelta " + y);
            y = y + 1;
        }
        while (y <= 3);

        // Veremos que y = y + 1 equivale a y++ en notación más breve
        for (y = 1; y <= 3; y++) {
            System.out.println("for: vuelta " + y);
        }

        y = 1;
        while (y <= 1000) {
            System.out.println("while con break: vuelta " + y);
            if (y == 3) {
                break;
            }  // cuando "y" vale 3, "break corta el ciclo sin evaluar la condición
            y++;
        }

        for (y = 1; y <= 3; y++) {
            if (y == 1) {
                continue;
            }  // si es la primera vuelta, "continue" fuerza a realizar otra vuelta
            System.out.println("for con continue: vuelta " + y);
        }

        // ¿Rarezas con el ciclo for?
        y = 1;
        for (; y <= 3; y++)  // dejamos vacía la sección de inicialización
        {
            System.out.println("for raro 1: vuelta " + y);
        }

        for (y = 1; y <= 3; )  // dejamos vacía la sección de incremento
        {
            System.out.println("for raro 2: vuelta " + y);
            y++;
        }

        y = 1;
        for (; y <= 3; )  // dejamos ambas vacías... Notar que el punto y coma al final de c/ sección va SIEMPRE
        {
            System.out.println("for raro 3: vuelta " + y);
            y++;
        }

        int a;
        for (y = 1, a = 0; y <= 3 && a != 10; y++) // si queremos más de una instrucción en una sección, separamos con comas...
        {
            System.out.println("for raro 4: vuelta " + y);
            a++;
        }

        // El que sigue provoca un ciclo infinito: la ausencia de condición de control equivale a un true...
       /*
       for(;;)
       {
       }
       */

        /*
         * Caso 4: Conteo y Acumulación
         * En Java, cualquier expresión de la forma general
         * var = var operador expresión;
         *
         * se puede escribir también en forma resumida:      var operador= expresión;
         * Ejemplos:     a = a + 1;    es también    a += 1;
         *               b = b + x;                  b += x;
         *               c = c + 2*r;                c += 2*r;
         *               d = d - 1;                  d -= 1;
         *               e = e * t;                  e *= t;
         *
         * Por otra parte, si desea contar o descontar de a uno, hay otras dos equivalencias:
         *           a = a + 1;  es lo mismo que  a += 1;  y también  a++;  y también   ++a;
         *           b = b - 1;                   b -= 1;             b--;              --b;
         */

        // Cuidado con el contexto...
        int c = 10, res;
        res = c++;  // es equivalente a:  res = c; c = c + 1; por lo tanto, res queda valiendo 10
        System.out.println("1.) Valor de res: " + res);

        c = 10;
        res = ++c;  // es equivalente a:  c = c + 1;  res = c; por lo tanto, res queda valiendo 11
        System.out.println("2.) Valor de res: " + res);
    }
}

