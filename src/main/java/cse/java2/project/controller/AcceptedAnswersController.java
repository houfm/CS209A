package cse.java2.project.controller;

import cse.java2.project.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AcceptedAnswersController {
    AnswerService answerService;
    AcceptedAnswersController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @RequestMapping("/acceptedAnswers")
    public String acceptedAnswers(Model model) {
        // percentage of questions get accepted answers
        double questionGetAcceptedAnswerPercentage = answerService.getQuestionGetAcceptedAnswerPercentage();
        model.addAttribute("questionGetAcceptedAnswerPercentage", questionGetAcceptedAnswerPercentage);

        // answer accepted time – question post time distribution
        int[] distribution = answerService.getTimeGapDistribution();
        model.addAttribute("distribution", distribution);

        // percentage of questions that non-accepted answers are upvote more than accepted answers
        double nonAcceptedAnswerUpvoteMorePercentage =
                answerService.getPercentageOfQuestionWithMoreVoteOnNonAcceptedAnswer();
        model.addAttribute("nonAcceptedAnswerUpvoteMorePercentage", nonAcceptedAnswerUpvoteMorePercentage);
        return "acceptedAnswers";
    }

}
