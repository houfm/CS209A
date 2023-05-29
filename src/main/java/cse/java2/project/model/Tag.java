package cse.java2.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tag")
@Data
public class Tag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String tag;

    @JsonIgnore
    @ManyToMany(mappedBy = "tagList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questionList;

    @Transient
    public Integer getQuestionCnt() {
        return questionList.size();
    }

    @Transient
    public Integer getScore() {
        return questionList.stream()
                .map(Question::getTotalScore)
                .reduce(0, Integer::sum);
    }

    @Transient
    public Integer getViewCnt() {
        return questionList.stream()
                .map(Question::getViewCount)
                .reduce(0, Integer::sum);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(tagId, tag1.tagId) && Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tag);
    }

}
