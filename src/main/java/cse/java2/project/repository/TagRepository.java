package cse.java2.project.repository;

import cse.java2.project.model.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByTag(String tag);

    @Query("SELECT t FROM Tag t WHERE t.tag <> 'java' GROUP BY t ORDER BY size(t.questionList) DESC")
    List<Tag> findTopNByOrderByQuestionCntDesc(Pageable pageable);


    @Query("SELECT t FROM Tag t JOIN t.questionList q WHERE t.tag <> 'java' GROUP BY t ORDER BY SUM(q.viewCount) DESC")
    List<Tag> findTopNByOrderByViewCntDesc(Pageable pageable);

    @Query(value = "SELECT tag_api,count(tag_api) from (SELECT tag_id,(regexp_match(tag.tag_name, 'java\\.[a-zA-Z0-9]+?\\.[a-zA-Z0-9]+(?=[^a-zA-Z0-9])'))[1] tag_api FROM tag  WHERE tag.tag_name ~ 'java\\.[a-zA-Z0-9]+?\\.[a-zA-Z0-9]+') q  JOIN question_tag ON q.tag_id = question_tag.tag_id GROUP BY tag_api", nativeQuery = true)
    List<Object[]> findJavaApi();

}
