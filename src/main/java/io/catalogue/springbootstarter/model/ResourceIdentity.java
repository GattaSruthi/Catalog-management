package io.catalogue.springbootstarter.model;

import org.springframework.lang.NonNull;


public class ResourceIdentity {

    @NonNull private Long id;

    public ResourceIdentity(final Long id) {
        this.id=id;
    }
}
