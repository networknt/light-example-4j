package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecipientMemo  {

    private String recipientMemo;

    public RecipientMemo () {
    }

    @JsonProperty("recipientMemo")
    public String getRecipientMemo() {
        return recipientMemo;
    }

    public void setRecipientMemo(String recipientMemo) {
        this.recipientMemo = recipientMemo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RecipientMemo RecipientMemo = (RecipientMemo) o;

        return Objects.equals(recipientMemo, RecipientMemo.recipientMemo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientMemo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RecipientMemo {\n");
        sb.append("    recipientMemo: ").append(toIndentedString(recipientMemo)).append("\n");
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
