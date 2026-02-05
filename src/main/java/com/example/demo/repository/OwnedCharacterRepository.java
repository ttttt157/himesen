package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OwnedCharacter;

@Repository
public interface OwnedCharacterRepository extends JpaRepository<OwnedCharacter, Integer> {
    List<OwnedCharacter> findByUsername(String username);

	boolean existsByUsernameAndOwnedcharacter(String username, String charactername);

    // 追加: 所持キャラ登録は save() で可能なので追加メソッド不要
	   // ★ 追加（レアガチャ用）
    @Query("SELECT o.id FROM OwnedCharacter o WHERE o.username = :username")
    List<Integer> findOwnedCharacterIdsByUsername(@Param("username") String username);


}
