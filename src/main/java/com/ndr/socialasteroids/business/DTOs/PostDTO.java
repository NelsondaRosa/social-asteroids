package com.ndr.socialasteroids.business.DTOs;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Instant;

import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.domain.entity.Post;
import com.ndr.socialasteroids.presentation.controller.ForumController;
import com.ndr.socialasteroids.presentation.controller.UserController;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostDTO extends RepresentationModel<PostDTO>
{
    private Long id;
    private String content;
    private Instant postedOn;
    
    public PostDTO(Post post)
    {
        this.id = post.getId();
        this.content = post.getContent();
        this.postedOn = post.getPostedOn();
        //Setf - Thread - Owner
        add(linkTo(methodOn(ForumController.class).getPost(id.toString())).withSelfRel());
        add(linkTo(methodOn(ForumController.class).getThread(post.getThread().toString())).withRel("thread"));
        add(linkTo(methodOn(UserController.class).getUser(post.getOwner().toString())).withRel("owner"));
    }
}
