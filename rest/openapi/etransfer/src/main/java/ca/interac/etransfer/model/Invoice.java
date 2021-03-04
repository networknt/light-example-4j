package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Invoice  {

    private String invoiceNumber;
    private String dueDate;

    public Invoice () {
    }

    @JsonProperty("invoiceNumber")
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @JsonProperty("dueDate")
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Invoice Invoice = (Invoice) o;

        return Objects.equals(invoiceNumber, Invoice.invoiceNumber) &&
               Objects.equals(dueDate, Invoice.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNumber, dueDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Invoice {\n");
        sb.append("    invoiceNumber: ").append(toIndentedString(invoiceNumber)).append("\n");        sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
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
