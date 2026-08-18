package utnfc.isi.backend;

/**
 *
 * @author Felipe
 */
public class Triangulo
{
    //Atributos
    private Vector lado1;
    private Vector lado2;
    private Vector lado3;
    
    //Constructor
    public Triangulo(Punto p1, Punto p2, Punto p3)
    {
        lado1 = new Vector(p1, p2);
        lado2 = new Vector(p2, p3);
        lado3 = new Vector(p3, p1);
        
    }

    public String toString()
    {
        return "Tirángulo con lados: {" + lado1 + ", " + lado2 + ", " + lado3 + "}";
        
    }
    
    public double perimetro()
    {
        return lado1.modulo() + lado2.modulo() + lado3.modulo();
    }
    
    public double area()
    {
        double s = perimetro() / 2;
        
        double area = Math.sqrt(s*(s - lado1.modulo()) *
                                    (s - lado2.modulo()) *
                                    (s - lado3.modulo()));
        
        return area;
    }
}
