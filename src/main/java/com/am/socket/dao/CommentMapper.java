package com.am.socket.dao;

import com.am.socket.model.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    void insertCommentIntoComments(Comment comment);
    void deleteCommentFromComments(@Param("commentId") int commentId);
    List<Comment> findMoreCommentFromComments(@Param("momentId") int momentId);
    Comment findCommentFromComments(@Param("commentId") int commentId);
}
