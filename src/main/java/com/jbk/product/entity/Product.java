package com.jbk.product.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;

@Entity
public class Product {
	
	@Id
	private String productId;
	
	@NotNull(message = "Product name is Required")
	private String productName;
	
	@NotNull(message = "Product Type is Required")
	private String productType;
	
	@Min(1)
	private int productQty;
	
	@Min(1)
	private double productPrice;

	public Product() {
	}

	public Product(String productId, String productName, String productType, int productQty, double productPrice) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productType = productType;
		this.productQty = productQty;
		this.productPrice = productPrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getProductQty() {
		return productQty;
	}

	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "ProductController [productId=" + productId + ", productName=" + productName + ", productType=" + productType
				+ ", productQty=" + productQty + ", productPrice=" + productPrice + "]";
	}

}
