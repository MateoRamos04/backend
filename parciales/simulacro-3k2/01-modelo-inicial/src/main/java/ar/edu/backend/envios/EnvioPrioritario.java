package ar.edu.backend.envios;

public class EnvioPrioritario extends Envio{
    public EnvioPrioritario(String id, String zona, int gramos){
        super(id, zona, gramos);
        if (gramos > 5000) {
            throw new IllegalArgumentException("prioritario supera 5000g: " + gramos);
        }
    }

    @Override public String modalidad() {return"Prioritario";}
    @Override public int costo() {return super.costo() + 400;}
}
