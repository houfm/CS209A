package cse.java2.project.repository;

import cse.java2.project.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findAllByTag(String tag);

    //List<Tag> getTop10Tags();
}
