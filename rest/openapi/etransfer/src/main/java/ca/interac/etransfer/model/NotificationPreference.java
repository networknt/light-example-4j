package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationPreference  {

    private String handle;
    
    
    public enum HandleTypeEnum {
        
        SMS ("sms"), 
        
        EMAIL ("email"); 
        

        private final String value;

        HandleTypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static HandleTypeEnum fromValue(String text) {
            for (HandleTypeEnum b : HandleTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private HandleTypeEnum handleType;

    
    private Boolean active;

    public NotificationPreference () {
    }

    @JsonProperty("handle")
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @JsonProperty("handleType")
    public HandleTypeEnum getHandleType() {
        return handleType;
    }

    public void setHandleType(HandleTypeEnum handleType) {
        this.handleType = handleType;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationPreference NotificationPreference = (NotificationPreference) o;

        return Objects.equals(handle, NotificationPreference.handle) &&
               Objects.equals(handleType, NotificationPreference.handleType) &&
               Objects.equals(active, NotificationPreference.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle, handleType, active);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NotificationPreference {\n");
        sb.append("    handle: ").append(toIndentedString(handle)).append("\n");        sb.append("    handleType: ").append(toIndentedString(handleType)).append("\n");        sb.append("    active: ").append(toIndentedString(active)).append("\n");
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
