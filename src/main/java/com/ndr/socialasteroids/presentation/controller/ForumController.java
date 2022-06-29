package com.ndr.socialasteroids.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ndr.socialasteroids.business.DTOs.PostDTO;
import com.ndr.socialasteroids.business.DTOs.ThreadDTO;
import com.ndr.socialasteroids.business.service.ForumService;
import com.ndr.socialasteroids.presentation.payload.request.forum.CreatePostRequest;
import com.ndr.socialasteroids.presentation.payload.request.forum.CreateThreadRequest;
import com.ndr.socialasteroids.presentation.payload.request.forum.DeleteRequest;
import com.ndr.socialasteroids.presentation.payload.request.forum.EditPostRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/forum") @RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForumController
{
    private final @NonNull ForumService forumService;

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable)
    {
        Page<ThreadDTO> threads = forumService.getPagedThreads(pageable);

        return ResponseEntity.ok().body(threads);
    }

    @PostMapping(path = "/create") @PreAuthorize("#thread.getOwnerId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> createThread(@P("thread") @RequestBody CreateThreadRequest request)
    {
        ThreadDTO thread = forumService.createThread(request.getOwnerId(), request.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(thread);
    }

    @PostMapping(path = "/createPost") @PreAuthorize("post.getOwnerId == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> createPost(@P("post") @RequestBody CreatePostRequest request)
    {
        PostDTO post = forumService.createPost(request.getOwnerId(), request.getThreadId(), request.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping(path = "/post/edit")
    @PreAuthorize("post.getOwnerId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> editPost(@P("post") @RequestBody EditPostRequest request)
    {
        PostDTO post = forumService.editPost(request.getPostId(), request.getContent());

        return ResponseEntity.ok().body(post);
    }

    @DeleteMapping(path = "/thread/delete")
    @PreAuthorize("thread.getOwnerId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> deleteThread(@P("thread") @RequestBody DeleteRequest request)
    {
        forumService.deleteThread(request.getEntityId());

        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("path = /post/delete")
    @PreAuthorize("post.getOwnerId() == principal.getUserSecurityInfo().getId()")
    public ResponseEntity<?> deletePost(@P("post") @RequestBody DeleteRequest request)
    {
        forumService.deletePost(request.getEntityId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/thread/get/{threadId}")
    public ResponseEntity<?> getThread(@PathVariable String threadId)
    {
        ThreadDTO thread = forumService.getThread(Long.valueOf(threadId));

        return ResponseEntity.ok().body(thread);
    }

    @GetMapping(path = "/thread/user/{ownerId}")
    public ResponseEntity<?> getThreadsByOwner(@PathVariable String ownerId, Pageable pageable)
    {
        Page<ThreadDTO> threads = forumService.getPagedThreadsByOwner(Long.valueOf(ownerId), pageable);
        
        return ResponseEntity.ok().body(threads);
    }

    @GetMapping(path = "/post/get/{postId}")
    public ResponseEntity<?> getPost(@PathVariable String postId)
    {
        PostDTO post = forumService.getPost(Long.valueOf(postId));

        return ResponseEntity.ok().body(post);
    }

    @GetMapping(path = "/post/thread/{threadId}")
    public ResponseEntity<?> getPostsByThread(@PathVariable String threadId, Pageable pageable)
    {
        Page<PostDTO> posts = forumService.getPagedPostsByThread(Long.valueOf(threadId), pageable);

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping(path="/post/user/{onwerId}")
    public ResponseEntity<?> getPostsByOwner(@PathVariable String onwerId, Pageable pageable)
    {
        Page<PostDTO> posts = forumService.getPagedPostsByOwner(Long.valueOf(onwerId), pageable);

        return ResponseEntity.ok().body(posts);
    }
}
