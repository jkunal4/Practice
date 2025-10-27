package Practice.Practice.controller;

import Practice.Practice.dto.CatalogRequest;
import Practice.Practice.dto.CatalogResponse;
import Practice.Practice.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CatalogResponse> create(@RequestBody @Valid CatalogRequest request){
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }


    @GetMapping
    public ResponseEntity<List<CatalogResponse>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestParam(defaultValue = "true") boolean softDelete ){
        service.delete(id,softDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogResponse> update(@PathVariable Long id, @RequestBody CatalogRequest request){

        CatalogResponse updated = service.update(id,request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/with-audit")
    public ResponseEntity<CatalogResponse> createWithAudit(@RequestBody CatalogRequest request) {
        try {
            return ResponseEntity.ok(service.create(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
