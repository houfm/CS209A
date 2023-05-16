package cse.java2.project.controller;

import cse.java2.project.model.Question;
import cse.java2.project.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/questions")
public class RestController {

    private final QuestionService questionService;

    public RestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getQuestions() {
        return questionService.getQuestions();
    }
}
