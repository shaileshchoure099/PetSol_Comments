package com.pertSol.comments.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pertSol.comments.Entity.Comment;
import com.pertSol.comments.Exception.InvalidDateFormatException;
import com.pertSol.comments.Service.CommentService;

@RestController
@RequestMapping("/api/v2")
public class CommentController {

	private final CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/comments")
	public ResponseEntity<List<Comment>> getAllComments() {
		List<Comment> comments = commentService.getAllComments();
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/comments/search")
	public ResponseEntity<List<Comment>> getCommentsByUsername(@RequestParam String username) {
		List<Comment> comments = commentService.getCommentsByUsername(username);
		if (comments.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	@GetMapping("/comments/searchDate")
	public ResponseEntity<List<Comment>> getCommentsByDate(@RequestParam String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime localDate = LocalDateTime.parse(date, formatter);

			List<Comment> comments = commentService.getCommentsByDate(localDate);

			if (comments.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (Exception e) {
			throw new InvalidDateFormatException("Invalid date format: " + date);
		}
	}

	@PostMapping("/comments")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
		// Ensure the comment's createdAt field is set, or set it to the current time
		if (comment.getCreatedAt() == null) {
			comment.setCreatedAt(LocalDateTime.now());
		}
		Comment createdComment = commentService.createComment(comment);
		return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
	}

	@PutMapping("/comments/{id}")
	public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
		// Retrieve the existing comment
		Comment existingComment = commentService.getCommentById(id);
		if (existingComment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Update the existing comment's fields
		existingComment.setBy(comment.getBy());
		existingComment.setText(comment.getText());
		existingComment.setCreatedAt(comment.getCreatedAt()); // Only if you want to allow updates to this field

		Comment updatedComment = commentService.updateComment(existingComment);
		return new ResponseEntity<>(updatedComment, HttpStatus.OK);
	}

	@DeleteMapping("/comments/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
		boolean isDeleted = commentService.deleteComment(id);
		if (isDeleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
