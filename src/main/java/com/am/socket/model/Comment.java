package com.am.socket.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Comment {

    @Id
    private String commentId;

    private String momentId;
    private String targetCommentId;
    private int userId;
    private String username;
    private Date pubtime;
    private String comment;
    private int targetUserId;
    private String targetUsername;

    public  String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(String momentId) {
        this.momentId = momentId;
    }

    public String getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(String targetCommentId) {
        this.targetCommentId = targetCommentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getPubtime() {
        return pubtime;
    }

    public void setPubtime(Date pubtime) {
        this.pubtime = pubtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }


}
