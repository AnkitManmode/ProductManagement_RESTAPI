package com.jbk.product.comparator;

import java.util.Comparator;

import com.jbk.product.entity.Product;

public class ProductIdComparator implements Comparator<Product> {

	@Override
	public int compare(Product p1, Product p2) {
		
		return p1.getProductId().compareTo(p2.getProductId());
	}// productId is string thats why we use compareTO method

}
