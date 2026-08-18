package utnfc.isi.backend;

public class Fraccion
{
    // Atributos (Datos miembro)
    private int numerador;
    private int denominador;
    
    // Constructores
    public Fraccion(int num, int den)
    {
        numerador = num;
        setDenominador(den);
    }

    public Fraccion(int num)
    {
        this(num, 1);

    }

    public Fraccion(Fraccion aCopiar) {
        this(aCopiar.numerador, aCopiar.denominador);
    }

    // Métodos
    public int getNumerador() {
        return numerador;
    }

//    public void setNumerador(int numerador) {
//        this.numerador = numerador;
//    }


    public int getDenominador() {

        return denominador;
    }

    private void setDenominador(int denominador)   {
        if (denominador != 0)
            this.denominador = denominador;
        else
            this.denominador = 1;

    }

    public Fraccion sumarA(Fraccion pData)
    {
        int wDenominador = this.denominador * pData.getDenominador();
        int wNumerador = (wDenominador / this.denominador * this.numerador) +
                (wDenominador / pData.getDenominador() * pData.getNumerador());

        Fraccion aux = new Fraccion(wNumerador, wDenominador);
        aux.simplificar();
        return aux;

    }

    public Fraccion multiplicarA(Fraccion pData)
    {
        int wNewNum = this.numerador * pData.getNumerador();
        int wNewDen = this.denominador * pData.getDenominador();

        Fraccion aux = new Fraccion(wNewNum, wNewDen);
        aux.simplificar();
        return aux;
    }

    public Fraccion inversa()
    {
        return new Fraccion(this.denominador, this.numerador);
    }

    public Fraccion dividirPor(Fraccion pData)
    {
        return multiplicarA(pData.inversa());

    }

    public Fraccion dividirPor(int num)
    {
        return dividirPor(new Fraccion(num));
    }

    public double valorReal()
    {
        double resp = numerador / (double) denominador;
        return resp;

    }

    public String toString()
    {
        return "[" +this.numerador + "/" + this.denominador + "]";
        // return STR."[\{ this.numerador }/\{ this.denominador }]"; // Preview featrure, está deshabilitado por defecto

    }

    public void simplificar()
    {
        boolean wFlag = true;
        int wPivot;

        while (wFlag)
        {
            wFlag = false;
            wPivot = this.obtenerPivot();
            for (int i = 2; i < wPivot; i++)
            {
                if ((numerador % i) == 0 && (denominador % i) == 0) {
                    numerador /= i;
                    denominador /= i;
                    wFlag = true;
                    break;
                }

            }

        }

    }

    private int obtenerPivot()
    {
        int wPivot;
        if (numerador > denominador) {
            wPivot = numerador;
        } else {
            wPivot = denominador;
        }
        return wPivot;
    }

    public void finalize()
    {
        System.out.println("Finalizo fracción: " + this);

    }

}
