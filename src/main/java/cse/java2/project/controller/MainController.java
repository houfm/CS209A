package cse.java2.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * This method is called when the user requests the root URL ("/") or "/demo".
     * In this demo, you can visit localhost:9090 or localhost:9090/demo to see the result.
     * @return the name of the view to be rendered
     * You can find the static HTML file in src/main/resources/templates/demo1.html
     */
    //TODO: 这个部分应该是用户交互主界面，从这个界面进入其他界面
    // （Number of Answers，Accepted Answers，Tags，Users）
    // 每一个分页面都创建一个对应的Controller
    // Controller尽量不执行实际的数据处理（其实也可以处理，不过分的开一点总是好的）
    // Service进行数据处理，Repository进行数据库操作
    // Controller调用Service，Service调用Repository这样一个层级关系
    // 每个Service，Controller，Repository都不是必须的，你不一定全都用上
    @GetMapping({"/", "/index","/main"})
    public String index(Model model) {
        model.addAttribute("button1Text", "Number of Answers");
        model.addAttribute("button2Text", "Accepted Answers");
        model.addAttribute("button3Text", "Tags");
        model.addAttribute("button4Text", "Users");
        model.addAttribute("button1Action", "/numberOfAnswers");
        model.addAttribute("button2Action", "/acceptedAnswers");
        model.addAttribute("button3Action", "/tags");
        model.addAttribute("button4Action", "/users");
        return "index";
    }

    @GetMapping({"/test"})
    public String test() {
        return "grapic";
    }

    @GetMapping({"/demo1"})
    public String demo1() {
        return "demo1";
    }
}
