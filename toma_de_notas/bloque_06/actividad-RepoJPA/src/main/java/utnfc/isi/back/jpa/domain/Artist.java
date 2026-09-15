package utnfc.isi.back.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Artist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "artistId")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArtistId")
    private Long artistId;

    @Column(name = "Name", nullable = false)
    private String name;
}
