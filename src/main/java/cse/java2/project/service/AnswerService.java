package cse.java2.project.service;

import cse.java2.project.model.Answer;
import cse.java2.project.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Optional<Answer> getAnswer(Long answerId) {
        return answerRepository.findById(answerId);
    }
}
