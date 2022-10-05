package com.jbk.product.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.naming.java.javaURLContextFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.reflect.Java14GenericSignatureInformationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.jbk.product.comparator.ProductIdComparator;
import com.jbk.product.comparator.ProductNameComparator;
import com.jbk.product.dao.ProductDao;
import com.jbk.product.entity.Product;
import com.jbk.product.validataion.ValidateObject;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao dao;

	String excludedRows = "";
	int totalRecordCount = 0;

	@Override
	public boolean saveProduct(Product product) {
		if(product.getProductId() == null) {
			String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
			product.setProductId(id);
		}
		boolean isAdded = dao.saveProduct(product);

		return isAdded;
	}

	@Override
	public List<Product> getAllProduct() {
		List<Product> allProduct = dao.getAllProduct();

		return allProduct;
	}

	@Override
	public Product getProductById(String productId) {
		Product productById = dao.getProductById(productId);
		return productById;
	}


	@Override
	public boolean deleteProduct(String productId) {
		boolean deletedProduct = dao.deleteProduct(productId);

		return deletedProduct;
	}


	@Override
	public boolean updateProduct(Product product) {
		boolean updatedProduct = dao.updateProduct(product);
		return updatedProduct;
	}


	@Override
	public List<Product> sortProduct(String sortBy) {
		List<Product>list = getAllProduct();
		if(!list.isEmpty()) {

			if(sortBy.equalsIgnoreCase("productName")) {
				Collections.sort(list,new ProductNameComparator());

			}else if(sortBy.equalsIgnoreCase("productId")) {
				Collections.sort(list, new ProductIdComparator());
			}
		}
		return list;
	}

	@Override
	public Product getMaxPriceProduct() {
		List<Product> allproduct = getAllProduct();
		Product product = null;
		if(allproduct.size()>1) {
			product = allproduct.stream().max(Comparator.comparingDouble(Product::getProductPrice)).get();
		}
		return product;
	}

	@Override
	public double sumOfProductPrice() {
		List<Product>Productprice = getAllProduct();
		double sum = 0;
		if(Productprice.size()>1) {
			sum = Productprice.stream().mapToDouble(Product::getProductPrice).sum();
		}
		return sum;
	}

	@Override
	public int getTotalCountOfProduct() {
		List<Product>countproduct = getAllProduct();
		int size = 0;
		if(countproduct.size()>0) {
			size = countproduct.size();
		}
		return size;
	}

	public List<Product> readExcel(String filePath){
		Workbook workbook = null;
		FileInputStream fis = null;
		List<Product>list = new ArrayList<Product>();
		Product product = null;
		
		try {
			
			fis = new FileInputStream(new File(filePath));

			workbook = new XSSFWorkbook(fis);

			Sheet sheet = workbook.getSheetAt(0);
			totalRecordCount = sheet.getLastRowNum();
			Iterator<Row> rows = sheet.rowIterator();
			int rowCount = 0;
			
			while(rows.hasNext()) {
				
				Row row = rows.next();
				if (rowCount == 0) {
					rowCount++;
					continue;
				}
				product = new Product();
				Thread.sleep(1);
				String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
				product.setProductId(id);
				Iterator<Cell> cells = row.cellIterator();
				
					while(cells.hasNext()) {
						Cell cell = cells.next();
						int column  = cell.getColumnIndex();

					switch (column) {
					case 0:
						product.setProductName(cell.getStringCellValue());
						break;

					case 1:
						product.setProductType(cell.getStringCellValue());
						break;
						
					case 2:
					
						product.setProductQty((int)cell.getNumericCellValue());
						break;
						
					case 3:
						product.setProductPrice((int)cell.getNumericCellValue());
						break;
		
				}
			}
			boolean isValid = ValidateObject.validateProduct(product);
			if (isValid) {
				list.add(product);
			
			}else {
				int rowNum = row.getRowNum() + 1;
				excludedRows = excludedRows + rowNum + ",";
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (workbook != null)
				workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	return list;

}

	
	@Override
	public Map<String, String> uploadSheet(CommonsMultipartFile file, HttpSession httpSession) {
		
		String path = httpSession.getServletContext().getRealPath("/");
		String fileName = file.getOriginalFilename();
		HashMap<String, String> map = new HashMap<>();
		
		int Uplodedcount = 0;
		FileOutputStream fos = null;
		byte [] data = file.getBytes();

		try {
			System.out.println(path);
			fos = new FileOutputStream(new File(path + File.separator + fileName));
			fos.write(data);

			List<Product> list = readExcel(path + File.separator + fileName);
			Uplodedcount = dao.uploadProductList(list);
				
			map.put("Total Record In Sheet", String.valueOf(totalRecordCount));
			map.put("Uploaded Record In DB", String.valueOf(Uplodedcount));
			map.put("Bad Record Row Number", excludedRows);
			map.put("Total Excluded", String.valueOf(totalRecordCount - Uplodedcount));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	@Override
	public String exportToExcel() {
		List<Product> allProduct = getAllProduct();
		String[] columns = { "ID", "NAME", "QTY", "PRICE", "TYPE" };
		try {

			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

			/*
			 * CreationHelper helps us create instances of various things like DataFormat,
			 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
			 */
			CreationHelper createHelper = workbook.getCreationHelper();

			// Create a Sheet
			Sheet sheet = workbook.createSheet("product");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Create a Row
			Row headerRow = sheet.createRow(0);

			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			// Create Cell Style for formatting Date
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			// Create Other rows and cells with employees data
			int rowNum = 1;
			for (Product product : allProduct) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(product.getProductId());

				row.createCell(1).setCellValue(product.getProductName());

				row.createCell(2).setCellValue(product.getProductQty());

				row.createCell(3).setCellValue(product.getProductPrice());

				row.createCell(4).setCellValue(product.getProductType());
			}

			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream("product.xlsx");
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Created";
	}

}


