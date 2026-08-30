public class Figura1 {
    public static void main(String[] args) {
        for (int fila = 1; fila <= 9; fila++) {
            if (fila <= 5) {
                for (int col = 1; col <= fila; col++) {
                    System.out.print('*');
                    System.out.print(' ');
                }
            } else {
                for (int col = 1; col <= 9 - fila; col++) {
                    System.out.print('*');
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }
}