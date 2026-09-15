package utnfc.isi.back.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Album")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "albumId")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AlbumId")
    private Long albumId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ArtistId")
    private Artist artist;

    @Column(name = "Title", nullable = false)
    private String title;
}
