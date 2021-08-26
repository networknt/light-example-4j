package com.networknt.backend.binary.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status  {

    private Integer statusCode;
    private String code;
    private String message;
    private String description;

    public Status () {
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
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

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        return Objects.equals(statusCode, Status.statusCode) &&
               Objects.equals(code, Status.code) &&
               Objects.equals(message, Status.message) &&
               Objects.equals(description, Status.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, code, message, description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Status {\n");
        sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");        sb.append("    code: ").append(toIndentedString(code)).append("\n");        sb.append("    message: ").append(toIndentedString(message)).append("\n");        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
