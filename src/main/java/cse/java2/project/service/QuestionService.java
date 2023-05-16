package cse.java2.project.service;

import cse.java2.project.model.Question;
import cse.java2.project.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

}
