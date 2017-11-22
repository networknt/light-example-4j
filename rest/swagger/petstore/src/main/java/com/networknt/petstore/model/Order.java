
package com.networknt.petstore.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

    
    private Integer id;
    
    private Integer petId;
    
    private Integer quantity;
    
    private java.time.LocalDateTime shipDate;
    
    private Boolean complete;
    
    
    
    public enum StatusEnum {
        
        PLACED ("placed"),
        
        APPROVED ("approved"),
        
        DELIVERED ("delivered");
        

        private final String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                return b;
                }
            }
            return null;
        }
    }

    private StatusEnum status;

    
    

    public Order () {
    }

    
    
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    @JsonProperty("petId")
    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }
    
    
    
    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    
    
    @JsonProperty("shipDate")
    public java.time.LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(java.time.LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }
    
    
    
    @JsonProperty("complete")
    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
    
    
    
    @JsonProperty("status")
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
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
        Order Order = (Order) o;

        return Objects.equals(id, Order.id) &&
        Objects.equals(petId, Order.petId) &&
        Objects.equals(quantity, Order.quantity) &&
        Objects.equals(shipDate, Order.shipDate) &&
        Objects.equals(complete, Order.complete) &&
        
        Objects.equals(status, Order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, petId, quantity, shipDate, complete,  status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Order {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    petId: ").append(toIndentedString(petId)).append("\n");
        sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
        sb.append("    shipDate: ").append(toIndentedString(shipDate)).append("\n");
        sb.append("    complete: ").append(toIndentedString(complete)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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
