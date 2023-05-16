package cse.java2.project.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="question")
@Data
public class Question {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "score")
    private int score;

    @Column(name = "link")
    private String link;

    @Column(name = "answer_count")
    private int answerCount;

    @Column(name = "view_count")
    private int viewCount;
    
    @Column(name = "content_license")
    private String contentLicense;

    @Column(name = "title")
    private String title;
    
    @Column(name = "last_activity_date")
    private Timestamp lastActivityDate;
    
    @Column(name = "last_edit_date")
    private Timestamp lastEditDate;
    
    @Column(name = "creation_date")
    private Timestamp creationDate;
    
    @Column(name = "account_id")
    private Long accountId;
    
    @Column(name = "body")
    private String body;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Answer> answerList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tagList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(questionId, question.questionId) && score == question.score && answerCount == question.answerCount && viewCount == question.viewCount && accountId == question.accountId && Objects.equals(link, question.link) && Objects.equals(contentLicense, question.contentLicense) && Objects.equals(title, question.title) && Objects.equals(lastActivityDate, question.lastActivityDate) && Objects.equals(lastEditDate, question.lastEditDate) && Objects.equals(creationDate, question.creationDate) && Objects.equals(body, question.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, score, link, answerCount, viewCount, contentLicense, title, lastActivityDate, lastEditDate, creationDate, accountId, body);
    }
}

