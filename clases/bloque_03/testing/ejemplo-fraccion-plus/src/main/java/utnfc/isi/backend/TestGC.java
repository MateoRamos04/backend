package utnfc.isi.backend;

public class TestGC
{
   public static void main(String[] args) throws Exception
   {
        Fraccion aux;
        while (true)
        {
            aux = new Fraccion((int)(Math.random() * 7 + 1), (int)(Math.random() * 9 + 1));
            System.out.println("Fracción creada: " + aux);
//            Thread.sleep(10);
            System.gc();

        }
        
       
   }
}
