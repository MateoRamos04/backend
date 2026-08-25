package utnfc.backend.iterador;

/**
 * Clase que modela una fraccion con sus operaciones aritmeticas.
 *
 * @author Felipe
 * @version 15/04/2013
 */
public class Fraccion implements Comparable
{
    private int numerador;
    private int denominador;

    public Fraccion(int num, int den)
    {
        numerador = num;
        setDenominador(den);
    }

    public Fraccion(int num)
    {
        this(num, 1);
    }

    public int getNumerador()
    {
        return numerador;
    }

    public int getDenominador()
    {
        return denominador;
    }

    private void setDenominador(int den)
    {
        if (den != 0)
            denominador = den;
        else
            denominador = 1;
    }

    public double valorReal()
    {
        return numerador / (double) denominador;
    }

    public Fraccion sumarA(Fraccion pData)
    {
        int wDenominador = denominador * pData.getDenominador();
        int wNumerador = (wDenominador / denominador * numerador)
                + (wDenominador / pData.getDenominador() * pData.getNumerador());
        Fraccion aux = new Fraccion(wNumerador, wDenominador);
        aux.simplificar();
        return aux;
    }

    public Fraccion multiplicarA(Fraccion pData)
    {
        Fraccion aux = new Fraccion(numerador * pData.getNumerador(),
                denominador * pData.getDenominador());
        aux.simplificar();
        return aux;
    }

    public Fraccion inversa()
    {
        return new Fraccion(denominador, numerador);
    }

    public Fraccion dividirPor(Fraccion pData)
    {
        return multiplicarA(pData.inversa());
    }

    public Fraccion dividirPor(int num)
    {
        return dividirPor(new Fraccion(num));
    }

    public boolean mayorQue(Fraccion x)
    {
        return valorReal() > x.valorReal();
    }

    @Override
    public String toString()
    {
        return "[" + numerador + "/" + denominador + "]";
    }

    public void simplificar()
    {
        boolean wFlag = true;
        int wPivot;
        while (wFlag)
        {
            wFlag = false;
            wPivot = obtenerPivot();
            for (int i = 2; i < wPivot; i++)
            {
                if (numerador % i == 0 && denominador % i == 0)
                {
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
        if (numerador > denominador)
            return numerador;
        return denominador;
    }

    @Override
    public int compareTo(Object o)
    {
        if (!(o instanceof Fraccion))
            throw new ClassCastException();

        Fraccion aux = (Fraccion) o;
        aux.simplificar();
        simplificar();
        if (aux.getNumerador() == numerador && aux.getDenominador() == denominador)
            return 0;
        if (valorReal() > aux.valorReal())
            return 1;
        return -1;
    }
}
