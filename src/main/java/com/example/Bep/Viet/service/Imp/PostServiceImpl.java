package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.enums.PostStatus;
import com.example.Bep.Viet.enums.PostType;
import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.Post;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.PostRepository;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.PostRequest;
import com.example.Bep.Viet.response.PostResponse;
import com.example.Bep.Viet.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Override
    public PostResponse create(PostRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnail(request.getThumbnail())
                .postType(request.getType())
                .postStatus(PostStatus.PENDING)
                .viewCount(0)
                .build();
        return mapToResponse(postRepository.save(post));
    }

    @Override
    public PostResponse getPostById(Long id) {
        postRepository.incrementViewCount(id);
        return mapToResponse(findById(id));
    }

    @Override
    public List<PostResponse> getAllPost() {
        return postRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PostResponse> getByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return postRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PostResponse> getByStatus(PostStatus status) {
        return postRepository.findByPostStatus(status).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PostResponse> getByType(PostType type) {
        return postRepository.findByPostType(type).stream().map(this::mapToResponse).toList();
    }

    @Override
    public PostResponse update(Long id, PostRequest request, Long currentUserId) {
        Post post = findById(id);


        if (!post.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setThumbnail(request.getThumbnail());
        post.setPostType(request.getType());
        post.setPostStatus(PostStatus.PENDING);

        return mapToResponse(postRepository.save(post));
    }

    @Override
    public void delete(Long id, Long currentUserId) {
        Post post = findById(id);
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        postRepository.delete(post);
    }

    @Override
    public PostResponse approve(Long id) {
        Post post = findById(id);
        post.setPostStatus(PostStatus.PUBLISHED);
        return mapToResponse(postRepository.save(post));
    }

    @Override
    public PostResponse reject(Long id) {
        Post post = findById(id);
        post.setPostStatus(PostStatus.REJECTED);
        return mapToResponse(postRepository.save(post));
    }

    private Post findById(Long id){
        return postRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND));
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .userName(post.getUser().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnail(post.getThumbnail())
                .type(post.getPostType())
                .status(post.getPostStatus())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
