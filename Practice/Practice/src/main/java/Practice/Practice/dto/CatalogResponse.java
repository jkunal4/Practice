package Practice.Practice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record CatalogResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
