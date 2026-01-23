package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    // JpaRepository を継承するだけで findAll() など基本的なメソッドが使えます
	@Query("SELECT COUNT(i) FROM Item i")
	int countAllItems();
}

