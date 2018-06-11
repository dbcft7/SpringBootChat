package com.am.socket.mapper;

import com.am.socket.Application;
import com.am.socket.dao.CommentMapper;
import com.am.socket.model.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CommentMapperTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void getCommentFromCommentId() throws Exception {
        String commentId = "5b1eeba6b30284114c483d97";
        Comment comment = commentMapper.findByCommentId(commentId);
        log.info(comment.getUsername());
    }
}
