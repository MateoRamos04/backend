package com.frc.utn.isi.backend.pagos;

import java.time.LocalDateTime;

public abstract class Pago {
    protected double monto;
    protected String idTransaccion;
    protected LocalDateTime fecha;

    public Pago(double monto, String idTransaccion) {
        this.monto = monto;
        this.idTransaccion = idTransaccion;
        this.fecha = LocalDateTime.now();
    }

    public abstract void procesar();
    public abstract String generarRecibo();

}
