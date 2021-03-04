package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingMoneyRequest  {

    private MoneyRequest moneyRequest;
    private CustomerDetails requesterDetails;
    private java.util.List<Object> requesterNotifications;

    public IncomingMoneyRequest () {
    }

    @JsonProperty("moneyRequest")
    public MoneyRequest getMoneyRequest() {
        return moneyRequest;
    }

    public void setMoneyRequest(MoneyRequest moneyRequest) {
        this.moneyRequest = moneyRequest;
    }

    @JsonProperty("requesterDetails")
    public CustomerDetails getRequesterDetails() {
        return requesterDetails;
    }

    public void setRequesterDetails(CustomerDetails requesterDetails) {
        this.requesterDetails = requesterDetails;
    }

    @JsonProperty("requesterNotifications")
    public java.util.List<Object> getRequesterNotifications() {
        return requesterNotifications;
    }

    public void setRequesterNotifications(java.util.List<Object> requesterNotifications) {
        this.requesterNotifications = requesterNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingMoneyRequest IncomingMoneyRequest = (IncomingMoneyRequest) o;

        return Objects.equals(moneyRequest, IncomingMoneyRequest.moneyRequest) &&
               Objects.equals(requesterDetails, IncomingMoneyRequest.requesterDetails) &&
               Objects.equals(requesterNotifications, IncomingMoneyRequest.requesterNotifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneyRequest, requesterDetails, requesterNotifications);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IncomingMoneyRequest {\n");
        sb.append("    moneyRequest: ").append(toIndentedString(moneyRequest)).append("\n");        sb.append("    requesterDetails: ").append(toIndentedString(requesterDetails)).append("\n");        sb.append("    requesterNotifications: ").append(toIndentedString(requesterNotifications)).append("\n");
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
