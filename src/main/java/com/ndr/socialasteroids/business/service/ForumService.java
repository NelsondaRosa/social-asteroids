package com.ndr.socialasteroids.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndr.socialasteroids.business.DTOs.PostDTO;
import com.ndr.socialasteroids.business.DTOs.ThreadDTO;
import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.domain.entity.Post;
import com.ndr.socialasteroids.domain.entity.User;
import com.ndr.socialasteroids.infra.data.repository.ForumThreadRepository;
import com.ndr.socialasteroids.infra.data.repository.PostRespository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
//Change all find to get if not throwing custom exception
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForumService
{
    private final @NonNull ForumThreadRepository threadRepository;
    private final @NonNull PostRespository postRepository;
    private final @NonNull UserService userService;

    public ThreadDTO createThread(Long ownerId, String title)
    {
        User owner = userService.getEntityById(ownerId);
        ForumThread thread = new ForumThread(title, owner);

        ThreadDTO createdThread = new ThreadDTO(threadRepository.saveAndFlush(thread));

        return createdThread;
    }
    
    public PostDTO createPost(Long ownerId, Long threadId, String content)
    {
        User owner = userService.getEntityById(ownerId);
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();
        Post post = new Post(owner, thread, content);

        PostDTO createdPost = new PostDTO(postRepository.saveAndFlush(post));

        return createdPost;
    }

    public ThreadDTO getThread(Long threadId)
    {
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();
        ThreadDTO threadDTO = new ThreadDTO(thread);

        return threadDTO;
    }

    public PostDTO editPost(Long postId, String content)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setContent(content);
        
        Post editedPost = postRepository.saveAndFlush(post);
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
    public List<ThreadDTO> getThreadsByOwner(Long ownerId)
    {
        User owner = userService.getEntityById(ownerId);
        List<ThreadDTO> threads = new ArrayList<ThreadDTO>();
        
        threadRepository.findByOwner(owner).orElseThrow()
                        .stream()
                        .forEach(thread -> threads.add(new ThreadDTO(thread)));

        
        return threads;
    }

    public PostDTO getPost(Long postId)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        PostDTO postDTO = new PostDTO(post);

        return postDTO;
    }

    public List<PostDTO> getPostsByThread(Long threadId)
    {
        ForumThread thread = threadRepository.findById(threadId).orElseThrow();
        List<PostDTO> posts = new ArrayList<PostDTO>();

        postRepository.findByThread(thread).orElseThrow()
                      .stream()
                      .forEach(post -> posts.add(new PostDTO(post)));
                      
        return posts;
    }

    public List<PostDTO> getPostsByOwner(Long ownerId)
    {
        User owner = userService.getEntityById(ownerId);
        List<PostDTO> posts = new ArrayList<PostDTO>();

        postRepository.findByOwner(owner).orElseThrow()
                      .stream()
                      .forEach(post -> posts.add(new PostDTO(post)));
        
        return posts;
    }
}
