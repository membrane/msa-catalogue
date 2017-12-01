package de.predi8.workshop.catalogue.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Article {
	String uuid;
	String name;
	BigDecimal price;
}