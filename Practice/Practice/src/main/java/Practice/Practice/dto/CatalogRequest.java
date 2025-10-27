package Practice.Practice.dto;

import jakarta.validation.constraints.NotBlank;

public record CatalogRequest (

        @NotBlank String name,

        String description,

        double price
){}
