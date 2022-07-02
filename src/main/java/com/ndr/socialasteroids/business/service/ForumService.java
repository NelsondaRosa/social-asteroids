package com.ndr.socialasteroids.business.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTO.PostDTO;
import com.ndr.socialasteroids.business.DTO.ThreadDTO;
import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.domain.entity.Post;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.ForumThreadRepository;
import com.ndr.socialasteroids.infra.data.repository.PostRespository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForumService
{
    private final @NonNull ForumThreadRepository threadRepository;
    private final @NonNull PostRespository postRepository;
    private final @NonNull UserService userService;

    public ThreadDTO createThread(Long authorId, String title)
    {
        User author = userService.getEntityById(authorId);
        ForumThread thread = new ForumThread(title, author);

        ThreadDTO createdThread = new ThreadDTO(threadRepository.save(thread));

        return createdThread;
    }
    
    public PostDTO createPost(Long authorId, Long threadId, String content)
    {
        User author = userService.getEntityById(authorId);
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();
        Post post = new Post(author, thread, content);
        
        thread.setUpdatedAt(Instant.now());

        PostDTO createdPost = new PostDTO(postRepository.save(post));

        thread.incrementPostsCount();
        threadRepository.save(thread);

        return createdPost;
    }

    public ThreadDTO getThread(Long threadId)
    {
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();
        ThreadDTO threadDTO = new ThreadDTO(thread);

        return threadDTO;
    }

    public PostDTO getPost(Long postId)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        PostDTO postDTO = new PostDTO(post);

        return postDTO;
    }

    public PostDTO editPost(Long postId, String content)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setContent(content);
        
        Post editedPost = postRepository.save(post);
        PostDTO postDTO = new PostDTO(editedPost);

        return postDTO;
    }

    public void deleteThread(Long threadId)
    {
        threadRepository.deleteById(threadId);
    }

    public void deletePost(Long postId)
    {
        postRepository.deleteById(postId);
    }

    //TODO Test findByUserId
    public Page<ThreadDTO> getPagedThreadsByAuthor(Long authorId, Pageable pageable)
    {
        User author = userService.getEntityById(authorId);
        
        return threadRepository.findByAuthorOrderByUpdatedAtDesc(author, pageable)
                            .map(thread -> new ThreadDTO(thread, pageable));
    }

    public Page<PostDTO> getPagedPostsByThread(Long threadId, Pageable pageable)
    {
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();

        return postRepository.findByThreadOrderByPostedAtDesc(thread, pageable)
                            .map(post -> new PostDTO(post));
    }

    public Page<PostDTO> getPagedPostsByAuthor(Long authorId, Pageable pageable)
    {
        User author = userService.getEntityById(authorId);

        return postRepository.findByAuthorOrderByPostedAtDesc(author, pageable)
                            .map(post -> new PostDTO(post));
    }

    public Page<ThreadDTO> getPagedThreads(Pageable pageable)
    {   
        return threadRepository.findByOrderByUpdatedAtDesc(pageable)
                        .map(thread -> new ThreadDTO(thread, pageable));
    }
}
