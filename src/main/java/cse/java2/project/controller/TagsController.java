package cse.java2.project.controller;


import cse.java2.project.model.Tag;
import cse.java2.project.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller

public class TagsController {

    TagService tagService;

    TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @RequestMapping("/tags")
    public String tags(Model model) {
        //第一个查询
        //
        //sql= select tag_name,count(*) from tags group by tag_name order by count(*) desc limit 10;
//        List<Tag> tagList=tagService.getTop10Tags();
//        model.addAttribute("tagList",tagList);

        //2
        //sql= select tag_name,count(*) from tags group by tag_name order by count(*) desc limit 10;
//        List<Tag> tagList=tagService.getTop10Tags();
//        model.addAttribute("tagList",tagList);

        //3
        //sql= select tag_name,count(*) from tags group by tag_name order by count(*) desc limit 10;
//        List<Tag> tagList=tagService.getTop10Tags();
//        model.addAttribute("tagList",tagList);

        return "tags";
    }

}
