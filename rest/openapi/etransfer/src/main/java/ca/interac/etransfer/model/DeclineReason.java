package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeclineReason  {

    private String declineReason;

    public DeclineReason () {
    }

    @JsonProperty("declineReason")
    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeclineReason DeclineReason = (DeclineReason) o;

        return Objects.equals(declineReason, DeclineReason.declineReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declineReason);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeclineReason {\n");
        sb.append("    declineReason: ").append(toIndentedString(declineReason)).append("\n");
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
