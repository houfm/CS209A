package cse.java2.project.controller;

import cse.java2.project.model.Answer;
import cse.java2.project.model.Question;
import cse.java2.project.service.AnswerService;
import cse.java2.project.service.QuestionService;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1")
public class RestController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    public RestController(QuestionService questionService,
                          AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(@RequestParam(value = "tag")
                                       List<String> tags) {
        if(tags.size() == 0) {
            return ResponseEntity.badRequest().build();
        }
        List<Question> questions = questionService.getQuestionUnionByTags(tags);
        int sum = questions.size();
        Pair<Integer, List<Question>> pair = Pair.of(sum, questions);
        return ResponseEntity.ok(pair);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<?> getQuestions(@PathVariable("questionId") Long questionId) {
        Optional<Question> question = questionService.getQuestion(questionId);
        if (question.isPresent()) {
            return ResponseEntity.ok(question.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/answers")
    public ResponseEntity<?> getAnswers(@RequestParam("questionId") Long questionId) {
        Optional<Question> question = questionService.getQuestion(questionId);
        if (question.isPresent()) {
            return ResponseEntity.ok(question.get().getAnswerList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@RequestParam("answerId") Long answerId) {
        Optional<Answer> answer = answerService.getAnswer(answerId);
        if (answer.isPresent()) {
            return ResponseEntity.ok(answer.get().getCommentList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
