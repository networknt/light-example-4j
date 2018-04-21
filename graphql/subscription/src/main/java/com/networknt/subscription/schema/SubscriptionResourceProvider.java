package com.networknt.subscription.schema;

import com.networknt.resources.PathResourceProvider;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;

import java.io.File;

/**
 * @author Nicholas Azar
 * Created on April 18, 2018
 */
public class SubscriptionResourceProvider implements PathResourceProvider {

    @Override
    public String getPath() {
        return "/static";
    }

    @Override
    public Boolean isPrefixPath() {
        return true;
    }

    @Override
    public ResourceManager getResourceManager() {
        return new PathResourceManager(new File(SubscriptionResourceProvider.class.getResource("/static").getFile()).toPath());
    }
}
