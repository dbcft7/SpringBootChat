package com.am.socket.mapper;

import com.am.socket.Application;
import com.am.socket.dao.MongoTestMapper;
import com.am.socket.model.MongoTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MongoTestMapperTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTestMapper mongoTestMapper;

    @Before
    public void setUp() {
        mongoTestMapper.deleteAll();
    }

    @Test
    public void test() throws Exception {
        MongoTest mongoTest1 = new MongoTest();
        mongoTest1.setKey1("1");
        mongoTest1.setKey2("1");
        mongoTest1.setKey3(1);
        mongoTest1.setKey4(new Date());
        mongoTestMapper.save(mongoTest1);

        MongoTest mongoTest2 = new MongoTest();
        mongoTest2.setKey1("2");
        mongoTest2.setKey2("2");
        mongoTest2.setKey3(2);
        mongoTest2.setKey4(new Date());
        mongoTestMapper.save(mongoTest2);

        List<MongoTest>  mongoTestList = mongoTestMapper.findAll();
        log.info("find: {}", mongoTestList.size());

        MongoTest test1 = mongoTestMapper.findByKey1("1");
        log.info("find key1:{}", test1.getKey1());
        test1.setKey1("2");
        mongoTestMapper.save(test1);
        MongoTest test1change = mongoTestMapper.findOne(test1.getId());
        log.info("find key1:{}", test1change.getKey1());
        mongoTestMapper.delete(test1);

        MongoTest test2 = mongoTestMapper.findByKey2("2");
        mongoTestMapper.delete(test2);
        log.info("find: {}", mongoTestMapper.findAll().size());
    }
}
