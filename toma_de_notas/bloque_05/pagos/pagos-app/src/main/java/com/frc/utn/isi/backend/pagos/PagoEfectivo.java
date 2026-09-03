package com.frc.utn.isi.backend.pagos;

public class PagoEfectivo extends Pago {
    private double montoRecibido;
    private double vuelto;

    public PagoEfectivo(double monto, String idTransaccion, double montoRecibido) {
        super(monto, idTransaccion);
        this.montoRecibido = montoRecibido;

        
    }

    
    public void procesar() {
        if (montoRecibido < monto) {
            throw new IllegalArgumentException("El monto recibido es menor al monto a pagar.");
        } else {
            this.vuelto = montoRecibido - monto;
        }
    }

    @Override
    public String generarRecibo() {
        return "Recibo de Pago en Efectivo\n" +
               "Monto: " + monto + "\n" +
               "Monto recibido: " + montoRecibido + "\n" +
               "Vuelto: " + vuelto + "\n" +
               "Fecha: " + fecha + "\n" +
               "ID de Transacción: " + idTransaccion;
               
    }
}
