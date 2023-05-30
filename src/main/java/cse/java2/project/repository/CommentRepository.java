package cse.java2.project.repository;

import cse.java2.project.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query(value = "SELECT (regexp_match(q.body, 'java\\.[a-zA-Z0-9]+?\\.[a-zA-Z0-9]+(?=[^a-zA-Z0-9])'))[1] FROM comment q WHERE q.body ~ 'java\\.[a-zA-Z0-9]+?\\.[a-zA-Z0-9]+'", nativeQuery = true)
  List<String> findJavaApi();
}
