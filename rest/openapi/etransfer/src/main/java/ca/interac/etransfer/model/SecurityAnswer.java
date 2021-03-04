package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecurityAnswer  {

    private String securityAnswer;

    public SecurityAnswer () {
    }

    @JsonProperty("securityAnswer")
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SecurityAnswer SecurityAnswer = (SecurityAnswer) o;

        return Objects.equals(securityAnswer, SecurityAnswer.securityAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityAnswer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SecurityAnswer {\n");
        sb.append("    securityAnswer: ").append(toIndentedString(securityAnswer)).append("\n");
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
