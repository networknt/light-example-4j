package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingTransfer  {

    private String referenceNumber;
    
    
    public enum TransferTypeEnum {
        
        REGULAR_TRANSFER ("REGULAR_TRANSFER"), 
        
        MONEY_REQUEST_FULFILLMENT ("MONEY_REQUEST_FULFILLMENT"), 
        
        DIRECT_DEPOSIT ("DIRECT_DEPOSIT"); 
        

        private final String value;

        TransferTypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TransferTypeEnum fromValue(String text) {
            for (TransferTypeEnum b : TransferTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private TransferTypeEnum transferType;

    
    private java.lang.Double amount;
    private String currency;
    private String senderMessage;
    private String expiryDate;
    private String senderRegistrationName;
    
    
    public enum RecipientLanguageEnum {
        
        EN ("en"), 
        
        FR ("fr"); 
        

        private final String value;

        RecipientLanguageEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static RecipientLanguageEnum fromValue(String text) {
            for (RecipientLanguageEnum b : RecipientLanguageEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private RecipientLanguageEnum recipientLanguage;

    
    private Integer status;
    private Boolean authenticationRequired;
    private String securityQuestion;
    
    
    public enum HashTypeEnum {
        
        SHA2 ("SHA2"), 
        
        MD5 ("MD5"); 
        

        private final String value;

        HashTypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static HashTypeEnum fromValue(String text) {
            for (HashTypeEnum b : HashTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private HashTypeEnum hashType;

    
    private String hashSalt;

    public IncomingTransfer () {
    }

    @JsonProperty("referenceNumber")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @JsonProperty("transferType")
    public TransferTypeEnum getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferTypeEnum transferType) {
        this.transferType = transferType;
    }

    @JsonProperty("amount")
    public java.lang.Double getAmount() {
        return amount;
    }

    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("senderMessage")
    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    @JsonProperty("expiryDate")
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @JsonProperty("senderRegistrationName")
    public String getSenderRegistrationName() {
        return senderRegistrationName;
    }

    public void setSenderRegistrationName(String senderRegistrationName) {
        this.senderRegistrationName = senderRegistrationName;
    }

    @JsonProperty("recipientLanguage")
    public RecipientLanguageEnum getRecipientLanguage() {
        return recipientLanguage;
    }

    public void setRecipientLanguage(RecipientLanguageEnum recipientLanguage) {
        this.recipientLanguage = recipientLanguage;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("authenticationRequired")
    public Boolean getAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(Boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    @JsonProperty("securityQuestion")
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @JsonProperty("hashType")
    public HashTypeEnum getHashType() {
        return hashType;
    }

    public void setHashType(HashTypeEnum hashType) {
        this.hashType = hashType;
    }

    @JsonProperty("hashSalt")
    public String getHashSalt() {
        return hashSalt;
    }

    public void setHashSalt(String hashSalt) {
        this.hashSalt = hashSalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingTransfer IncomingTransfer = (IncomingTransfer) o;

        return Objects.equals(referenceNumber, IncomingTransfer.referenceNumber) &&
               Objects.equals(transferType, IncomingTransfer.transferType) &&
               Objects.equals(amount, IncomingTransfer.amount) &&
               Objects.equals(currency, IncomingTransfer.currency) &&
               Objects.equals(senderMessage, IncomingTransfer.senderMessage) &&
               Objects.equals(expiryDate, IncomingTransfer.expiryDate) &&
               Objects.equals(senderRegistrationName, IncomingTransfer.senderRegistrationName) &&
               Objects.equals(recipientLanguage, IncomingTransfer.recipientLanguage) &&
               Objects.equals(status, IncomingTransfer.status) &&
               Objects.equals(authenticationRequired, IncomingTransfer.authenticationRequired) &&
               Objects.equals(securityQuestion, IncomingTransfer.securityQuestion) &&
               Objects.equals(hashType, IncomingTransfer.hashType) &&
               Objects.equals(hashSalt, IncomingTransfer.hashSalt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceNumber, transferType, amount, currency, senderMessage, expiryDate, senderRegistrationName, recipientLanguage, status, authenticationRequired, securityQuestion, hashType, hashSalt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IncomingTransfer {\n");
        sb.append("    referenceNumber: ").append(toIndentedString(referenceNumber)).append("\n");        sb.append("    transferType: ").append(toIndentedString(transferType)).append("\n");        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");        sb.append("    senderMessage: ").append(toIndentedString(senderMessage)).append("\n");        sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");        sb.append("    senderRegistrationName: ").append(toIndentedString(senderRegistrationName)).append("\n");        sb.append("    recipientLanguage: ").append(toIndentedString(recipientLanguage)).append("\n");        sb.append("    status: ").append(toIndentedString(status)).append("\n");        sb.append("    authenticationRequired: ").append(toIndentedString(authenticationRequired)).append("\n");        sb.append("    securityQuestion: ").append(toIndentedString(securityQuestion)).append("\n");        sb.append("    hashType: ").append(toIndentedString(hashType)).append("\n");        sb.append("    hashSalt: ").append(toIndentedString(hashSalt)).append("\n");
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
