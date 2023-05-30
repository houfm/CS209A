package cse.java2.project.controller;

import cse.java2.project.service.AnswerService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class AcceptedAnswersController {
  AnswerService answerService;

  AcceptedAnswersController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @RequestMapping("/acceptedAnswers")
  public String acceptedAnswers(Model model) {
    // percentage of questions get accepted answers
    double AcceptedPer = answerService.getQuestionGetAcceptedAnswerPercentage();
    List<Map<String, Object>> Data1 = new ArrayList<>();
    Data1.add(Map.of("name", "Accepted", "value", String.format("%.2f", AcceptedPer * 100)));
    Data1.add(Map.of("name", "Not Accepted", "value", String.format("%.2f", (1 - AcceptedPer) * 100)));
    model.addAttribute("pieData1", Data1);

    // answer accepted time – question post time distribution
    Map<Long, Integer> timeMap = answerService.getTimeGapDistribution();
    List<Pair<Long, Integer>> timeList = new ArrayList<>();
    for (Map.Entry<Long, Integer> entry : timeMap.entrySet()) {
      timeList.add(Pair.of(entry.getKey(), entry.getValue()));
    }
    //升序
    timeList.sort(Comparator.comparing(Pair::getFirst));

    for (int i = 1; i < timeList.size(); i++) {
      timeList.set(i, Pair.of(timeList.get(i).getFirst(), timeList.get(i - 1).getSecond() + timeList.get(i).getSecond()));

    }
//        List<Map<String, Object>> Data2 = new ArrayList<>();
//        for (Pair<Long, Integer> pair : timeList) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", pair.getFirst());
//            map.put("value", pair.getSecond());
//            Data2.add(map);
//        }
    List<List<Long>> lineData2 = new ArrayList<>();
    List<List<Long>> lineData1 = new ArrayList<>();
    for (Pair<Long, Integer> pair : timeList) {
      List<Long> list = new ArrayList<>();
      list.add(pair.getFirst());
      list.add((long) pair.getSecond());
      lineData2.add(list);
    }
    for (Pair<Long, Integer> pair : timeList) {
      //只取前12小时
      if (pair.getFirst() <= 12 * 60 * 60) {
        List<Long> list = new ArrayList<>();
        list.add(pair.getFirst());
        list.add((long) pair.getSecond());
        lineData1.add(list);
      }
    }
    model.addAttribute("lineData1", lineData1);
    model.addAttribute("lineData2", lineData2);
//        List<String> xData2 = new ArrayList<>();
//        List<Integer> yData2 = new ArrayList<>();
//        pairToXY(timeList, xData2, yData2);
//        xData2.replaceAll(a -> ((int)(Math.pow(Double.parseDouble(a), 0.5)*100))/100 + "");
//        model.addAttribute("lineDatax", xData2);
//        model.addAttribute("lineDatay", yData2);

    // percentage of questions that non-accepted answers are upvote more than accepted answers
    double nonAcceptedMorePer =
            answerService.getPercentageOfQuestionWithMoreVoteOnNonAcceptedAnswer();
    List<Map<String, Object>> Data3 = new ArrayList<>();
    Data3.add(Map.of("name", "More than", "value", String.format("%.2f", nonAcceptedMorePer * 100)));
    Data3.add(Map.of("name", "Less than", "value", String.format("%.2f", (1 - nonAcceptedMorePer) * 100)));
    model.addAttribute("pieData2", Data3);

    return "acceptedAnswers";
  }

}
