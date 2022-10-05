package com.jbk.product.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.jbk.product.entity.Product;
import com.jbk.product.exception.ProductAlreadyExist;
import com.jbk.product.exception.ProductNotFoundException;
import com.jbk.product.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService service;

	@PostMapping(value="/saveProduct")
	public ResponseEntity <Boolean>saveProduct( @Valid @RequestBody Product product){
		boolean isAdded = service.saveProduct(product);
		if(isAdded) {
			return new ResponseEntity<Boolean>(isAdded,HttpStatus.CREATED);
		}else {
			throw new ProductAlreadyExist("Product already Exist:=>"+ product.getProductId());
		}
	}
	@GetMapping(value="/getAllProduct")
	public ResponseEntity<List<Product>>getAllProduct(){
		List<Product> allProduct = service.getAllProduct();
		if(allProduct.isEmpty()) {
			return new ResponseEntity<List<Product>>(allProduct, HttpStatus.NO_CONTENT);
		}else {
			return new ResponseEntity<List<Product>>(allProduct, HttpStatus.OK);
		}

	}
	@GetMapping(value="/getProductById")
	public ResponseEntity<Product> getProductById(@RequestParam String productId){
		Product product = service.getProductById(productId);
		if(product==null) {
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}	else {
			throw new ProductNotFoundException("products not found for id :==>"+productId);

		}
	}
	@DeleteMapping(value="/deleteProduct")
	public ResponseEntity<Boolean> deleteProduct(@RequestParam String productId){

		boolean isdeleted = service.deleteProduct(productId);
		if(isdeleted) {
			return new ResponseEntity<Boolean>(isdeleted, HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Product not found for delete with id:==>"+ productId);
		}
	}
	@PutMapping(value="/updateProduct")
	public ResponseEntity<Boolean> updateProduct(@RequestBody Product product){

		boolean isupdated = service.updateProduct(product);
		if(isupdated) {
			return new ResponseEntity<Boolean>(HttpStatus.OK);
		}else {
			throw new  ProductNotFoundException("Product not found for update with Id :==>"+product.getProductId());
		}
	}	
	@GetMapping(value="/sortProduct")

	public ResponseEntity<List<Product>> sortProduct(@RequestParam String sortBy){
		List<Product> productsort = service.sortProduct(sortBy);
		
		if(!productsort.isEmpty()) {
		return new  ResponseEntity<List<Product>>(productsort,HttpStatus.OK);
		}else {
			throw new ProductNotFoundException("Product not found for sort:==>"+ sortBy);
		}
	}

	@GetMapping(value="/getMaxPriceProduct")

	public ResponseEntity<Product> getMaxPriceProduct(){
		Product maxPriceProduct = service.getMaxPriceProduct();
		if(maxPriceProduct != null) {
			return new ResponseEntity<Product>(maxPriceProduct,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(maxPriceProduct,HttpStatus.NO_CONTENT);

		}
	}

		@GetMapping(value="/sumOfProductPrice")

	public ResponseEntity<Double>sumOfProductPrice(){
		double Sum = service.sumOfProductPrice();
		if(Sum>0) {
		return new ResponseEntity<Double>(Sum,HttpStatus.OK);
		}else {
			return new ResponseEntity<Double>(Sum, HttpStatus.NO_CONTENT);
		}

	}

	@GetMapping(value="/getTotalCountOfProduct")

	public ResponseEntity<Integer> getTotalCountOfProduct(){
		int  numberOfProduct = service.getTotalCountOfProduct();
		if(numberOfProduct >0) {
			return new ResponseEntity<Integer>(numberOfProduct,HttpStatus.OK);
		}else {

		return new ResponseEntity<Integer>(numberOfProduct, HttpStatus.NO_CONTENT);
	}
	}
	@PostMapping(value="/uploadSheet")
	public ResponseEntity<Map<String,String>>uploadSheet(@RequestParam CommonsMultipartFile file,HttpSession session){
		
		Map<String,String> map = service.uploadSheet(file, session);
	return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/exportToExcel")
	public ResponseEntity<String> exportToExcel() {
		String msg = service.exportToExcel();
		return new ResponseEntity<String>(msg, HttpStatus.OK);

	
	}
}