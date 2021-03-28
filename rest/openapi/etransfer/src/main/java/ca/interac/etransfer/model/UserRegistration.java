package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistration  {

    private String apiRegistrationId;
    private String participantId;
    private String indirectConnectorId;
    private String participantUserId;
    private String participantApiRegistrationId;
    private String fiAccountId;
    
    
    public enum RegistrationStatusEnum {
        
        IN_REVIEW ("in-review"), 
        
        ACTIVE ("active"), 
        
        FAILED ("failed"), 
        
        INACTIVE ("inactive"), 
        
        PENDING ("pending"); 
        

        private final String value;

        RegistrationStatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static RegistrationStatusEnum fromValue(String text) {
            for (RegistrationStatusEnum b : RegistrationStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private RegistrationStatusEnum registrationStatus;

    
    private java.util.List<String> allowedFeatures;
    private String thirdPartyUserId;
    private DirectDepositRegistration directDepositRegistration;
    private CustomerDetails customerDetails;
    private String name;
    private String logoUrl;
    private String websiteUrl;

    public UserRegistration () {
    }

    @JsonProperty("apiRegistrationId")
    public String getApiRegistrationId() {
        return apiRegistrationId;
    }

    public void setApiRegistrationId(String apiRegistrationId) {
        this.apiRegistrationId = apiRegistrationId;
    }

    @JsonProperty("participantId")
    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    @JsonProperty("indirectConnectorId")
    public String getIndirectConnectorId() {
        return indirectConnectorId;
    }

    public void setIndirectConnectorId(String indirectConnectorId) {
        this.indirectConnectorId = indirectConnectorId;
    }

    @JsonProperty("participantUserId")
    public String getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(String participantUserId) {
        this.participantUserId = participantUserId;
    }

    @JsonProperty("participantApiRegistrationId")
    public String getParticipantApiRegistrationId() {
        return participantApiRegistrationId;
    }

    public void setParticipantApiRegistrationId(String participantApiRegistrationId) {
        this.participantApiRegistrationId = participantApiRegistrationId;
    }

    @JsonProperty("fiAccountId")
    public String getFiAccountId() {
        return fiAccountId;
    }

    public void setFiAccountId(String fiAccountId) {
        this.fiAccountId = fiAccountId;
    }

    @JsonProperty("registrationStatus")
    public RegistrationStatusEnum getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatusEnum registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    @JsonProperty("allowedFeatures")
    public java.util.List<String> getAllowedFeatures() {
        return allowedFeatures;
    }

    public void setAllowedFeatures(java.util.List<String> allowedFeatures) {
        this.allowedFeatures = allowedFeatures;
    }

    @JsonProperty("thirdPartyUserId")
    public String getThirdPartyUserId() {
        return thirdPartyUserId;
    }

    public void setThirdPartyUserId(String thirdPartyUserId) {
        this.thirdPartyUserId = thirdPartyUserId;
    }

    @JsonProperty("directDepositRegistration")
    public DirectDepositRegistration getDirectDepositRegistration() {
        return directDepositRegistration;
    }

    public void setDirectDepositRegistration(DirectDepositRegistration directDepositRegistration) {
        this.directDepositRegistration = directDepositRegistration;
    }

    @JsonProperty("customerDetails")
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("logoUrl")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @JsonProperty("websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRegistration UserRegistration = (UserRegistration) o;

        return Objects.equals(apiRegistrationId, UserRegistration.apiRegistrationId) &&
               Objects.equals(participantId, UserRegistration.participantId) &&
               Objects.equals(indirectConnectorId, UserRegistration.indirectConnectorId) &&
               Objects.equals(participantUserId, UserRegistration.participantUserId) &&
               Objects.equals(participantApiRegistrationId, UserRegistration.participantApiRegistrationId) &&
               Objects.equals(fiAccountId, UserRegistration.fiAccountId) &&
               Objects.equals(registrationStatus, UserRegistration.registrationStatus) &&
               Objects.equals(allowedFeatures, UserRegistration.allowedFeatures) &&
               Objects.equals(thirdPartyUserId, UserRegistration.thirdPartyUserId) &&
               Objects.equals(directDepositRegistration, UserRegistration.directDepositRegistration) &&
               Objects.equals(customerDetails, UserRegistration.customerDetails) &&
               Objects.equals(name, UserRegistration.name) &&
               Objects.equals(logoUrl, UserRegistration.logoUrl) &&
               Objects.equals(websiteUrl, UserRegistration.websiteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiRegistrationId, participantId, indirectConnectorId, participantUserId, participantApiRegistrationId, fiAccountId, registrationStatus, allowedFeatures, thirdPartyUserId, directDepositRegistration, customerDetails, name, logoUrl, websiteUrl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserRegistration {\n");
        sb.append("    apiRegistrationId: ").append(toIndentedString(apiRegistrationId)).append("\n");        sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");        sb.append("    indirectConnectorId: ").append(toIndentedString(indirectConnectorId)).append("\n");        sb.append("    participantUserId: ").append(toIndentedString(participantUserId)).append("\n");        sb.append("    participantApiRegistrationId: ").append(toIndentedString(participantApiRegistrationId)).append("\n");        sb.append("    fiAccountId: ").append(toIndentedString(fiAccountId)).append("\n");        sb.append("    registrationStatus: ").append(toIndentedString(registrationStatus)).append("\n");        sb.append("    allowedFeatures: ").append(toIndentedString(allowedFeatures)).append("\n");        sb.append("    thirdPartyUserId: ").append(toIndentedString(thirdPartyUserId)).append("\n");        sb.append("    directDepositRegistration: ").append(toIndentedString(directDepositRegistration)).append("\n");        sb.append("    customerDetails: ").append(toIndentedString(customerDetails)).append("\n");        sb.append("    name: ").append(toIndentedString(name)).append("\n");        sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");        sb.append("    websiteUrl: ").append(toIndentedString(websiteUrl)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
