package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.LevelExperience;

@Repository
public interface LevelExperienceRepository extends JpaRepository<LevelExperience, Integer> {
    LevelExperience findByLevel(int level);
}
