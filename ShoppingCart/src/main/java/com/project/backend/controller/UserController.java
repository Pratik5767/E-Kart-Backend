package com.project.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.dto.UserDto;
import com.project.backend.exception.AlreadyExistsException;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.User;
import com.project.backend.request.CreateUserRequest;
import com.project.backend.request.UpdateUserRequest;
import com.project.backend.response.ApiResponse;
import com.project.backend.service.user.IUserService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@GetMapping("/user/{userId}/user")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
		try {
			User user = userService.getUserById(userId);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.ok(new ApiResponse("User found!", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PostMapping("/user/add")
	public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
		try {
			User user = userService.createUser(request);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.ok(new ApiResponse("User created successfully!", userDto));
		} catch (AlreadyExistsException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/user/{userId}/update")
	public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {		
		try {
			User user = userService.updateUser(request, userId);
			UserDto userDto = userService.convertToDto(user);
			return ResponseEntity.ok(new ApiResponse("User updated successfully!", userDto));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
	
	@DeleteMapping("/user/{userId}/delete")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok(new ApiResponse("User deleted successfully!", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}
}
