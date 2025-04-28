package com.project.backend.dto;

import lombok.Data;

@Data
public class ImageDto {
	
	private Long ImageId;
	private String imageName;
	private String downloadUrl;

}