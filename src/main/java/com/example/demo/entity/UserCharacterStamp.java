package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_character_stamp",
       uniqueConstraints = @UniqueConstraint(columnNames = {"username", "record_charactername"}))
public class UserCharacterStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(name = "record_charactername", nullable = false)
    private String recordCharactername;

    // getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRecordCharactername() { return recordCharactername; }
    public void setRecordCharactername(String recordCharactername) { this.recordCharactername = recordCharactername; }
}
