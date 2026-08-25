package utnfc.back.bolitas;

/**
 * Hello world!
 *
 */
public class App
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Tramo tb1, tb2, tb3, tb4, tb5, dl, dp, ac, dt, tc, dv;

        tb1 = new Tubo(2500, "tubo1");
        tb2 = new Tubo(7, "tubo2");
        tb3 = new Tubo(3, "tubo3");
        tb4 = new Tubo(12, "tubo4");
        tb5 = new Tubo(9, "tubo5");
        // Crear acá instancias de las nuevas clases

        dl = new DesvioLisa("desvío lisa 1");
        dp = new DesvioPeso(25, "desvío peso 1");

        ac = new Acelerador("acelerador 1");
        dt = new Detencion(7, "detension 1");

        tc = new TuboCurvo("Tubo curvo", 35, 3);
        //dv = new DesvioVelocidad(10, "Desvío Velocidad");

        tb1.encastrarSalida(dl);

        dl.encastrarSalida(tb2);

        ((Desvio)dl).setAlternativa(tb3);

        tb2.encastrarSalida(ac);

        ac.encastrarSalida(dt);

        tb3.encastrarSalida(tc);

        tc.encastrarSalida(dv);

        dv.encastrarSalida(tb4);

        //((Desvio)dv).setAlternativa(tc);

        // Encastrar las instancias de la nueva clase en el circuito

        Bolita bt, bn, bp;
        //bp = new BolitaConHistoria(false, 22, 0.001);
        bp = new Bolita(false, 22, 1);

        System.out.println("Prueba de Bolitas Viajeras");
        System.out.println("Primera Prueba: Transportar: " + bp);

        tb1.transportar(bp);

        System.out.println("Al finalizar: " + bp);

        bp = new Bolita(false, 22, 1);

        bn = new BolitaConHistoria(true, 19, 1);

        System.out.println("Segunda Prueba: Usando el avanzar. Bolita: " + bn);

        System.out.println("Avance paso a paso");
        Tramo tramo = tb1;
        while (tramo != null)
        {
            tramo = tramo.avanzar(bn);
            System.out.println("Bolita  => Tramo: "
                               + bn.getTramoActual() + " | " + bn);
            System.out.println(bn);

        }

        System.out.println("Al finalizar: " + bn);

        bt = new Bolita(false, 26, 1);

    }
}
