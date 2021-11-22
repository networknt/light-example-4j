package com.networknt.account.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account  {

    private String num;
    private String name;
    private String type;
    private String firstName;
    private String lastName;
    private String sinNumber;
    private String status;

    public Account () {
    }

    @JsonProperty("num")
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("sinNumber")
    public String getSinNumber() {
        return sinNumber;
    }

    public void setSinNumber(String sinNumber) {
        this.sinNumber = sinNumber;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Account Account = (Account) o;

        return Objects.equals(num, Account.num) &&
               Objects.equals(name, Account.name) &&
               Objects.equals(type, Account.type) &&
               Objects.equals(firstName, Account.firstName) &&
               Objects.equals(lastName, Account.lastName) &&
               Objects.equals(sinNumber, Account.sinNumber) &&
               Objects.equals(status, Account.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, name, type, firstName, lastName, sinNumber, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Account {\n");
        sb.append("    num: ").append(toIndentedString(num)).append("\n");        sb.append("    name: ").append(toIndentedString(name)).append("\n");        sb.append("    type: ").append(toIndentedString(type)).append("\n");        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");        sb.append("    sinNumber: ").append(toIndentedString(sinNumber)).append("\n");        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
