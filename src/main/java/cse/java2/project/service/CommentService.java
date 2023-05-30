package cse.java2.project.service;

import cse.java2.project.model.Comment;
import cse.java2.project.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Map<Long, Integer> getAllCommentUsers() {
        List<Comment> commentList = getAllComments();
        Map<Long, Integer> userList = new HashMap<>();
        for (Comment comment : commentList) {
            if (userList.containsKey(comment.getAccountId())) {
                userList.put(comment.getAccountId(), userList.get(comment.getAccountId()) + 1);
            } else {
                userList.put(comment.getAccountId(), 1);
            }
        }
        return userList;
    }

    public Optional<Comment> getComment(Long commentId) {
        return commentRepository.findById(commentId);
    }
}
