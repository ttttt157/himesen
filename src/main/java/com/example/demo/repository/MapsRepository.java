package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Maps;

@Repository
public interface MapsRepository extends JpaRepository<Maps, Integer> {
    Maps findByMapname(String mapname);
}
