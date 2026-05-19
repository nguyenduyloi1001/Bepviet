package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.Answer;
import com.example.Bep.Viet.model.Question;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.AnswerRepository;
import com.example.Bep.Viet.repository.QuestionRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.AnswerRequest;
import com.example.Bep.Viet.response.AnswerResponse;
import com.example.Bep.Viet.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository repository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AnswerResponse create(Long questionId, AnswerRequest request, Long adminId) {
        if (repository.existsByQuestionId(questionId)) {
            throw new AppException(ErrorCode.ANSWER_ALREADY_EXISTS);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Answer answer = Answer.builder()
                .question(question)
                .user(admin)
                .content(request.getContent())
                .build();

        return mapToResponse(repository.save(answer));
    }

    @Override
    @Transactional
    public AnswerResponse update(Long answerId, AnswerRequest request, Long currentUserId) {
        Answer answer = findById(answerId);

        if (!answer.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.ANSWER_FORBIDDEN);
        }

        answer.setContent(request.getContent());
        return mapToResponse(repository.save(answer));
    }

    @Override
    @Transactional
    public void delete(Long answerId, Long currentUserId) {
        Answer answer = findById(answerId);

        if (!answer.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.ANSWER_FORBIDDEN);
        }

        repository.delete(answer);
    }

    // helper
    private Answer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
    }

    private AnswerResponse mapToResponse(Answer a) {
        return AnswerResponse.builder()
                .id(a.getId())
                .questionId(a.getQuestion().getId())
                .userId(a.getUser().getId())
                .userName(a.getUser().getUsername())
                .content(a.getContent())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
