package com.networknt.kafka.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryUserCount  {

    private Integer count;
    private String country;

    public CountryUserCount () {
    }

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryUserCount CountryUserCount = (CountryUserCount) o;

        return Objects.equals(count, CountryUserCount.count) &&
               Objects.equals(country, CountryUserCount.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, country);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CountryUserCount {\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");        sb.append("    country: ").append(toIndentedString(country)).append("\n");
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
