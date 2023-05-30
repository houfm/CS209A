package cse.java2.project.service;

import cse.java2.project.model.Question;
import cse.java2.project.model.Tag;
import cse.java2.project.repository.TagRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {
  private final TagRepository tagRepository;

  TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public List<Tag> getAllTags() {
    return tagRepository.findAll();
  }

  public List<Pair<String, Integer>> getTop40FrequencyTags() {
    Pageable pageable = Pageable.ofSize(41);
    List<Tag> tags = tagRepository.findTopNByOrderByQuestionCntDesc(pageable);
    tags.remove(0);
    List<Pair<String, Integer>> pairs = new ArrayList<>();
    for (Tag tag : tags) {
      pairs.add(Pair.of(tag.getTag(), tag.getQuestionCnt()));
    }
    return pairs;
  }

  public List<Pair<Tag, Integer>> getTop20ScoreTags() {

    List<Tag> tags = getAllTags();
    List<Pair<Tag, Integer>> pair = new ArrayList<>();
    List<Pair<Tag, Integer>> pairs;
    for (Tag tag : tags) {
      pair.add(Pair.of(tag, tag.getScore()));
    }
    //降序，删除java tag
    pairs = pair.stream()
            .sorted((o1, o2) -> o2.getSecond().compareTo(o1.getSecond()))
            .limit(21)
            .filter(o -> !o.getFirst().getTag().equals("java"))
            .limit(20).toList();
    return pairs;
  }

  public List<Pair<Tag, Integer>> getTop20ViewCntTags() {
    Pageable pageable = Pageable.ofSize(21);
    List<Tag> tags = tagRepository.findTopNByOrderByViewCntDesc(pageable);
    //删除java tag
    List<Pair<Tag, Integer>> pair = new ArrayList<>();
    List<Pair<Tag, Integer>> pairs;
    for (Tag tag : tags) {
      pair.add(Pair.of(tag, tag.getViewCnt()));
    }

    pairs = pair.stream()
            .sorted((o1, o2) -> o2.getSecond().compareTo(o1.getSecond()))
            .limit(21)
            .filter(o -> !o.getFirst().getTag().equals("java"))
            .limit(20).toList();

    return pairs;
  }

  public int getScoreByQuestionList(List<Question> questions) {
    int score = 0;
    for (Question question : questions) {
      score += question.getScore();
    }
    return score;
  }

  public int getViewCntByQuestionList(List<Question> questions) {
    int viewCnt = 0;
    for (Question question : questions) {
      viewCnt += question.getViewCount();
    }
    return viewCnt;
  }

  /**
   * @param num number of tag combination member
   * @return top 10 tag combinations with the highest score
   */
  public List<Pair<String, Integer>> getTop20ScoreTagCombinations(int num) {
    List<Tag> tags = tagRepository.findAll();
    Map<Tag, List<Question>> questionListLists = new HashMap<>();
    Map<String, Integer> tagScore = new HashMap<>();
    for (Tag tag : tags) {
      questionListLists.put(tag, tag.getQuestionList());
    }
    for (int i = 0; i < tags.size(); i++) {
      Tag tag1 = tags.get(i);
      if (Objects.equals(tag1.getTag(), "java")) continue;
      for (int j = i + 1; j < tags.size(); j++) {
        Tag tag2 = tags.get(j);
        if (tag1 == tag2 || Objects.equals(tag2.getTag(), "java")) continue;
        List<Question> questions = new ArrayList<>(questionListLists.get(tag1));
        questions.retainAll(questionListLists.get(tag2));
        if (num == 3) {
          for (int k = j + 1; k < tags.size(); k++) {
            Tag tag3 = tags.get(k);
            if (tag1 == tag3 || tag2 == tag3 || Objects.equals(tag3.getTag(), "java")) continue;
            questions.retainAll(questionListLists.get(tag3));
            tagScore.put(tag1.getTag() + "&" + tag2.getTag() + "&" + tag3.getTag(), getScoreByQuestionList(questions));
          }
        } else {
          tagScore.put(tag1.getTag() + "&" + tag2.getTag(), getScoreByQuestionList(questions));
        }

      }
    }
    List<Map.Entry<String, Integer>> list = new ArrayList<>(tagScore.entrySet());
    //降序
    list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    List<Pair<String, Integer>> pairs = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      pairs.add(Pair.of(list.get(i).getKey(), list.get(i).getValue()));
    }
    return pairs;
  }

  public List<Pair<String, Integer>> getTop20ViewCntTagCombinations(int num) {
    List<Tag> tags = tagRepository.findAll();
    Map<Tag, List<Question>> questionListLists = new HashMap<>();
    Map<String, Integer> tagScore = new HashMap<>();
    for (Tag tag : tags) {
      questionListLists.put(tag, tag.getQuestionList());
    }
    for (int i = 0; i < tags.size(); i++) {
      Tag tag1 = tags.get(i);
      if (Objects.equals(tag1.getTag(), "java")) continue;
      for (int j = i + 1; j < tags.size(); j++) {
        Tag tag2 = tags.get(j);
        if (tag1 == tag2 || Objects.equals(tag2.getTag(), "java")) continue;
        List<Question> questions = new ArrayList<>(questionListLists.get(tag1));
        questions.retainAll(questionListLists.get(tag2));
        if (num == 3) {
          for (int k = j + 1; k < tags.size(); k++) {
            Tag tag3 = tags.get(k);
            if (tag1 == tag3 || tag2 == tag3 || Objects.equals(tag3.getTag(), "java")) continue;
            questions.retainAll(questionListLists.get(tag3));
            tagScore.put(tag1.getTag() + "&" + tag2.getTag() + "&" + tag3.getTag(), getViewCntByQuestionList(questions));
          }
        } else {
          tagScore.put(tag1.getTag() + "&" + tag2.getTag(), getViewCntByQuestionList(questions));
        }

      }
    }
    List<Map.Entry<String, Integer>> list = new ArrayList<>(tagScore.entrySet());
    //降序
    list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    List<Pair<String, Integer>> pairs = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      pairs.add(Pair.of(list.get(i).getKey(), list.get(i).getValue()));
    }
    return pairs;
  }


}
