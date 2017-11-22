
package com.networknt.petstore;

import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import com.networknt.info.ServerInfoGetHandler;
import com.networknt.health.HealthGetHandler;
import com.networknt.petstore.handler.*;

public class PathHandlerProvider implements HandlerProvider {
    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
        
            .add(Methods.POST, "/v2/pet", new PetPostHandler())
        
            .add(Methods.PUT, "/v2/pet", new PetPutHandler())
        
            .add(Methods.DELETE, "/v2/user/{username}", new UserUsernameDeleteHandler())
        
            .add(Methods.PUT, "/v2/user/{username}", new UserUsernamePutHandler())
        
            .add(Methods.GET, "/v2/user/{username}", new UserUsernameGetHandler())
        
            .add(Methods.GET, "/v2/pet/findByStatus", new PetFindByStatusGetHandler())
        
            .add(Methods.GET, "/v2/health", new HealthGetHandler())
        
            .add(Methods.POST, "/v2/user/createWithList", new UserCreateWithListPostHandler())
        
            .add(Methods.POST, "/v2/pet/{petId}/uploadImage", new PetPetIdUploadImagePostHandler())
        
            .add(Methods.GET, "/v2/store/inventory", new StoreInventoryGetHandler())
        
            .add(Methods.GET, "/v2/user/login", new UserLoginGetHandler())
        
            .add(Methods.POST, "/v2/user", new UserPostHandler())
        
            .add(Methods.GET, "/v2/server/info", new ServerInfoGetHandler())
        
            .add(Methods.POST, "/v2/user/createWithArray", new UserCreateWithArrayPostHandler())
        
            .add(Methods.GET, "/v2/pet/findByTags", new PetFindByTagsGetHandler())
        
            .add(Methods.POST, "/v2/store/order", new StoreOrderPostHandler())
        
            .add(Methods.GET, "/v2/user/logout", new UserLogoutGetHandler())
        
            .add(Methods.POST, "/v2/pet/{petId}", new PetPetIdPostHandler())
        
            .add(Methods.DELETE, "/v2/pet/{petId}", new PetPetIdDeleteHandler())
        
            .add(Methods.GET, "/v2/pet/{petId}", new PetPetIdGetHandler())
        
            .add(Methods.DELETE, "/v2/store/order/{orderId}", new StoreOrderOrderIdDeleteHandler())
        
            .add(Methods.GET, "/v2/store/order/{orderId}", new StoreOrderOrderIdGetHandler())
        
        ;
    }
}
