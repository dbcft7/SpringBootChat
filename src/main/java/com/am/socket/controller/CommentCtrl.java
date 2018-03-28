package com.am.socket.controller;

import com.am.socket.model.Comment;
import com.am.socket.model.User;
import com.am.socket.service.CommentService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentCtrl {
    @Resource
    private CommentService commentService;
    private static final String SESSION_ATTRIBUTE = "user";

    @GetMapping("/sendCommentToMoment")
    public String sendCommentToMoment(@RequestParam("momentId") int momentId, @RequestParam("commentContent") String commentContent, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        return commentService.sendComment(momentId, user.getId(), user.getUsername(), commentContent);
    }

    @GetMapping("/sendCommentToComment")
    public String sendCommentToComment(@RequestParam("momentId") int momentId, @RequestParam("targetCommentId") int targetCommentId, @RequestParam("commentContent") String commentContent, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        return commentService.sendComment(momentId, targetCommentId, user.getId(), user.getUsername(), commentContent);
    }

    @GetMapping("/getCommentList")
    public List<Comment> getCommentList(@RequestParam("momentId") int momentId, HttpSession session) {
        return commentService.getComments(momentId);
    }

    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam("CommentId") int commentId) {
        return commentService.deleteComment(commentId);
    }
}
