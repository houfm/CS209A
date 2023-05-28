package cse.java2.project.controller;

import cse.java2.project.model.Answer;
import cse.java2.project.service.AnswerService;
import cse.java2.project.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class NumberOfAnswersController {
    QuestionService questionService;

    public NumberOfAnswersController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping("/number-of-answers")
    public String numberOfAnswers(Model model) {
        // no ans percentage
        double noAnsPercentage = questionService.getNoAnswerQuestionPercentage();
        model.addAttribute("noAnsPercentage", noAnsPercentage);

        // avg max
        double avgAnswerNum = questionService.getAvgAnswerCount();
        model.addAttribute("avgAnswerNum", avgAnswerNum);
        int maxAnswerNum = questionService.getMaxAnswerCount();
        model.addAttribute("maxAnswerNum", maxAnswerNum);

        // distribution
        List<Object[]> distribution = questionService.getQuestionCountGroupByAnswerCount();
        model.addAttribute("distribution", distribution);
        return "number-of-answers";
    }
}
