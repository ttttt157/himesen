package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserCharacterStamp;

public interface UserCharacterStampRepository extends JpaRepository<UserCharacterStamp, Integer> {
    Optional<UserCharacterStamp> findByUsernameAndRecordCharactername(String username, String recordCharactername);
    
    List<UserCharacterStamp> findByUsername(String username);
}
