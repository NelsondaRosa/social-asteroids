package com.ndr.socialasteroids.domain.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ndr.socialasteroids.infra.data.converters.ZoneIdConverter;

import lombok.Data;

@Data
@Entity
@Table(name = "posts")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, 
    property = "id")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User owner;

    @ManyToOne
    private ForumThread thread;

    @Column(name = "content")
    private String content;

    @Column(name = "posted_on")
    private LocalDateTime postedOn;

    @Column(name = "time_zone_id")
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zoneId;

    public Post(){}
    
    public Post(User owner, String content)
    {
        this.owner = owner;
        this.content = content;
        setTimeNow();
    }

    public void setTimeNow()
    {
        this.postedOn = LocalDateTime.now();
        this.zoneId = ZoneId.systemDefault();
    }
}
