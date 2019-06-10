package com.networknt.market.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Market {

    private String name;
    private java.util.List<Pet> pets;
    private java.util.List<Book> books;
    private java.util.List<Food> foodbox;
    private java.util.List<Computer> computers;

    public Market () {
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("pets")
    public java.util.List<Pet> getPets() {
        return pets;
    }

    public void setPets(java.util.List<Pet> pets) {
        this.pets = pets;
    }

    @JsonProperty("books")
    public java.util.List<Book> getBooks() {
        return books;
    }

    public void setBooks(java.util.List<Book> books) {
        this.books = books;
    }

    @JsonProperty("foodbox")
    public java.util.List<Food> getFoodbox() {
        return foodbox;
    }

    public void setFoodbox(java.util.List<Food> foodbox) {
        this.foodbox = foodbox;
    }

    @JsonProperty("computers")
    public java.util.List<Computer> getComputers() {
        return computers;
    }

    public void setComputers(java.util.List<Computer> computers) {
        this.computers = computers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Market Market = (Market) o;

        return Objects.equals(name, Market.name) &&
               Objects.equals(pets, Market.pets) &&
               Objects.equals(books, Market.books) &&
               Objects.equals(foodbox, Market.foodbox) &&
               Objects.equals(computers, Market.computers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pets, books, foodbox, computers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Market {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
        sb.append("    books: ").append(toIndentedString(books)).append("\n");
        sb.append("    foodbox: ").append(toIndentedString(foodbox)).append("\n");
        sb.append("    computers: ").append(toIndentedString(computers)).append("\n");
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
