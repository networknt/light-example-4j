package ca.interac.etransfer.handler;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import ca.interac.etransfer.model.UserRegistrationUpdate;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationsApiRegistartionIdPatchService implements HttpService<UserRegistrationUpdate, String> {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationsApiRegistartionIdPatchService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<UserRegistrationUpdate> requestEntity) {
        UserRegistrationUpdate requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "[{\"code\":1005,\"message\":\"ApiRegistartionId doesn't exist\"}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
