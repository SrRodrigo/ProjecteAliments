package org.rodriDev.projectealiments;

public class ProductInfo {
    private String productName;
    private String brand;
    private String categories;
    private String ingredients;
    private String allergens;
    private String countries;
    private String additives;
    private String labels;
    private String imageUrl;

    public ProductInfo(String productName, String brand, String categories, String ingredients,
                       String allergens, String countries, String additives, String labels, String imageUrl) {
        this.productName = productName;
        this.brand = brand;
        this.categories = categories;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.countries = countries;
        this.additives = additives;
        this.labels = labels;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategories() {
        return categories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getAllergens() {
        return allergens;
    }

    public String getCountries() {
        return countries;
    }

    public String getAdditives() {
        return additives;
    }

    public String getLabels() {
        return labels;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
