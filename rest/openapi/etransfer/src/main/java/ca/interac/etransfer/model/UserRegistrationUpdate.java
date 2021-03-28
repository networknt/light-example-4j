package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationUpdate  {

    private java.util.List<String> allowedFeatures;
    private String fiAccountId;
    private DirectDepositRegistration directDepositRegistration;
    private String name;
    private String logoUrl;
    private String websiteUrl;

    public UserRegistrationUpdate () {
    }

    @JsonProperty("allowedFeatures")
    public java.util.List<String> getAllowedFeatures() {
        return allowedFeatures;
    }

    public void setAllowedFeatures(java.util.List<String> allowedFeatures) {
        this.allowedFeatures = allowedFeatures;
    }

    @JsonProperty("fiAccountId")
    public String getFiAccountId() {
        return fiAccountId;
    }

    public void setFiAccountId(String fiAccountId) {
        this.fiAccountId = fiAccountId;
    }

    @JsonProperty("directDepositRegistration")
    public DirectDepositRegistration getDirectDepositRegistration() {
        return directDepositRegistration;
    }

    public void setDirectDepositRegistration(DirectDepositRegistration directDepositRegistration) {
        this.directDepositRegistration = directDepositRegistration;
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

        UserRegistrationUpdate UserRegistrationUpdate = (UserRegistrationUpdate) o;

        return Objects.equals(allowedFeatures, UserRegistrationUpdate.allowedFeatures) &&
               Objects.equals(fiAccountId, UserRegistrationUpdate.fiAccountId) &&
               Objects.equals(directDepositRegistration, UserRegistrationUpdate.directDepositRegistration) &&
               Objects.equals(name, UserRegistrationUpdate.name) &&
               Objects.equals(logoUrl, UserRegistrationUpdate.logoUrl) &&
               Objects.equals(websiteUrl, UserRegistrationUpdate.websiteUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedFeatures, fiAccountId, directDepositRegistration, name, logoUrl, websiteUrl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserRegistrationUpdate {\n");
        sb.append("    allowedFeatures: ").append(toIndentedString(allowedFeatures)).append("\n");        sb.append("    fiAccountId: ").append(toIndentedString(fiAccountId)).append("\n");        sb.append("    directDepositRegistration: ").append(toIndentedString(directDepositRegistration)).append("\n");        sb.append("    name: ").append(toIndentedString(name)).append("\n");        sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");        sb.append("    websiteUrl: ").append(toIndentedString(websiteUrl)).append("\n");
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
