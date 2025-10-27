package Practice.Practice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "catalog_audit")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CatalogAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "catalog_id", nullable = false)
    private Long catalogId;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(name = "action_time", nullable = false)
    private LocalDateTime actionTime;
}
