package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Maps;
import com.example.demo.repository.MapsRepository;

@Service
public class MapService {

    @Autowired
    private MapsRepository mapsRepository; // static は削除

    // 全マップ取得
    public List<Maps> findAllMaps() {
        return mapsRepository.findAll();
    }

    // IDで取得
    public Maps findById(int mapid) {
        return mapsRepository.findById(mapid).orElse(null);
    }

    // 保存・更新
    public Maps save(Maps map) {
        return mapsRepository.save(map);
    }

    // 削除
    public void deleteMapById(int mapid) {
        mapsRepository.deleteById(mapid);
    }

    // 名前で取得
    public Maps findByMapname(String mapname) {
        return mapsRepository.findByMapname(mapname);
    }
    
    public boolean createMap(Maps map) {
        if (mapsRepository.existsById(map.getMapid())) {
            return false; // ID 重複
        }
        mapsRepository.save(map);
        return true;
    }
}
