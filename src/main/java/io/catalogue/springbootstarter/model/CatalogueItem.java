package io.catalogue.springbootstarter.model;

import java.util.Date;
import org.springframework.lang.NonNull;


public class CatalogueItem {
    private Long id;
    @NonNull private String sku;
    @NonNull private String name;
    @NonNull private String description;
    @NonNull private String category;
    @NonNull private Double price;
    private Date createdOn;
    private Date updatedOn;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    @NonNull public Double getPrice() {
        return price;
    }

    public void setPrice(@NonNull final Double price) {
        this.price = price;
    }

    @NonNull public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(@NonNull final Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
