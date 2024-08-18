package seleksi.labpro.owca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 1000)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        this.created_at = OffsetDateTime.now();
        this.updated_at = OffsetDateTime.now(); // Optionally set both at create time
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_at = OffsetDateTime.now();
    }
}