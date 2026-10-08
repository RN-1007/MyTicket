package com.travelapp.travel_app.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.name();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        String clean = dbData.trim();
        if ("Theme Park".equalsIgnoreCase(clean) || "Theme_Park".equalsIgnoreCase(clean) || "ThemePark".equalsIgnoreCase(clean)) {
            return Category.Park;
        }
        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(clean)) {
                return c;
            }
        }
        return Category.Park;
    }
}
