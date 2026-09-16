package ar.edu.univ.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COTIZACIONES")
public class CotizacionEntity {

    @Id
    @Column(name = "MONEDA", length = 3)
    private String moneda;

    @Column(name = "TASA_CAMBIO", nullable = false)
    private Double tasaCambio;
}
