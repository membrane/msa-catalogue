package de.predi8.catalogue.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Article {

	@Id
	private String uuid;
	private String name;
	private BigDecimal price;

	public Article() {
	}

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

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String toString() {
		return "Article(uuid=" + uuid + ", name=" + name + ", price=" + price + ")";
	}

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}