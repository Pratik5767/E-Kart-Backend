package com.project.backend.service.product;

import java.util.List;

import com.project.backend.model.Product;
import com.project.backend.request.AddProductRequest;
import com.project.backend.request.UpdateProductRequest;

public interface IProductService {

	Product addProduct(AddProductRequest request);
	
	Product getProductById(Long id);
	
	Product updateProduct(UpdateProductRequest request, Long productId);
	
	void deleteProductById(Long id);

	List<Product> getAllProducts();
	
	List<Product> getProductsByCategory(String category);
	
	List<Product> getProductsByBrand(String brand);	
	
	List<Product> getProductsByCategoryAndBrand(String category, String brand);	
	
	List<Product> getProductsByName(String name);
	
	List<Product> getProductsByBrandAndName(String brand, String name);
	
	Long countProductsByBrandAndName(String brand, String name);
}