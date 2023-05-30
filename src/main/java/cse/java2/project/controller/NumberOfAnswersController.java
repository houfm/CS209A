package cse.java2.project.controller;

import cse.java2.project.model.Answer;
import cse.java2.project.service.AnswerService;
import cse.java2.project.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class NumberOfAnswersController {
    QuestionService questionService;

    public NumberOfAnswersController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping("/numberOfAnswers")
    public String numberOfAnswers(Model model) {
        // no ans percentage
        double noAnsPercentage = questionService.getNoAnswerQuestionPercentage();
        List<Map<String, Object>> Data1 = new ArrayList<>();
        Data1.add(Map.of("name", "No Answer", "value", String.format("%.2f", noAnsPercentage * 100)));
        Data1.add(Map.of("name", "Answered", "value", String.format("%.2f", (1 - noAnsPercentage) * 100)));
        model.addAttribute("pieData1", Data1);
        // avg max
        double avgAnswerNum = questionService.getAvgAnswerCount();
        model.addAttribute("avgAnswerNum", String.format("%.2f", avgAnswerNum));
        int maxAnswerNum = questionService.getMaxAnswerCount();
        model.addAttribute("maxAnswerNum", maxAnswerNum);

        // distribution
        List<Object[]> distribution = questionService.getQuestionCountGroupByAnswerCount();
        //升序
        distribution.sort((o1, o2) -> {
            int a = Integer.parseInt(o1[0].toString());
            int b = Integer.parseInt(o2[0].toString());
            return a - b;
        });
        List<String> Datax3 = new ArrayList<>();
        List<String> Datay3 = new ArrayList<>();
        for (Object[] objects : distribution) {
            Datax3.add(objects[0].toString());
            Datay3.add(objects[1].toString());
        }
        model.addAttribute("Datax3", Datax3);
        model.addAttribute("Datay3", Datay3);
        return "numberOfAnswers";
    }
}
