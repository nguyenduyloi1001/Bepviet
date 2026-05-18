package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.TagRequest;
import com.example.Bep.Viet.response.TagResponse;

import java.util.List;

public interface TagService {

    TagResponse create(TagRequest request);

    List<TagResponse> getAll();

    TagResponse getBySlug(String slug);

    void delete(Long id);
}