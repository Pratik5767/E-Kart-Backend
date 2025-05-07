package com.project.backend.service.user;

import com.project.backend.dto.UserDto;
import com.project.backend.model.User;
import com.project.backend.request.CreateUserRequest;
import com.project.backend.request.UpdateUserRequest;

public interface IUserService {

	User getUserById(Long userId);
	
	User createUser(CreateUserRequest request);
	
	User updateUser(UpdateUserRequest request, Long userId);
	
	void deleteUser(Long userId);

	UserDto convertToDto(User user);
}