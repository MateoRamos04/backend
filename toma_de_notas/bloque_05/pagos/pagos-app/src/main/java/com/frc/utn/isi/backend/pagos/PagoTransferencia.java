package com.frc.utn.isi.backend.pagos;

public class PagoTransferencia extends Pago {
    private String cbuAliasDestino;
    private String bancoOrigen;
    private double montoAgregado;

    public PagoTransferencia(double monto, String idTransaccion, String cbuAliasDestino, String bancoOrigen) {
        super(monto, idTransaccion);
        this.cbuAliasDestino = cbuAliasDestino;
        this.bancoOrigen = bancoOrigen;
    }

    @Override
    public void procesar() {
        montoAgregado = monto * 1.00035;
    }

    @Override
    public String generarRecibo() {
        return "Recibo de Pago por Transferencia\n" +
               "Monto: " + monto + "\n" +
               "Monto agregado: " + montoAgregado + "\n" +
               "CBU/Alias del destino: " + cbuAliasDestino + "\n" +
               "Banco de origen: " + bancoOrigen + "\n" +
               "Fecha: " + fecha + "\n" +
               "ID de Transacción: " + idTransaccion;
    }
    
}
