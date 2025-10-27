package Practice.Practice.service;

import Practice.Practice.dto.CatalogRequest;
import Practice.Practice.dto.CatalogResponse;
import Practice.Practice.entity.Catalog;
import Practice.Practice.entity.CatalogAudit;
import Practice.Practice.exception.CatalogNotFoundException;
import Practice.Practice.repository.CatalogAuditRepository;
import Practice.Practice.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CatalogService {

    private final CatalogRepository repository;
    private final CatalogAuditRepository auditRepository;

    public CatalogService(CatalogRepository repository, CatalogAuditRepository auditRepository){
        this.repository = repository;
        this.auditRepository = auditRepository;
    }

    @Transactional
    public CatalogResponse create(CatalogRequest request) {
        LocalDateTime now = LocalDateTime.now();

        //save catalog
        Catalog catalog = Catalog.builder()
                .name(request.name())
                .description(request.description())
                .price(BigDecimal.valueOf(request.price()))
                .status("ACTIVE")          // required for NOT NULL column
                .createdAt(now)            // required for NOT NULL column
                .updatedAt(now)            // required for NOT NULL column
                .build();
                repository.save(catalog);

        CatalogAudit audit = CatalogAudit.builder()
                .catalogId(catalog.getId())
                .action("CREATED")
                .actionTime(now)
                .build();
        auditRepository.save(audit);

        // ❌ Uncomment this line to test rollback
        throw new RuntimeException("Simulated failure: rollback demo!");

        //return toResponse(catalog);
    }

    @Transactional(readOnly = true)
    public CatalogResponse getById(Long id){
        Catalog catalog = repository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(id));
        return toResponse(catalog);
    }

    @Transactional(readOnly = true)
    public List<CatalogResponse> getAll(){
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public CatalogResponse update(Long id, CatalogRequest request){
        Catalog catalog = repository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(id));
        catalog.setName(request.name());
        catalog.setDescription(request.name());
        catalog.setPrice(BigDecimal.valueOf(request.price()));
        return toResponse(repository.save(catalog));
    }

    public void delete(Long id, boolean softDelete){
        Catalog catalog = repository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(id));
        if(softDelete) {
            catalog.setStatus("INACTIVE");
            repository.save(catalog);
        }else {
            repository.delete(catalog);
        }
    }

    private CatalogResponse toResponse(Catalog catalog){
        return new CatalogResponse(
                catalog.getId(),
                catalog.getName(),
                catalog.getDescription(),
                catalog.getPrice(),
                catalog.getStatus(),
                catalog.getCreatedAt(),
                catalog.getUpdatedAt()

        );
    }
}
