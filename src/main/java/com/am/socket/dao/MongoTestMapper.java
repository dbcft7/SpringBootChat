package com.am.socket.dao;

import com.am.socket.model.MongoTest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoTestMapper extends MongoRepository<MongoTest, String> {
    MongoTest findByKey1(String key1);
    MongoTest findByKey2(String key2);
}
