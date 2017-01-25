package com.networknt.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;


public class RandomNumber {
  private Integer id = null;

  private Integer randomNumber = null;

  public RandomNumber(int id, int randomNumber) {
    this.id = id;
    this.randomNumber = randomNumber;
  }

  /**
   * a unique id as primary key
   **/
  public RandomNumber id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "null", required = true, value = "a unique id as primary key")
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * a random number
   **/
  public RandomNumber randomNumber(Integer randomNumber) {
    this.randomNumber = randomNumber;
    return this;
  }

  
  @ApiModelProperty(example = "null", required = true, value = "a random number")
  @JsonProperty("randomNumber")
  public Integer getRandomNumber() {
    return randomNumber;
  }
  public void setRandomNumber(Integer randomNumber) {
    this.randomNumber = randomNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RandomNumber randomNumber = (RandomNumber) o;
    return Objects.equals(id, randomNumber.id) &&
        Objects.equals(randomNumber, randomNumber.randomNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, randomNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RandomNumber {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    randomNumber: ").append(toIndentedString(randomNumber)).append("\n");
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

