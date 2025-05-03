package com.project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.backend.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	void deleteAllByCartId(Long id);

}