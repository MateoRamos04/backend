package com.frc.utn.isi.backend.pagos;

public class PagoTarjeta extends Pago {
    private String tokenTarjeta;
    private String marcaTarjeta;
    private int cuotas;
    private int interes;

    public PagoTarjeta(double monto, String idTransaccion, String tokenTarjeta, String marcaTarjeta, int cuotas) {
        super(monto, idTransaccion);
        this.tokenTarjeta = tokenTarjeta;
        this.marcaTarjeta = marcaTarjeta;
        this.cuotas = cuotas;
    }

    @Override
    public void procesar() {
        if (cuotas > 3) {
            this.interes = monto * 0.03; // Recargo del 2% por cuotas mayores a 3
        }
    }

    @Override
    public String generarRecibo() {
        return "Recibo de Pago por Tarjeta\n" +
               "Monto: " + monto + "\n" +
               "Token de la tarjeta: " + tokenTarjeta + "\n" +
               "Marca de la tarjeta: " + marcaTarjeta + "\n" +
               "Cuotas: " + cuotas + "\n" +
               "Fecha: " + fecha + "\n" +
               "ID de Transacción: " + idTransaccion;
    }
}