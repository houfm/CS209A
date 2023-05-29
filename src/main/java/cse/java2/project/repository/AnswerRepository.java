package cse.java2.project.repository;

import cse.java2.project.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    long countByIsAccepted(boolean accept);

    List<Answer> findAnswerByIsAccepted(boolean accept);
    List<Long> findQuestionIdByIsAccepted(boolean accept);

    long countByQuestionId(long questionId);
    List<Answer> findAnswerByQuestionIdAndIsAccepted(long questionId, boolean accept);

    int findScoreByAnswerId(long answerId);

    Answer findByAnswerId(long answerId);


    List<Answer> findAnswersByQuestionId(long questionId);

}
