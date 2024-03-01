package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact  {

    private String contactId;
    private String contactName;
    private String contactHash;


    public enum LanguageEnum {

        EN ("en"),

        FR ("fr");


        private final String value;

        LanguageEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static LanguageEnum fromValue(String text) {
            for (LanguageEnum b : LanguageEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private LanguageEnum language;


    private java.util.List<NotificationPreference> notificationPreferences;
    private Object hardContactUser;
    private DirectDepositRegistration directDepositRegistration;
    private CustomerDetails customerDetails;
    private Boolean onUs;

    public Contact () {
    }

    @JsonProperty("contactId")
    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @JsonProperty("contactName")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @JsonProperty("contactHash")
    public String getContactHash() {
        return contactHash;
    }

    public void setContactHash(String contactHash) {
        this.contactHash = contactHash;
    }

    @JsonProperty("language")
    public LanguageEnum getLanguage() {
        return language;
    }

    public void setLanguage(LanguageEnum language) {
        this.language = language;
    }

    @JsonProperty("notificationPreferences")
    public java.util.List<NotificationPreference> getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(java.util.List<NotificationPreference> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    @JsonProperty("hardContactUser")
    public Object getHardContactUser() {
        return hardContactUser;
    }

    public void setHardContactUser(Object hardContactUser) {
        this.hardContactUser = hardContactUser;
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

    @JsonProperty("onUs")
    public Boolean getOnUs() {
        return onUs;
    }

    public void setOnUs(Boolean onUs) {
        this.onUs = onUs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Contact Contact = (Contact) o;

        return Objects.equals(contactId, Contact.contactId) &&
               Objects.equals(contactName, Contact.contactName) &&
               Objects.equals(contactHash, Contact.contactHash) &&
               Objects.equals(language, Contact.language) &&
               Objects.equals(notificationPreferences, Contact.notificationPreferences) &&
               Objects.equals(hardContactUser, Contact.hardContactUser) &&
               Objects.equals(directDepositRegistration, Contact.directDepositRegistration) &&
               Objects.equals(customerDetails, Contact.customerDetails) &&
               Objects.equals(onUs, Contact.onUs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, contactName, contactHash, language, notificationPreferences, hardContactUser, directDepositRegistration, customerDetails, onUs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Contact {\n");
        sb.append("    contactId: ").append(toIndentedString(contactId)).append("\n");        sb.append("    contactName: ").append(toIndentedString(contactName)).append("\n");        sb.append("    contactHash: ").append(toIndentedString(contactHash)).append("\n");        sb.append("    language: ").append(toIndentedString(language)).append("\n");        sb.append("    notificationPreferences: ").append(toIndentedString(notificationPreferences)).append("\n");        sb.append("    hardContactUser: ").append(toIndentedString(hardContactUser)).append("\n");        sb.append("    directDepositRegistration: ").append(toIndentedString(directDepositRegistration)).append("\n");        sb.append("    customerDetails: ").append(toIndentedString(customerDetails)).append("\n");        sb.append("    onUs: ").append(toIndentedString(onUs)).append("\n");
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
