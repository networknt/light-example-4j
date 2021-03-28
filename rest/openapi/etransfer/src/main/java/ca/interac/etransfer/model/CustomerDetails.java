package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerDetails  {

    
    
    public enum CustomerTypeEnum {
        
        RETAIL ("retail"), 
        
        SMALL_BUSINESS ("small_business"), 
        
        CORPORATE ("corporate"); 
        

        private final String value;

        CustomerTypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static CustomerTypeEnum fromValue(String text) {
            for (CustomerTypeEnum b : CustomerTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private CustomerTypeEnum customerType;

    
    private String registrationName;
    private Object legalName;

    public CustomerDetails () {
    }

    @JsonProperty("customerType")
    public CustomerTypeEnum getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerTypeEnum customerType) {
        this.customerType = customerType;
    }

    @JsonProperty("registrationName")
    public String getRegistrationName() {
        return registrationName;
    }

    public void setRegistrationName(String registrationName) {
        this.registrationName = registrationName;
    }

    @JsonProperty("legalName")
    public Object getLegalName() {
        return legalName;
    }

    public void setLegalName(Object legalName) {
        this.legalName = legalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDetails CustomerDetails = (CustomerDetails) o;

        return Objects.equals(customerType, CustomerDetails.customerType) &&
               Objects.equals(registrationName, CustomerDetails.registrationName) &&
               Objects.equals(legalName, CustomerDetails.legalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerType, registrationName, legalName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CustomerDetails {\n");
        sb.append("    customerType: ").append(toIndentedString(customerType)).append("\n");        sb.append("    registrationName: ").append(toIndentedString(registrationName)).append("\n");        sb.append("    legalName: ").append(toIndentedString(legalName)).append("\n");
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
