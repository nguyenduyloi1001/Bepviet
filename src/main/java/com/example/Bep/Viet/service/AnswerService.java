package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.AnswerRequest;
import com.example.Bep.Viet.response.AnswerResponse;

public interface AnswerService {
    AnswerResponse create(Long questionId, AnswerRequest request, Long adminId);
    AnswerResponse update(Long answerId, AnswerRequest request, Long currentUserId);
    void delete(Long answerId, Long currentUserId);
}
