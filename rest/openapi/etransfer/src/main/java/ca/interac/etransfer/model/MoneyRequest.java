package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyRequest  {

    private String referenceNumber;
    private String sourceMoneyRequestId;
    private Object requestedFrom;
    private java.lang.Double amount;
    private String currency;
    private Boolean editableFulfillAmount;
    private String requesterMessage;
    private Invoice invoice;
    private String expiryDate;
    private Boolean supressResponderNotifications;
    private String returnURL;
    private String creationDate;
    private Integer status;
    private java.lang.Double fulfillAmount;
    private String responderMessage;
    private Integer notificationStatus;

    public MoneyRequest () {
    }

    @JsonProperty("referenceNumber")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @JsonProperty("sourceMoneyRequestId")
    public String getSourceMoneyRequestId() {
        return sourceMoneyRequestId;
    }

    public void setSourceMoneyRequestId(String sourceMoneyRequestId) {
        this.sourceMoneyRequestId = sourceMoneyRequestId;
    }

    @JsonProperty("requestedFrom")
    public Object getRequestedFrom() {
        return requestedFrom;
    }

    public void setRequestedFrom(Object requestedFrom) {
        this.requestedFrom = requestedFrom;
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

    @JsonProperty("editableFulfillAmount")
    public Boolean getEditableFulfillAmount() {
        return editableFulfillAmount;
    }

    public void setEditableFulfillAmount(Boolean editableFulfillAmount) {
        this.editableFulfillAmount = editableFulfillAmount;
    }

    @JsonProperty("requesterMessage")
    public String getRequesterMessage() {
        return requesterMessage;
    }

    public void setRequesterMessage(String requesterMessage) {
        this.requesterMessage = requesterMessage;
    }

    @JsonProperty("invoice")
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @JsonProperty("expiryDate")
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @JsonProperty("supressResponderNotifications")
    public Boolean getSupressResponderNotifications() {
        return supressResponderNotifications;
    }

    public void setSupressResponderNotifications(Boolean supressResponderNotifications) {
        this.supressResponderNotifications = supressResponderNotifications;
    }

    @JsonProperty("returnURL")
    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    @JsonProperty("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("fulfillAmount")
    public java.lang.Double getFulfillAmount() {
        return fulfillAmount;
    }

    public void setFulfillAmount(java.lang.Double fulfillAmount) {
        this.fulfillAmount = fulfillAmount;
    }

    @JsonProperty("responderMessage")
    public String getResponderMessage() {
        return responderMessage;
    }

    public void setResponderMessage(String responderMessage) {
        this.responderMessage = responderMessage;
    }

    @JsonProperty("notificationStatus")
    public Integer getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Integer notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MoneyRequest MoneyRequest = (MoneyRequest) o;

        return Objects.equals(referenceNumber, MoneyRequest.referenceNumber) &&
               Objects.equals(sourceMoneyRequestId, MoneyRequest.sourceMoneyRequestId) &&
               Objects.equals(requestedFrom, MoneyRequest.requestedFrom) &&
               Objects.equals(amount, MoneyRequest.amount) &&
               Objects.equals(currency, MoneyRequest.currency) &&
               Objects.equals(editableFulfillAmount, MoneyRequest.editableFulfillAmount) &&
               Objects.equals(requesterMessage, MoneyRequest.requesterMessage) &&
               Objects.equals(invoice, MoneyRequest.invoice) &&
               Objects.equals(expiryDate, MoneyRequest.expiryDate) &&
               Objects.equals(supressResponderNotifications, MoneyRequest.supressResponderNotifications) &&
               Objects.equals(returnURL, MoneyRequest.returnURL) &&
               Objects.equals(creationDate, MoneyRequest.creationDate) &&
               Objects.equals(status, MoneyRequest.status) &&
               Objects.equals(fulfillAmount, MoneyRequest.fulfillAmount) &&
               Objects.equals(responderMessage, MoneyRequest.responderMessage) &&
               Objects.equals(notificationStatus, MoneyRequest.notificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceNumber, sourceMoneyRequestId, requestedFrom, amount, currency, editableFulfillAmount, requesterMessage, invoice, expiryDate, supressResponderNotifications, returnURL, creationDate, status, fulfillAmount, responderMessage, notificationStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MoneyRequest {\n");
        sb.append("    referenceNumber: ").append(toIndentedString(referenceNumber)).append("\n");        sb.append("    sourceMoneyRequestId: ").append(toIndentedString(sourceMoneyRequestId)).append("\n");        sb.append("    requestedFrom: ").append(toIndentedString(requestedFrom)).append("\n");        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");        sb.append("    editableFulfillAmount: ").append(toIndentedString(editableFulfillAmount)).append("\n");        sb.append("    requesterMessage: ").append(toIndentedString(requesterMessage)).append("\n");        sb.append("    invoice: ").append(toIndentedString(invoice)).append("\n");        sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");        sb.append("    supressResponderNotifications: ").append(toIndentedString(supressResponderNotifications)).append("\n");        sb.append("    returnURL: ").append(toIndentedString(returnURL)).append("\n");        sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");        sb.append("    status: ").append(toIndentedString(status)).append("\n");        sb.append("    fulfillAmount: ").append(toIndentedString(fulfillAmount)).append("\n");        sb.append("    responderMessage: ").append(toIndentedString(responderMessage)).append("\n");        sb.append("    notificationStatus: ").append(toIndentedString(notificationStatus)).append("\n");
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
