package seleksi.labpro.owca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director", nullable = false)
    private String director;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime created_at;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updated_at;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ElementCollection
    @CollectionTable(name = "film_genres", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "genre")
    private List<String> genres;
}
