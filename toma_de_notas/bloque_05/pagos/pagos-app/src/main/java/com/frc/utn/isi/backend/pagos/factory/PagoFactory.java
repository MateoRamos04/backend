package com.frc.utn.isi.backend.pagos.factory;

import com.frc.utn.isi.backend.pagos.Pago;
import com.frc.utn.isi.backend.pagos.PagoEfectivo;
import com.frc.utn.isi.backend.pagos.PagoTarjeta;
import com.frc.utn.isi.backend.pagos.PagoTransferencia;

public class PagoFactory {
    public static Pago generar(String tipoPago, String montoStr, String idTransaccion, String montoRecibidoStr, String tokenTarjeta, String marcaTarjeta, String cuotasStr, String cbuAliasDestino, String bancoOrigen) {
        switch (tipoPago.toUpperCase()) {
            case "EFECTIVO":
                double monto  = Double.parseDouble(montoStr);
                double montoRecibido = Double.parseDouble(montoRecibidoStr);
                return new PagoEfectivo(monto, idTransaccion, montoRecibido);
            
            case "TARJETA":
                String token = tokenTarjeta;
                String marca = marcaTarjeta;
                int cuotas = Integer.parseInt(cuotasStr);
                return new PagoTarjeta(Double.parseDouble(montoStr), idTransaccion, token, marca, cuotas);
            
            case "TRANSFERENCIA":
                String cbu = cbuAliasDestino;
                String banco = bancoOrigen;
                return new PagoTransferencia(Double.parseDouble(montoStr), idTransaccion, cbu, banco);

            default:
                throw new IllegalArgumentException("Tipo de pago no soportado: " + tipoPago);
        }
    }
}

    
