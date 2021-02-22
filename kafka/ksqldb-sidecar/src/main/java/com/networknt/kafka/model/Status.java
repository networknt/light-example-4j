package com.networknt.kafka.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status  {

    private String description;
    private String code;
    private String message;
    private Integer statusCode;

    public Status () {
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Status Status = (Status) o;

        return Objects.equals(description, Status.description) &&
               Objects.equals(code, Status.code) &&
               Objects.equals(message, Status.message) &&
               Objects.equals(statusCode, Status.statusCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, code, message, statusCode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Status {\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");        sb.append("    code: ").append(toIndentedString(code)).append("\n");        sb.append("    message: ").append(toIndentedString(message)).append("\n");        sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
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
