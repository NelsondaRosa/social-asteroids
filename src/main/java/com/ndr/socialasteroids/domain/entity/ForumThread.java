package com.ndr.socialasteroids.domain.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ndr.socialasteroids.infra.data.converters.ZoneIdConverter;

import lombok.Data;

@Data
@Entity
@Table(name = "threads")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, 
    property = "id")
public class ForumThread
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;
    
    @OneToMany(mappedBy = "thread", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<Post>();

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "time_zone_id")
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zoneId;

    public ForumThread(){}

    public ForumThread(User owner)
    {
        this.owner = owner;
        setTimeNow();
    }

    public void setTimeNow()
    {
        this.createdOn = LocalDateTime.now();
        this.zoneId = ZoneId.systemDefault();
    }




    
}
