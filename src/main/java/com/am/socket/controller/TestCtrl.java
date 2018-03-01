package com.am.socket.controller;
import com.am.socket.model.Test;
import com.am.socket.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestCtrl {
    @Resource
    private TestService testService;

    @GetMapping("/list")
    public List<Test> list() {
        List<Test> list = testService.findAll();
        return list;
    }
}
