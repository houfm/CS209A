package cse.java2.project.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class AnswerDeprecated {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionDeprecated question;
}
