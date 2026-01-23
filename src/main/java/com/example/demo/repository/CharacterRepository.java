package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Optional<Character> findByCharactername(String charactername);
    List<Character> findByAvailability(String availability);
    List<Character> findByAvailabilityAndFightstyle(String availability, int fightstyle);
    List<Character> findByAffiliation(String mapname);
    int countByAvailability(String string);

    // 追加: 複数の availability をまとめて取得
    List<Character> findByAvailabilityIn(List<String> availabilities);
}
