package com.networknt.soap;

import com.networknt.rule.IAction;
import com.networknt.rule.RuleActionValue;
import com.networknt.rule.RuleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Transform a soap request to call the external service with a security section with username, password, nonce and
 * timestamp. The original request body, username and password will be passed from the yaml rule instead of hardcoded
 * in the action class here.
 *
 * We don't want to introduce some third party XML library to manipulate the request body to inject the security section,
 * so only regex replacement is used. It depends on the original request body has a string <soap:Header/> to be matched.
 *
 * Please be aware that we are dealing with XML content here instead of JSON.
 *
 * @author Steve Hu
 */
public class SoapSecurityTransformAction implements IAction {
    private static final Logger logger = LoggerFactory.getLogger(SoapSecurityTransformAction.class);

    @Override
    public void performAction(Map<String, Object> objMap, Map<String, Object> resultMap, Collection<RuleActionValue> actionValues) {
        // get the body from the objMap and create a new body in the resultMap. Both in string format.
        resultMap.put(RuleConstants.RESULT, true);
        String requestBody = (String)objMap.get("requestBody");
        String username = null;
        String password = null;
        for(RuleActionValue value: actionValues) {
            if("username".equals(value.getActionValueId())) {
                username = value.getValue();
                continue;
            }
            if("password".equals(value.getActionValueId())) {
                password = value.getValue();
                continue;
            }
        }
        if(logger.isTraceEnabled()) logger.debug("original request body = " + requestBody);
        String modifiedBody = transform(requestBody, username, password);
        if(logger.isTraceEnabled()) logger.trace("transformed request body = " + modifiedBody);
        resultMap.put("requestBody", modifiedBody);
    }

    private String transform(String requestBody, String username, String password) {
        // the source input is a string of XML, we will replace the header with security with regex.
        String output = requestBody.replaceAll("<soap:Header/>", generateSecurity(username, password));
        return output;
    }

    private String generateSecurity(String username, String password) {
        String nonce = generateNonce();
        if(logger.isTraceEnabled()) logger.trace("Nonce = " + nonce);
        System.out.println("Nonce = " + nonce);

        // created date/time in UTC format
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String created = dateFormatter.format(today);
        if(logger.isTraceEnabled()) logger.trace("Created = " + created);

        // generate the password digest from the nonce + created + fixed password
        String passwordDigest = createPasswordDigest(nonce, created, password);
        if(logger.isTraceEnabled()) logger.trace("Password Digest = " + passwordDigest);

        // SOAP header security section.
        String security =
                  "   <soap:Header>\n"
                + "   <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
                + "               xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n"
                + "               soap:mustUnderstand=\"1\">\n"
                + "      <wsse:UsernameToken wsu:Id=\"UsernameToken-eab4be46-8374-4492-81c9-502798f4b123\">\n"
                + "         <wsse:Username>%Username%</wsse:Username>\n"
                + "         <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">%PasswordDigest%</wsse:Password>\n"
                + "         <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">%Nonce%</wsse:Nonce>\n"
                + "         <wsu:Created>%Created%</wsu:Created>\n"
                + "      </wsse:UsernameToken>\n"
                + "   </wsse:Security>\n"
                + "   </soap:Header>\\n\";"  ;

        security = security.replaceAll("%Username%", username);
        security = security.replaceAll("%PasswordDigest%", passwordDigest);
        security = security.replaceAll("%Nonce%", nonce);
        security = security.replaceAll("%Created%", created);
        return security;
    }

    private String createPasswordDigest(String nonce, String created, String password) {
        MessageDigest sha1;
        String passwordDigest = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(Base64.getDecoder().decode(nonce));
            sha1.update(created.getBytes("UTF-8"));
            passwordDigest = new String(Base64.getEncoder().encode(sha1.digest(password.getBytes("UTF-8"))));
            sha1.reset();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("Exception: ", e);
        }
        return passwordDigest;
    }

    private String generateNonce() {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.getEncoder().encodeToString(nonceByte);
    }

}
