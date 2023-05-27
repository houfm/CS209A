package cse.java2.project.service;

import cse.java2.project.model.Tag;
import cse.java2.project.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

//    public List<Tag> getTop10Tags() {
//        return tagRepository.getTop10Tags();
//    }
}
