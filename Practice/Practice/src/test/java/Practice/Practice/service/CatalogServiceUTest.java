package Practice.Practice.service;

import Practice.Practice.dto.CatalogRequest;
import Practice.Practice.dto.CatalogResponse;
import Practice.Practice.entity.Catalog;
import Practice.Practice.entity.CatalogAudit;
import Practice.Practice.exception.CatalogNotFoundException;
import Practice.Practice.repository.CatalogAuditRepository;
import Practice.Practice.repository.CatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceUTest {

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private CatalogAuditRepository auditRepository;

    @InjectMocks
    private CatalogService catalogService;

    private Catalog catalog;
    private CatalogRequest request;

    @BeforeEach
    void setUp() {
        catalog = Catalog.builder()
                .id(1L)
                .name("Phone")
                .description("Smartphone")
                .price(BigDecimal.valueOf(999.99))
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        request = new CatalogRequest("Phone", "Smartphone", 999.99);
    }

    // Test getById() - success
    @Test
    void testGetById_Success() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog));

        CatalogResponse response = catalogService.getById(1L);

        assertNotNull(response);
        assertEquals("Phone", response.name());
        verify(catalogRepository).findById(1L);
    }

    // Test getById() - not found
    @Test
    void testGetById_NotFound() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CatalogNotFoundException.class, () -> catalogService.getById(1L));
    }

    // Test getAll()
    @Test
    void testGetAll() {
        when(catalogRepository.findAll()).thenReturn(List.of(catalog));

        List<CatalogResponse> responses = catalogService.getAll();

        assertEquals(1, responses.size());
        assertEquals("Phone", responses.get(0).name());
    }

    // Test update() - success
    @Test
    void testUpdate_Success() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog));
        when(catalogRepository.save(any(Catalog.class))).thenReturn(catalog);

        CatalogResponse response = catalogService.update(1L, request);

        assertNotNull(response);
        assertEquals("Phone", response.name());
        verify(catalogRepository).save(any(Catalog.class));
    }

    // Test update() - not found
    @Test
    void testUpdate_NotFound() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CatalogNotFoundException.class, () -> catalogService.update(1L, request));
    }

    //  Test delete() - soft delete
    @Test
    void testDelete_SoftDelete() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog));

        catalogService.delete(1L, true);

        assertEquals("INACTIVE", catalog.getStatus());
        verify(catalogRepository).save(catalog);
        verify(catalogRepository, never()).delete(any());
    }

    // Test delete() - hard delete
    @Test
    void testDelete_HardDelete() {
        when(catalogRepository.findById(1L)).thenReturn(Optional.of(catalog));

        catalogService.delete(1L, false);

        verify(catalogRepository).delete(catalog);
    }

    // Test create() - Simulated failure rollback
    @Test
    void testCreate_ShouldThrowAndRollback() {
        doAnswer(invocation -> {
            Catalog c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        }).when(catalogRepository).save(any(Catalog.class));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> catalogService.create(request));
        assertEquals("Simulated failure: rollback demo!", thrown.getMessage());

        verify(catalogRepository).save(any(Catalog.class));
        verify(auditRepository).save(any(CatalogAudit.class));
    }
}
