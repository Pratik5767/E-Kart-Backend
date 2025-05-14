package com.project.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.dto.ProductDto;
import com.project.backend.exception.AlreadyExistsException;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Product;
import com.project.backend.request.AddProductRequest;
import com.project.backend.request.UpdateProductRequest;
import com.project.backend.response.ApiResponse;
import com.project.backend.service.product.IProductService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

	private final IProductService productService;

	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
		return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
	}

	@GetMapping("/product/{productId}/product")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
		try {
			Product product = productService.getProductById(productId);
			ProductDto productDto = productService.convertToDto(product);
			
			return ResponseEntity.ok(new ApiResponse("Product found with id :: " + productId, productDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
		try {
			Product newProduct = productService.addProduct(product);
			ProductDto productDto = productService.convertToDto(newProduct);
			return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/product/{productId}/update")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request,
			@PathVariable Long productId) {
		try {
			Product updateProduct = productService.updateProduct(request, productId);
			ProductDto productDto = productService.convertToDto(updateProduct);
			return ResponseEntity.ok(new ApiResponse("Product updated successfully", productDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@DeleteMapping("/product/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProductById(productId);
			return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@GetMapping("/product/by/brand-and-name/products")
	public ResponseEntity<ApiResponse> findProductsByBrandAndName(@RequestParam String brandName,
			@RequestParam String productName) {
		try {
			List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found ", null));
			}
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Product found!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/product/by/category-and-brand/products")
	public ResponseEntity<ApiResponse> findProductsByCategoryAndBrand(@RequestParam String category,
			@RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found ", null));
			}
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Product found!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/product/{name}/products")
	public ResponseEntity<ApiResponse> findProductsByName(@PathVariable String name) {
		try {
			List<Product> products = productService.getProductsByName(name);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found ", null));
			}
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Product found!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/product/by-brand/products")
	public ResponseEntity<ApiResponse> findProductsByBrand(@RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByBrand(brand);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found ", null));
			}
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Product found!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/product/{category}/all/products")
	public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category) {
		try {
			List<Product> products = productService.getProductsByCategory(category);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found ", null));
			}
			
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Product found!", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

	@GetMapping("/product/count/by-brand-and-name")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
			@RequestParam String name) {
		try {
			var productCount = productService.countProductsByBrandAndName(brand, name);
			return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error!", e.getMessage()));
		}
	}

}