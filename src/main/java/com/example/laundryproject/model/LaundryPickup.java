package com.example.laundryproject.model;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import jakarta.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pickups")
public class LaundryPickup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String store;
    private String serviceType;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = ClothingItemsConverter.class)
    private Map<String, Map<String, Integer>> clothingItems;

    private String pickupAddress;
    private LocalDate pickupDate;
    private String pickupTime;

    private String deliveryAddress;
    private LocalDate deliveryDate;
    private String deliveryTime;

    private Double totalPrice;

    // Order ID that can be generated and shown to the customer
    private String orderId;

    private String status = "placed";
}

@Component
class ClothingItemsConverter implements AttributeConverter<Map<String, Map<String, Integer>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Map<String, Integer>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null;
            }

            return objectMapper.readValue(dbData,
                    new TypeReference<Map<String, Map<String, Integer>>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}