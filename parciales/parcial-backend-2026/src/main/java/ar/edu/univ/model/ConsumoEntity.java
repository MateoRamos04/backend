package ar.edu.univ.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONSUMOS")
public class ConsumoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_TARJETA", nullable = false)
    private TarjetaEntity tarjeta;

    @Column(name = "MONTO", nullable = false)
    private Double monto;

    @Column(name = "DIA", nullable = false)
    private Integer dia;

    @Column(name = "MES", nullable = false)
    private Integer mes;

    @Column(name = "ANIO", nullable = false)
    private Integer anio;

    @Column(name = "RUBRO", nullable = false, length = 20)
    private String rubro;

    @Column(name = "MONEDA", nullable = false, length = 3, insertable = false, updatable = false)
    private String moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MONEDA", referencedColumnName = "MONEDA", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CotizacionEntity cotizacion;

}
