package cse.java2.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "answer")
@Data
public class Answer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "answer_id")
    private Long answerId;
    
    @Column(name = "last_activity_date")
    private Timestamp lastActivityDate;
    
    @Column(name = "last_edit_date")
    private Timestamp lastEditDate;
    
    @Column(name = "creation_date")
    private Timestamp creationDate;
    
    @Column(name = "score")
    private int score;
    
    @Column(name = "is_accepted")
    private boolean isAccepted;
    
    @Column(name = "content_license")
    private String contentLicense;
    
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    @Column(name = "body")
    private String body;
    
    @Column(name = "account_id")
    private Long accountId;

    @JsonIgnore
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return answerId == answer.answerId && score == answer.score && isAccepted == answer.isAccepted && questionId == answer.questionId && accountId == answer.accountId && Objects.equals(lastActivityDate, answer.lastActivityDate) && Objects.equals(lastEditDate, answer.lastEditDate) && Objects.equals(creationDate, answer.creationDate) && Objects.equals(contentLicense, answer.contentLicense) && Objects.equals(body, answer.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId, lastActivityDate, lastEditDate, creationDate, score, isAccepted, contentLicense, questionId, body, accountId);
    }
}
