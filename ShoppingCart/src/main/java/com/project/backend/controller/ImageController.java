package com.project.backend.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.project.backend.dto.ImageDto;
import com.project.backend.exception.ResourceNotFoundException;
import com.project.backend.model.Image;
import com.project.backend.response.ApiResponse;
import com.project.backend.service.image.IImageService;

import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

	private final IImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files,
			@RequestParam Long productId) {
		try {
			List<ImageDto> imageDtos = imageService.saveImages(files, productId);
			return ResponseEntity.ok(new ApiResponse("Image Uploaded Successfully", imageDtos));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Image Upload Failed!", e.getMessage()));
		}
	}

	@GetMapping("/image/download/{imageId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
		Image image = imageService.getImageById(imageId);
		ByteArrayResource resource = new ByteArrayResource(
				image.getImage().getBytes(1, (int) image.getImage().length()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + image.getFileName() + "\"")
				.body(resource);
	}

	@PutMapping("/image/{imageId}/update")
	public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
		try {
			Image image = imageService.getImageById(imageId);

			if (image != null) {
				imageService.updateImage(file, imageId);
				return ResponseEntity.ok(new ApiResponse("Image Updated Successfully", null));
			}
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Updation Failed", INTERNAL_SERVER_ERROR));
	}
	
	@DeleteMapping("/image/{imageId}/delete")
	public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
		try {
			Image image = imageService.getImageById(imageId);

			if (image != null) {
				imageService.deleteImageById(imageId);
				return ResponseEntity.ok(new ApiResponse("Image Deleted Successfully", null));
			}
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(new ApiResponse("Deletion Failed", INTERNAL_SERVER_ERROR));
	}
}