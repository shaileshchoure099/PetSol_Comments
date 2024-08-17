package com.pertSol.comments.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pertSol.comments.Entity.Comment;
import com.pertSol.comments.Repository.CommentRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;

	@Autowired
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public List<Comment> getAllComments() {
		return commentRepository.findAll();
	}

	public List<Comment> getCommentsByUsername(String username) {
		return commentRepository.findByBy(username);
	}

	public List<Comment> getCommentsByDate(LocalDateTime date) {
		return commentRepository.findByCreatedAt(date);
	}

	public Comment createComment(Comment comment) {
		return commentRepository.save(comment);
	}

	public Comment getCommentById(Long id) {
		return commentRepository.findById(id).orElse(null);
	}

	public Comment updateComment(Comment comment) {
		return commentRepository.save(comment);
	}

	public boolean deleteComment(Long id) {
		if (commentRepository.existsById(id)) {
			commentRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
