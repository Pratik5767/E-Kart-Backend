package com.project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByName(String name);

}