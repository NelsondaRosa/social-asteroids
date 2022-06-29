package com.ndr.socialasteroids.domain.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
//TODO: editedOn
@Data @Entity @Table(name = "posts") @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User owner;

    @ManyToOne(optional = false)
    private ForumThread thread;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "posted_on", nullable = false)
    private Instant postedOn;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Post(){}

    public Post(User owner, ForumThread thread, String content)
    {
        this.owner = owner;
        this.thread = thread;
        this.content = content;
        this.deleted = false;
        this.postedOn = Instant.now();
    }
}
