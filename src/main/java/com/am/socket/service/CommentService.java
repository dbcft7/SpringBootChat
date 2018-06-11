package com.am.socket.service;

import com.am.socket.dao.CommentMapper;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.Comment;
import com.am.socket.model.Moment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommentMapper commentMapper;

    public String sendComment(String momentId, int userId, String username, String commentContent) {
        Comment comment = new Comment();
        Date pubtime = new Date();
        comment.setComment(commentContent);
        comment.setPubtime(pubtime);
        comment.setMomentId(momentId);
        comment.setUserId(userId);
        comment.setUsername(username);
        commentMapper.save(comment);
        log.info("sent comment successfully!");
        return "sent comment successfully!";
    }

    public String sendComment(String momentId, String targetCommentId, int userId, String  username, String commentContent) {
        Comment targetComment = commentMapper.findByCommentId(targetCommentId);
        int targetUserId = targetComment.getUserId();
        String targetUsername = targetComment.getUsername();
        Date pubtime = new Date();
        Comment comment = new Comment();
        comment.setUsername(username);
        comment.setUserId(userId);
        comment.setMomentId(momentId);
        comment.setTargetUserId(targetUserId);
        comment.setTargetUsername(targetUsername);
        comment.setTargetCommentId(targetCommentId);
        comment.setComment(commentContent);
        comment.setPubtime(pubtime);
        commentMapper.save(comment);
        log.info("sent comment successfully!");
        return "sent comment successfully!";
    }

    public String deleteComment(String commentId) {
        Comment comment = commentMapper.findByCommentId(commentId);
        commentMapper.delete(comment);
        log.info("comment deleted successfully!");
        return "comment deleted successfully!";
    }

    public List<Comment> getComments(String momentId) {
        log.info("got comments successfully!");
        return commentMapper.findByMomentId(momentId);
    }
}
