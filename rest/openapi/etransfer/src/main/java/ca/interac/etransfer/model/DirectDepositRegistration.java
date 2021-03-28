package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DirectDepositRegistration  {

    private String directDepositRegistrationId;
    private String directDepositHandle;
    
    
    public enum DirectDepositHandleTypeEnum {
        
        SMS ("sms"), 
        
        EMAIL ("email"), 
        
        UUID ("uuid"); 
        

        private final String value;

        DirectDepositHandleTypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static DirectDepositHandleTypeEnum fromValue(String text) {
            for (DirectDepositHandleTypeEnum b : DirectDepositHandleTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private DirectDepositHandleTypeEnum directDepositHandleType;

    

    public DirectDepositRegistration () {
    }

    @JsonProperty("directDepositRegistrationId")
    public String getDirectDepositRegistrationId() {
        return directDepositRegistrationId;
    }

    public void setDirectDepositRegistrationId(String directDepositRegistrationId) {
        this.directDepositRegistrationId = directDepositRegistrationId;
    }

    @JsonProperty("directDepositHandle")
    public String getDirectDepositHandle() {
        return directDepositHandle;
    }

    public void setDirectDepositHandle(String directDepositHandle) {
        this.directDepositHandle = directDepositHandle;
    }

    @JsonProperty("directDepositHandleType")
    public DirectDepositHandleTypeEnum getDirectDepositHandleType() {
        return directDepositHandleType;
    }

    public void setDirectDepositHandleType(DirectDepositHandleTypeEnum directDepositHandleType) {
        this.directDepositHandleType = directDepositHandleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DirectDepositRegistration DirectDepositRegistration = (DirectDepositRegistration) o;

        return Objects.equals(directDepositRegistrationId, DirectDepositRegistration.directDepositRegistrationId) &&
               Objects.equals(directDepositHandle, DirectDepositRegistration.directDepositHandle) &&
               Objects.equals(directDepositHandleType, DirectDepositRegistration.directDepositHandleType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directDepositRegistrationId, directDepositHandle, directDepositHandleType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DirectDepositRegistration {\n");
        sb.append("    directDepositRegistrationId: ").append(toIndentedString(directDepositRegistrationId)).append("\n");        sb.append("    directDepositHandle: ").append(toIndentedString(directDepositHandle)).append("\n");        sb.append("    directDepositHandleType: ").append(toIndentedString(directDepositHandleType)).append("\n");
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
