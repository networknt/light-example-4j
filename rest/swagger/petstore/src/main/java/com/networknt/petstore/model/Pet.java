
package com.networknt.petstore.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pet {


    private String name;

    private java.lang.Long id;

    private java.util.List<String> photoUrls;

    private Category category;

    private java.util.List<Tag> tags;



    public enum StatusEnum {

        AVAILABLE ("available"),

        PENDING ("pending"),

        SOLD ("sold");


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




    public Pet () {
    }



    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @JsonProperty("id")
    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }



    @JsonProperty("photoUrls")
    public java.util.List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(java.util.List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }



    @JsonProperty("category")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    @JsonProperty("tags")
    public java.util.List<Tag> getTags() {
        return tags;
    }

    public void setTags(java.util.List<Tag> tags) {
        this.tags = tags;
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
        Pet Pet = (Pet) o;

        return Objects.equals(name, Pet.name) &&
        Objects.equals(id, Pet.id) &&
        Objects.equals(photoUrls, Pet.photoUrls) &&
        Objects.equals(category, Pet.category) &&
        Objects.equals(tags, Pet.tags) &&

        Objects.equals(status, Pet.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, photoUrls, category, tags,  status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Pet {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    photoUrls: ").append(toIndentedString(photoUrls)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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
