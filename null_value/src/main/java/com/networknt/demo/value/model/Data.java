package com.networknt.demo.value.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;





public class Data   {
  private Long id = null;

  private String name = null;

  private Double weight = null;

  private Date startDate = null;

  private Date endDate = null;

  /**
   **/
  public Data id(Long id) {
    this.id = id;
    return this;
  }


  @ApiModelProperty(example = "null", required = true, value = "")
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  public Data name(String name) {
    this.name = name;
    return this;
  }


  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public Data weight(Double weight) {
    this.weight = weight;
    return this;
  }


  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("weight")
  public Double getWeight() {
    return weight;
  }
  public void setWeight(Double weight) {
    this.weight = weight;
  }

  /**
   **/
  public Data startDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }


  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("startDate")
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public Data endDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }


  @ApiModelProperty(example = "null", value = "")
  @JsonProperty("endDate")
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Data data = (Data) o;
    return Objects.equals(id, data.id) &&
        Objects.equals(name, data.name) &&
        Objects.equals(weight, data.weight) &&
        Objects.equals(startDate, data.startDate) &&
        Objects.equals(endDate, data.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, weight, startDate, endDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Data {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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
