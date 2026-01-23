package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserItem;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUsername(String username);
    boolean existsByUsernameAndItemname(String username, String itemname);

    // ここを追加：Spring Data JPA が自動で SELECT COUNT を生成
    int countByUsername(String username);
}
