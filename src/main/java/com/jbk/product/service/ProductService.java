package com.jbk.product.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jbk.product.entity.Product;

public interface ProductService {
	public boolean saveProduct(Product product);

	public List<Product> getAllProduct();

	public Product getProductById(String productId);
	
	public boolean deleteProduct(String productId);
	
	public boolean updateProduct(Product product);
	
	public List<Product>sortProduct(String sortBy);
	
	public Product getMaxPriceProduct();
	
	public double sumOfProductPrice();
	
	public int getTotalCountOfProduct();

	public Map<String,String> uploadSheet(CommonsMultipartFile file,HttpSession httpsession);

	public String exportToExcel();
	
	
}