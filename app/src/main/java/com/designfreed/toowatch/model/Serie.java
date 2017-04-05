package com.designfreed.toowatch.model;

public class Serie {

    private String name;
    private String type;
    private String description;
    private Integer year;
    private Float rating;
    private String imageUrl;

    public Serie() {
    }

    public Serie(String name, String type, String description, Integer year, Float rating, String imageUrl) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.year = year;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

