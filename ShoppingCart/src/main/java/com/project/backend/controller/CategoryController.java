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
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.exception.AlreadyExistsException;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Category;
import com.project.backend.response.ApiResponse;
import com.project.backend.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final ICategoryService categoryService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse> getAllCategories() {
		try {
			List<Category> categories = categoryService.getAllCategories();
			return ResponseEntity.ok(new ApiResponse("Found!", categories));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
		}
	}	
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
		try {
			Category category = categoryService.addCategory(name);
			return ResponseEntity.ok(new ApiResponse("Category Added Successfully", category));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/category/{categoryId}/category")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
		try {
			Category category = categoryService.getCategoryById(categoryId);
			return ResponseEntity.ok(new ApiResponse("Category found with id :: " + categoryId, category));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@GetMapping("/category/{name}/category")
	public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
		try {
			Category category = categoryService.getCategoryByName(name);
			return ResponseEntity.ok(new ApiResponse("Category found with name :: " + name, category));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@DeleteMapping("/category/{categoryId}/delete")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
		try {
			categoryService.deleteCategory(categoryId);
			return ResponseEntity.ok(new ApiResponse("Category deleted with id :: " + categoryId, null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@PutMapping("/category/{categoryId}/update")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
		try {
			Category updateCategory = categoryService.updateCategory(category, categoryId);
			return ResponseEntity.ok(new ApiResponse("Category updated successfully", updateCategory));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
}