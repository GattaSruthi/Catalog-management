package io.catalogue.springbootstarter.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.catalogue.springbootstarter.model.CatalogueItem;
import io.catalogue.springbootstarter.model.SearchFilter;


@Component
@Service
public class CatalogueService {

    private Map<String, CatalogueItem> catalogueItemMap = new HashMap<>();
    private Map<String, HashSet<String>> searchIndexMap=new HashMap<>();

    private LoadingCache<String, CatalogueItem> catalogueCache =
            CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(30, TimeUnit.MINUTES).build(
                    new CacheLoader<String, CatalogueItem>() {
                        @Override public CatalogueItem load(final String sku) throws Exception {
                            return getFromInMemory(sku);
                        }
                    });

    /**
     * loads the data to the cache from map
     * @param sku
     * @return
     */
    private CatalogueItem getFromInMemory(final String sku) {
        return catalogueItemMap.get(sku);
    }



    /**
     * Get single record based on SKU
     * @param skuNumber
     * @return
     * @throws ExecutionException
     */
    public CatalogueItem getCatalogueItem(String skuNumber) throws ExecutionException {
        return getCatalogueItemBySku(skuNumber);
    }

    /**
     * add item to the in memory cache.
     * @param catalogueItem
     * @return
     */
    public Long addCatalogItem(CatalogueItem catalogueItem) {
        catalogueItem.setCreatedOn(new Date());
        catalogueItemMap.put(catalogueItem.getSku(), catalogueItem);
        // Build search index map
        HashSet<String> uniqueWords= getUniqueWords(catalogueItem.getDescription());
        buildSearchIndexMap(uniqueWords, catalogueItem.getSku());
        return catalogueItem.getId();
    }

    /**
     * Updates the particular catalogue item
     * TODO: update the buildSearchIndexMap
     * @param catalogueItem
     * @throws ExecutionException
     */
    public void updateCatalogueItem(CatalogueItem catalogueItem) throws ExecutionException {
        CatalogueItem catalogueItemfromDB = getCatalogueItemBySku(catalogueItem.getSku());
        catalogueItemfromDB.setName(catalogueItem.getName());
        catalogueItemfromDB.setDescription(catalogueItem.getDescription());
        catalogueItemfromDB.setPrice(catalogueItem.getPrice());
        catalogueItemfromDB.setUpdatedOn(new Date());
        catalogueItemMap.put(catalogueItem.getSku(), catalogueItem);
    }

    /**
     * TODO: update the buildSearchIndexMap
     * deletes the record from the guava cache
     * @param catalogueItem
     */
    public void deleteCatalogueItem(CatalogueItem catalogueItem) {
        catalogueCache.invalidate(catalogueItem.getSku());
    }

    /***
     * Search the string and get all the SKU related to that.
     * @param searchQuery
     * @return
     */
    public List<CatalogueItem> getSearchCatalogueItems(final SearchFilter searchQuery) {
        List<CatalogueItem> searchCatalogueResults = new LinkedList<>();
        HashSet<String> skuSet = searchIndexMap.get(searchQuery.getSearchQuery());
        if (skuSet != null) {
            skuSet.forEach(sku -> {
                searchCatalogueResults.add(catalogueItemMap.get(sku));
            });

            return searchCatalogueResults;
        } else {
            return null;
        }
    }

    private CatalogueItem getCatalogueItemBySku(String skuNumber) throws ExecutionException {
        CatalogueItem catalogueItem = catalogueCache.get(skuNumber);
        return catalogueItem;
    }


    private  HashSet<String> getUniqueWords(final String inputValue){
        String[] array = inputValue.split(" ");
        HashSet<String> uniqueWords= new HashSet<>();
        uniqueWords.addAll(Arrays.asList(array));
        return uniqueWords;
    }

    private void buildSearchIndexMap(final HashSet<String> uniqueWords, final String SKU){
        uniqueWords.forEach(word->{
            HashSet<String> skuSet=searchIndexMap.getOrDefault(word, new HashSet<>());
            skuSet.add(SKU);
            searchIndexMap.put(word,skuSet);
        });
    }
}