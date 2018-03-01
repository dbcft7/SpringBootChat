package com.am.socket.service;
import com.am.socket.model.Test;
import com.am.socket.dao.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/01/31.
 */
@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List<Test> findAll() {
        return testMapper.findAll();
    }

}
