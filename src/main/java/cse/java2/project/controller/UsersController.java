package cse.java2.project.controller;

import cse.java2.project.model.Answer;
import cse.java2.project.model.Comment;
import cse.java2.project.model.Question;
import cse.java2.project.service.AnswerService;
import cse.java2.project.service.CommentService;
import cse.java2.project.service.QuestionService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import static cse.java2.project.utils.utils.pairToXY;

@Controller
public class UsersController {

    QuestionService questionService;
    CommentService commentService;
    AnswerService answerService;

    UsersController(QuestionService questionService, CommentService commentService, AnswerService answerService) {
        this.questionService = questionService;
        this.commentService = commentService;
        this.answerService = answerService;
    }

    @RequestMapping("/users")
    public String users(Model model) {

        //1
        List<Integer> userNum = questionService.getAllQuestionsUserCount();
        List<Integer> userNumByInterval = new ArrayList<>();
        userNumByInterval.add(questionService.getUserNumByInterval(userNum, 0, 0));
        userNumByInterval.add(questionService.getUserNumByInterval(userNum, 1, 10));
        userNumByInterval.add(questionService.getUserNumByInterval(userNum, 11, 100));
        userNumByInterval.add(questionService.getUserNumByInterval(userNum, 101, 1000));
        userNumByInterval.add(questionService.getUserNumByInterval(userNum, 1001, 10000));

        List<String> xData1 = new ArrayList<>();
        xData1.add("0");
        xData1.add("1-10");
        xData1.add("11-100");
        xData1.add("101-1000");
        xData1.add("1001+");
        List<Integer> yData1 = userNumByInterval;
        model.addAttribute("xData1", xData1);
        model.addAttribute("yData1", yData1);

        //2

        Map<Long, Integer> commentUserList = commentService.getAllCommentUsers();
        Map<Long, Integer> answerUserList = answerService.getAllAnswerUsers();
        List<String> xData2 = new ArrayList<>();
        xData2.add("CommentUser");
        xData2.add("AnswerUser");
        List<Integer> yData2 = new ArrayList<>();
        yData2.add(commentUserList.size());
        yData2.add(answerUserList.size());
        model.addAttribute("xData2", xData2);
        model.addAttribute("yData2", yData2);

        //3
        Map<Long, Integer> questionUserList = questionService.getAllQuestionUsers();
        Set<Long> userList = new HashSet<>();
        userList.addAll(questionUserList.keySet());
        userList.addAll(commentUserList.keySet());
        userList.addAll(answerUserList.keySet());
        List<Pair<Long, Integer>> userScoreList = new ArrayList<>();
        for (Long userId : userList) {
           //question*5+answer*3+comment*2
            int score = 0;
            if (questionUserList.containsKey(userId)) {
                score += questionUserList.get(userId) * 5;
            }
            if (answerUserList.containsKey(userId)) {
                score += answerUserList.get(userId) * 3;
            }
            if (commentUserList.containsKey(userId)) {
                score += commentUserList.get(userId) * 2;
            }
            userScoreList.add(Pair.of(userId, score));
        }
        List<String> xData3 = new ArrayList<>();
        List<Integer> yData3 = new ArrayList<>();
        pairToXY(userScoreList, xData3, yData3);
        model.addAttribute("xData3", xData3);
        model.addAttribute("yData3", yData3);

        return "users";
    }

}