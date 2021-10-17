package com.rahi.moviecatelogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Amimul Ehsan
 * @Created at 10/17/21
 * @Project Movie Catelog Service
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogItem {
    private String name;
    private String desc;
    private int rating;
}
