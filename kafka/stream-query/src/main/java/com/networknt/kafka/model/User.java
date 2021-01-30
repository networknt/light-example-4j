package com.networknt.kafka.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User  {

    private String firstName;
    private String lastName;
    private String country;
    private String userId;

    public User () {
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

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User User = (User) o;

        return Objects.equals(firstName, User.firstName) &&
               Objects.equals(lastName, User.lastName) &&
               Objects.equals(country, User.country) &&
               Objects.equals(userId, User.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, country, userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");        sb.append("    country: ").append(toIndentedString(country)).append("\n");        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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
