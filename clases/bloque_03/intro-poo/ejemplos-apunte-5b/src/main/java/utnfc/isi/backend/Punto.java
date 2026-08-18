package utnfc.isi.backend;

public class Punto
{
    //Atributos
    private double x;
    private double y;
    
    //Construtores
    public Punto(double x, double y)
    {
        this.x = x;
        this.y = y;
        
    }
    
    public Punto(Punto p)
    {
       this(p.getX(), p.getY());
       // this.x = p.getX();
       // this.y = p.getY();
    }
    
    //Métodos
    public String toString()
    {
        //Variable para armar la cadena
        String s;
        //Formo la cadena
        s = "(" + x + ":" + y + ")";
        //Devuelvo la cadena
        return s;
        
        
    }
    
    public double distanciaOrigen()
    {
        double sumaCuadrados = x * x + y * y;

        double distancia = Math.sqrt(sumaCuadrados);

        return distancia;
        
//       Vector v = new Vector(new Punto(x, y));
//       Vector v = new Vector(this);
//
//       return v.distancia();
        
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }

    public boolean estaEjeX()
    {
        boolean resp = false;
        if (y == 0)
            resp = true;
        return resp;
    }
    
    public boolean estaEjeY()
    {
//        boolean resp = x == 0;
//        return resp;
        
        return x == 0;
    }
    
    public boolean estaOrigen()
    {
        return estaEjeX() && estaEjeY();
    }

}
