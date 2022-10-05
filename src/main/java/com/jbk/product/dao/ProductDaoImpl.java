package com.jbk.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbk.product.entity.Product;

@Repository
public class ProductDaoImpl implements ProductDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public boolean saveProduct(Product product) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		boolean isAdded = false;
		try {
			session.save(product);
			transaction.commit();
			isAdded=true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(session !=null) {
				session.close();
			}
		}
		
		return isAdded;
	}

	@Override
	public List<Product> getAllProduct() {
		Session session = sessionFactory.openSession();
		  Criteria criteria = session.createCriteria(Product.class);
		  List<Product>list=null;
		  try {
			list =criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(session !=null)
				session.close();
		}
		
		return list;
	}
	
	@Override
	public Product getProductById(String productId) {
		Session session = sessionFactory.openSession();
		Product product = null;
	
			try {
				product =session.get(Product.class, productId);
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(session!=null)
				session.close();	
			}
				return product;
		}

	@Override
	public boolean deleteProduct(String productId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		boolean isDeleted=false;
		
		try {
		Product product = session.get(Product.class, productId);
		
		if(product!=null) {
		session.delete(product);
		transaction.commit();
		isDeleted=true;
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(session!=null)
				session.close();
		}
		System.out.println("Product deleted");
		 
		return isDeleted;
	}

	@Override
	public boolean updateProduct(Product product) {
		Session session = null;
			 Transaction transaction = null;
			 boolean isUpdated=false;
			 try {
				 session= sessionFactory.openSession();
				 transaction=session.beginTransaction();
			Product prd= session.get(Product.class, product.getProductId());
			
			if(prd!=null) {
				session.evict(prd);
			 session.update(product);
			 transaction.commit();
			 isUpdated=true;
			}
			
			 }catch(Exception e){
				 e.printStackTrace();
			 }finally {
				 if(session!=null)
					 session.close();
				 System.out.println("product updated");
			 }
			 
		return isUpdated;
	}

	
	@Override
	public int uploadProductList(List<Product> list) {
		Session session = null;
		Transaction transaction = null;
		int count = 0;
		try {
		
			for (Product product : list) {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				session.save(product);
				transaction.commit();
				count++;
				
			
			}
			} catch (Exception e) {
			e.printStackTrace();
	 	}finally {
			if(session !=null) {
				session.close();
			}
		}
		return count;
	}
}
