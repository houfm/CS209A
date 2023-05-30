package cse.java2.project.utils;

import cse.java2.project.model.Tag;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Objects;

public class utils {
  public static <T> void pairToXY(List<Pair<T, Integer>> pairs, List<String> x, List<Integer> y) {
    for (Pair<T, Integer> pair : pairs) {
      if (pair.getFirst() instanceof Tag) {
        x.add(((Tag) pair.getFirst()).getTag());
      } else if (pair.getFirst() instanceof String) {
        x.add((String) pair.getFirst());
      } else if (pair.getFirst() instanceof Long) {
        x.add(Objects.toString(pair.getFirst()));
      }
      y.add(pair.getSecond());
    }

  }
}
