package com.am.socket.dao;

import com.am.socket.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentMapper extends MongoRepository<Comment, String>{
    List<Comment> findByMomentId(String momentId);
    Comment findByCommentId(String commentId);
}
