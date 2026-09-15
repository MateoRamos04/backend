package utnfc.isi.back.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Track")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "trackId")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TrackId")
    private Long trackId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "AlbumId")
    private Album album;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Milliseconds", nullable = false)
    private Integer milliseconds;
}
