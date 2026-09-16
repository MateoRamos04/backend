package ar.edu.univ.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LIQUIDACIONES")
public class LiquidacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TARJETA", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TarjetaEntity tarjeta;

    @Column(name = "MES", nullable = false)
    private Integer mes;

    @Column(name = "ANIO", nullable = false)
    private Integer anio;

    @Column(name = "TOTAL_A_PAGAR", nullable = false)
    private Double totalAPagar;

    @Column(name = "TOTAL_CONSUMOS", nullable = false)
    private Double totalConsumos;

    @Column(name = "TOTAL_IMPUESTOS", nullable = false)
    private Double totalImpuestos;

    @Column(name = "TOTAL_DESCUENTOS", nullable = false)
    private Double totalDescuentos;
}
