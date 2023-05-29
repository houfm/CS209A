package cse.java2.project.controller;


import cse.java2.project.model.Tag;
import cse.java2.project.service.TagService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cse.java2.project.utils.utils.pairToXY;

@Controller

public class TagsController {

    TagService tagService;


    TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping("/tags")
    public String tags(Model model) {
        //1
        List<Pair<String, Integer>> tagList1 = tagService.getTop40FrequencyTags();

//        List<String> xData1 = new ArrayList<>();
//        List<Integer> yData1 = new ArrayList<>();
//
//        pairToXY(tagList1, xData1, yData1);
//        model.addAttribute("xData1", xData1);
//        model.addAttribute("yData1", yData1);
        List<Map<String, Object>> tagMap=new ArrayList<>();
        for(Pair<String, Integer> pair:tagList1){
            Map<String, Object> map=new HashMap<>();
            map.put("name",pair.getFirst());
            map.put("value",pair.getSecond());
            tagMap.add(map);
        }
        model.addAttribute("wordcloudData", tagMap);

        //2
        List<Pair<Tag, Integer>> tagList21 = tagService.getTop20ScoreTags();
        List<Pair<String, Integer>> tagList22 = tagService.getTop20ScoreTagCombinations(2);
//        List<Pair<String, Integer>> tagList23 = tagService.getTop10ScoreTagCombinations(3);
        List<String> xData21 = new ArrayList<>();
        List<Integer> yData21 = new ArrayList<>();
        List<String> xData22 = new ArrayList<>();
        List<Integer> yData22 = new ArrayList<>();
//        List<String> xData23 = new ArrayList<>();
//        List<Integer> yData23 = new ArrayList<>();

        pairToXY(tagList21, xData21, yData21);
        pairToXY(tagList22, xData22, yData22);
//        pairToXY(tagList23, xData23, yData23);
        model.addAttribute("barDatax1", xData21);
        model.addAttribute("barDatay1", yData21);
        model.addAttribute("barDatax2", xData22);
        model.addAttribute("barDatay2", yData22);
//        model.addAttribute("xData23", xData23);
//        model.addAttribute("yData23", yData23);


        //3
        List<Pair<Tag, Integer>> tagList31 = tagService.getTop20ViewCntTags();
        List<Pair<String, Integer>> tagList32 = tagService.getTop20ViewCntTagCombinations(2);
//        List<Pair<String, Integer>> tagList33 = tagService.getTop10ViewCntTagCombinations(3);
        List<String> xData31 = new ArrayList<>();
        List<Integer> yData31 = new ArrayList<>();
        List<String> xData32 = new ArrayList<>();
        List<Integer> yData32 = new ArrayList<>();
//        List<String> xData33 = new ArrayList<>();
//        List<Integer> yData33 = new ArrayList<>();
        pairToXY(tagList31, xData31, yData31);
        pairToXY(tagList32, xData32, yData32);
//        pairToXY(tagList33, xData33, yData33);
        model.addAttribute("barDatax3", xData31);
        model.addAttribute("barDatay3", yData31);
        model.addAttribute("barDatax4", xData32);
        model.addAttribute("barDatay4", yData32);
//        model.addAttribute("xData33", xData33);
//        model.addAttribute("yData33", yData33);

        return "tags";
    }

}
