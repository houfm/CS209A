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
}
