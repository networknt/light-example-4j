package com.networknt.mesh.kafka.backend.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record  {

    private String key;
    private String value;
    private Object header;
    private Integer partition;
    private java.math.BigDecimal offset;

    public Record () {
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("header")
    public Object getHeader() {
        return header;
    }

    public void setHeader(Object header) {
        this.header = header;
    }

    @JsonProperty("partition")
    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    @JsonProperty("offset")
    public java.math.BigDecimal getOffset() {
        return offset;
    }

    public void setOffset(java.math.BigDecimal offset) {
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Record Record = (Record) o;

        return Objects.equals(key, Record.key) &&
               Objects.equals(value, Record.value) &&
               Objects.equals(header, Record.header) &&
               Objects.equals(partition, Record.partition) &&
               Objects.equals(offset, Record.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, header, partition, offset);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Record {\n");
        sb.append("    key: ").append(toIndentedString(key)).append("\n");        sb.append("    value: ").append(toIndentedString(value)).append("\n");        sb.append("    header: ").append(toIndentedString(header)).append("\n");        sb.append("    partition: ").append(toIndentedString(partition)).append("\n");        sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
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
