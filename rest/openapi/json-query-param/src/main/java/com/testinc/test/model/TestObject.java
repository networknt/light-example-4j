package com.testinc.test.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestObject {

    private String testProperty;

    public TestObject () {
    }

    @JsonProperty("testProperty")
    public String getTestProperty() {
        return testProperty;
    }

    public void setTestProperty(String testProperty) {
        this.testProperty = testProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestObject TestObject = (TestObject) o;

        return Objects.equals(testProperty, TestObject.testProperty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testProperty);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TestObject {\n");
        sb.append("    testProperty: ").append(toIndentedString(testProperty)).append("\n");
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
