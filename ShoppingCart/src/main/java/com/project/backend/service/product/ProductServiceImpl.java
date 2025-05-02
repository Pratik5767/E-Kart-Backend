package com.project.backend.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.project.backend.dto.ImageDto;
import com.project.backend.dto.ProductDto;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Category;
import com.project.backend.model.Image;
import com.project.backend.model.Product;
import com.project.backend.repository.CategoryRepository;
import com.project.backend.repository.ImageRepository;
import com.project.backend.repository.ProductRepository;
import com.project.backend.request.AddProductRequest;
import com.project.backend.request.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	private final ModelMapper modelMapper;

	@Override
	public Product addProduct(AddProductRequest request) {
		Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
				.orElseGet(() -> {
					Category newCategory = new Category(request.getCategory().getName());
					return categoryRepository.save(newCategory);
				});

		request.setCategory(category);
		return productRepository.save(createProduct(request, category));
	}

	private Product createProduct(AddProductRequest request, Category category) {
		return new Product(request.getName(), request.getBrand(), request.getPrice(), request.getInventory(),
				request.getDescription(), category);
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	@Override
	public Product updateProduct(UpdateProductRequest request, Long productId) {
		return productRepository.findById(productId)
				.map(existingProducts -> updateExistingProduct(existingProducts, request)).map(productRepository::save)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
		existingProduct.setName(request.getName());
		existingProduct.setBrand(request.getBrand());
		existingProduct.setPrice(request.getPrice());
		existingProduct.setInventory(request.getInventory());
		existingProduct.setDescription(request.getDescription());

		Category category = categoryRepository.findByName(request.getCategory().getName());
		existingProduct.setCategory(category);
		return existingProduct;
	}

	@Override
	public void deleteProductById(Long id) {
		productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
			throw new ResourceNotFoundException("Product not found");
		});
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		return productRepository.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}

	@Override
	public List<ProductDto> getConvertedProducts(List<Product> products) {
		return products.stream().map(this::convertToDto).toList();
	}
	
	@Override
	public ProductDto convertToDto(Product product) {
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		
		List<Image> images = imageRepository.findByProductId(product.getId());
		List<ImageDto> imageDtos = images.stream()
				.map(image -> modelMapper.map(image, ImageDto.class))
				.toList();
		productDto.setImages(imageDtos);
		return productDto;
	}
	
}