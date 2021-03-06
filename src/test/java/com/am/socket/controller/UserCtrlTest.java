package com.am.socket.controller;

import com.am.socket.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserCtrlTest {

    private static String preUrl = "/user";

    @Resource
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void login() throws Exception {
        String password = "bdZ1/vlRh9GCDEE52sw7zgpmdAEZ5pJlHUFe+/MXlplOs+Ru/hQhW1TVomDbnqezBzOU373/C07gV3AGtPg0MvPvlqJTgZp2OKJGyGmOfqmKbMHE4XyKizDfSgN9yHQ9ylRlCQxSlnnZwIvQjZhsDgJbMHSsdcNxhjUaYGLEXUU=";
        String loginUrl = preUrl + "/login";
        mockMvc.perform(
                get(loginUrl)
                        .param("username", "mazyi")
                        .param("password", password)
                        .param("captcha", "TW6T")
                        .param("uuid", "02a123d2-acf0-46b2-9ba7-43eaa0b34f30")
        ).andDo(print());
    }

    @Test
    public void sendMoment () throws Exception{
        login();
        String content = "the weather is so good today!";
        String sendMomentUrl = preUrl + "/sendMoment";
        mockMvc.perform(
                get(sendMomentUrl)
                        .param("content", content)
        ).andDo(print());
    }

}
