package de.predi8.workshop.catalogue.dto;

import java.math.BigDecimal;

public class Article {
	private final String uuid;
	private final String name;
	private final BigDecimal price;

	public Article(String uuid, String name, BigDecimal price) {
		this.uuid = uuid;
		this.name = name;
		this.price = price;
	}

	public String getUuid() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String toString() {
		return "Article(uuid=" + uuid + ", name=" + name + ", price=" + price + ")";
	}
}