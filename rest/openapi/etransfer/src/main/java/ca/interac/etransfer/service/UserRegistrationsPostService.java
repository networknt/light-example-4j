package ca.interac.etransfer.handler;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import ca.interac.etransfer.model.UserRegistration;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegistrationsPostService implements HttpService<UserRegistration, String> {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationsPostService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<UserRegistration> requestEntity) {
        UserRegistration requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "[{\"code\":1003,\"message\":\"Customer doesn't exist - ParticipantUserId doesn't exist\"}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
