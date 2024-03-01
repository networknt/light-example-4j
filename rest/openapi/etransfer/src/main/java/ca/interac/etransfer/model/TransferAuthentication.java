package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferAuthentication  {

    private String securityQuestion;
    private String securityAnswer;


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

    public TransferAuthentication () {
    }

    @JsonProperty("securityQuestion")
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @JsonProperty("securityAnswer")
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
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

        TransferAuthentication TransferAuthentication = (TransferAuthentication) o;

        return Objects.equals(securityQuestion, TransferAuthentication.securityQuestion) &&
               Objects.equals(securityAnswer, TransferAuthentication.securityAnswer) &&
               Objects.equals(hashType, TransferAuthentication.hashType) &&
               Objects.equals(hashSalt, TransferAuthentication.hashSalt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityQuestion, securityAnswer, hashType, hashSalt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransferAuthentication {\n");
        sb.append("    securityQuestion: ").append(toIndentedString(securityQuestion)).append("\n");        sb.append("    securityAnswer: ").append(toIndentedString(securityAnswer)).append("\n");        sb.append("    hashType: ").append(toIndentedString(hashType)).append("\n");        sb.append("    hashSalt: ").append(toIndentedString(hashSalt)).append("\n");
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
