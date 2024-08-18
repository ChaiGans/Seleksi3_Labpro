package seleksi.labpro.owca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "films")
@Builder
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length=1000, nullable = false)
    private String description;

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

    @ManyToMany(mappedBy = "ownedFilms")
    private List<User> users;

    @ElementCollection
    @CollectionTable(name = "film_genres", joinColumns = @JoinColumn(name = "film_id"))
    @Column(name = "genre")
    private List<String> genres;


    @OneToMany(mappedBy = "film")
    private Set<Review> reviews;

    @ManyToMany(mappedBy = "wishlistFilms")
    private Set<User> wishlistedBy;
}
