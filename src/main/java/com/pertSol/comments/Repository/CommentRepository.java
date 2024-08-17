package com.pertSol.comments.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pertSol.comments.Entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	// You can add custom query methods here if needed

	List<Comment> findByBy(String username);

	List<Comment> findByCreatedAt(LocalDateTime createdAt);
}
