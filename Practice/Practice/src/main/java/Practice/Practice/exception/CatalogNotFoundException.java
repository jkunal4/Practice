package Practice.Practice.exception;

public class CatalogNotFoundException extends RuntimeException{
    public CatalogNotFoundException(Long id){
        super("Catalog with id " + id + " Not found");
    }
}
