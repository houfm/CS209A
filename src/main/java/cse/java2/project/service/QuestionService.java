package cse.java2.project.service;

import cse.java2.project.model.Question;
import cse.java2.project.model.Tag;
import cse.java2.project.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository TagRepository;

    public QuestionService(QuestionRepository questionRepository,
                           TagRepository TagRepository) {
        this.questionRepository = questionRepository;
        this.TagRepository = TagRepository;
    }

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestion(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public List<Question> getQuestionsByTags(List<String> tags) {
        Set<Question> questions = new HashSet<>();
        for (String tag : tags) {
            Tag t = TagRepository.findAllByTag(tag);
                questions.addAll(t.getQuestions());
        }
        //distinct
        return new ArrayList<>(questions);
    }
}
