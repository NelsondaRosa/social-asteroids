package com.ndr.socialasteroids.business.DTOs;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.domain.entity.ForumThread;
import com.ndr.socialasteroids.presentation.controller.ForumController;
import com.ndr.socialasteroids.presentation.controller.UserController;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ThreadDTO extends RepresentationModel<ThreadDTO>
{
    private Long id;
    private String title;
    private Instant createdOn;
    private Instant updatedOn;

    public ThreadDTO(ForumThread thread)
    {
        this.id = thread.getId();
        this.title = thread.getTitle();
        this.createdOn = thread.getCreatedOn();
        this.updatedOn = thread.getUpdatedOn();
        Pageable pageable = PageRequest.of(0, 20);
        //Self - posts - owner
        add(linkTo(methodOn(ForumController.class).getThread(id.toString())).withSelfRel());
        add(linkTo(methodOn(ForumController.class).getPostsByThread(id.toString(), pageable)).withRel("posts"));
        add(linkTo(methodOn(UserController.class).getUser(thread.getOwner().getId().toString())).withRel("owner"));
    }
    
}
