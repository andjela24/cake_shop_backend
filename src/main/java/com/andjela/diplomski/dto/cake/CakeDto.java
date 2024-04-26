package com.andjela.diplomski.dto.cake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeDto {

    private String title;
    private int pricePerKilo;
    private int decorationPrice;
    private int minWeight;
    private int maxWeight;
    private int minTier;
    private int maxTier;
    private String imageUrl;
}
