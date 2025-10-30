package Practice.Practice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many reviews belong to one catalog
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @Column(nullable = false, length = 255)
    private String reviewerName;

    @Column(nullable = false)
    private int rating; // e.g., 1 to 5 stars

    @Column(length = 500)
    private String comment;

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        reviewedAt = LocalDateTime.now();
    }
}
