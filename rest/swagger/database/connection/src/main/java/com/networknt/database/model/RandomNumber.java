
package com.networknt.database.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RandomNumber {

    
    private Integer randomNumber;
    
    private Integer id;
    

    public RandomNumber () {
    }

    
    
    @JsonProperty("randomNumber")
    public Integer getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Integer randomNumber) {
        this.randomNumber = randomNumber;
    }
    
    
    
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RandomNumber RandomNumber = (RandomNumber) o;

        return Objects.equals(randomNumber, RandomNumber.randomNumber) &&
        
        Objects.equals(id, RandomNumber.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(randomNumber,  id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RandomNumber {\n");
        
        sb.append("    randomNumber: ").append(toIndentedString(randomNumber)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
