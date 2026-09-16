package ar.edu.univ.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TARJETAS")
public class TarjetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO", nullable = false, unique = true, length = 16)
    private String numero;

    @Column(name = "TITULAR", nullable = false, length = 100)
    private String titular;

    @Column(name = "LIMITE_CREDITO", nullable = false)
    private Double limiteCredito;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ConsumoEntity> consumos;

    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<LiquidacionEntity> liquidaciones;
}
