package com.ndr.socialasteroids.domain.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;

    @Column(name = "author_id", insertable = false, updatable = false)
    private Long authorId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ForumThread thread;

    @Column(name = "thread_id", insertable = false, updatable = false)
    private Long threadId;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "posted_at", nullable = false)
    private Instant postedAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Post(){}

    public Post(User author, ForumThread thread, String content)
    {
        this.author = author;
        this.thread = thread;
        this.content = content;
        this.deleted = false;
        this.postedAt = Instant.now();
    }
}
