package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.request.TagRequest;
import com.example.Bep.Viet.response.TagResponse;
import com.example.Bep.Viet.model.Tags;
import com.example.Bep.Viet.repository.TagRepository;
import com.example.Bep.Viet.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagResponse create(TagRequest request) {

        if (tagRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tag already exists");
        }

        Tags tag = Tags.builder()
                .name(request.getName())
                .build();

        Tags saved = tagRepository.save(tag);

        return mapToResponse(saved);
    }

    @Override
    public List<TagResponse> getAll() {
        return tagRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TagResponse getBySlug(String slug) {

        Tags tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        return mapToResponse(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    private TagResponse mapToResponse(Tags tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .slug(tag.getSlug())
                .build();
    }
}