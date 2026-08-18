package utnfc.isi.backend;

public class Fraccion {

    // Atributos (Datos miembro)
    private int numerador;
    private int denominador;

    // Constructores
    public Fraccion(int num, int den) {
        numerador = num;
        setDenominador(den);
    }

    public Fraccion(int num) {
        this(num, 1);

    }

    public Fraccion(Fraccion aCopiar) {
        this(aCopiar.numerador, aCopiar.denominador);
    }

    // Métodos
    public int getNumerador() {
        return numerador;
    }

    public int getDenominador() {

        return denominador;
    }

    private void setDenominador(int denominador) {
        if (denominador == 0) {
            throw new ArithmeticException("El denominador no puede ser cero");
        }

        this.denominador = denominador;

    }

    public Fraccion sumarA(Fraccion pData) {
        int wDenominador = this.denominador * pData.getDenominador();
        int wNumerador = (wDenominador / this.denominador * this.numerador)
                + (wDenominador / pData.getDenominador() * pData.getNumerador());

        Fraccion aux = new Fraccion(wNumerador, wDenominador);
        aux.simplificar();
        return aux;

    }

    public Fraccion multiplicarA(Fraccion pData) {
        int wNewNum = this.numerador * pData.getNumerador();
        int wNewDen = this.denominador * pData.getDenominador();

        Fraccion aux = new Fraccion(wNewNum, wNewDen);
        aux.simplificar();
        return aux;
    }

    public Fraccion inversa() {
        return new Fraccion(this.denominador, this.numerador);
    }

    public Fraccion dividirPor(Fraccion pData) {
        return multiplicarA(pData.inversa());

    }

    public Fraccion dividirPor(int num) {
        return dividirPor(new Fraccion(num));
    }

    public double valorReal() {
        double resp = numerador / (double) denominador;
        return resp;

    }

    public String toString() {
        return "[" + this.numerador + "/" + this.denominador + "]";

    }

    public boolean equals(Object otra) {
        if (this == otra) {
            return true;
        }
        if (!(otra instanceof Fraccion)) {
            return false;
        }
        Fraccion otraFraccion = (Fraccion) otra;
        return this.numerador == otraFraccion.numerador
                && this.denominador == otraFraccion.denominador;
    }
    
    public void simplificar() {
        int divisor = mcd(Math.abs(numerador), Math.abs(denominador));
        numerador /= divisor;
        denominador /= divisor;
    }

    private int mcd(int a, int b) {
        return b == 0 ? a : mcd(b, a % b);
    }

}
