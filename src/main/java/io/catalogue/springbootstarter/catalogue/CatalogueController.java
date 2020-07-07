package io.catalogue.springbootstarter.catalogue;


import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.catalogue.springbootstarter.CatalogueAPIPaths;
import io.catalogue.springbootstarter.model.CatalogueItem;
import io.catalogue.springbootstarter.model.ResourceIdentity;
import io.catalogue.springbootstarter.model.SearchFilter;
import io.catalogue.springbootstarter.service.CatalogueService;



@RestController
@RequestMapping(CatalogueAPIPaths.BASE_PATH)
public class CatalogueController {


    @Autowired
    private CatalogueService catalogueCrudService;

  /*  @GetMapping(CatalogueAPIPaths.GET_ITEMS)
    @ResponseStatus(value = HttpStatus.OK)
    public List<CatalogueItem> getCatalogueItems() {
        return catalogueCrudService.getCatalogueItems();
    }*/

    @GetMapping(CatalogueAPIPaths.GET_ITEM)
    public CatalogueItem getCatalogueItemBySKU(@PathVariable(value = "sku") String skuNumber) throws  ExecutionException {
        return catalogueCrudService.getCatalogueItem(skuNumber);
    }

    @PostMapping(CatalogueAPIPaths.CREATE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity addCatalogueItem(@Valid @RequestBody CatalogueItem catalogueItem) {
        Long id = catalogueCrudService.addCatalogItem(catalogueItem);
        return new ResponseEntity<>("Created SKU "+ id, HttpStatus.CREATED) ;
    }

    @PutMapping(CatalogueAPIPaths.UPDATE)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateCatalogueItem(
            @PathVariable(value = "sku") String skuNumber,
            @Valid @RequestBody CatalogueItem catalogueItem) throws ExecutionException {

        catalogueCrudService.updateCatalogueItem(catalogueItem);
    }

    @DeleteMapping(CatalogueAPIPaths.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCatalogItem(@PathVariable(value = "sku") String skuNumber)
            throws ExecutionException {

        catalogueCrudService.deleteCatalogueItem(catalogueCrudService.getCatalogueItem(skuNumber));
    }

    @PostMapping(CatalogueAPIPaths.SEARCH)
    @ResponseStatus(value = HttpStatus.OK)
    public List<CatalogueItem> addCatalogueItem(@Valid @RequestBody SearchFilter searchQuery) {
         return catalogueCrudService.getSearchCatalogueItems(searchQuery);
    }
}
