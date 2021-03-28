package ca.interac.etransfer.handler;

import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import com.networknt.http.RequestEntity;
import com.networknt.http.ResponseEntity;
import com.networknt.http.HttpService;
import ca.interac.etransfer.model.Transfer;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransfersSendPostService implements HttpService<Transfer, String> {
    private static final Logger logger = LoggerFactory.getLogger(TransfersSendPostService.class);

    @Override
    public ResponseEntity invoke(RequestEntity<Transfer> requestEntity) {
        Transfer requestBody = requestEntity.getBody();
        logger.debug(requestBody.toString());
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = "[{\"code\":1005,\"message\":\"ApiRegistartionId doesn't exist\"},{\"code\":1007,\"message\":\"contact doesn't exist\"},{\"code\":1025,\"message\":\"moneyRequestReferenceNumber doesn't exist.\"},{\"code\":1026,\"message\":\"directDepositRegistrationId doesn't exist.\"}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, responseHeaders, HttpStatus.OK);
        return responseEntity;
    }
}
