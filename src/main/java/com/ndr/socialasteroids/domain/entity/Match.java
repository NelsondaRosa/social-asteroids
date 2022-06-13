package com.ndr.socialasteroids.domain.entity;

import java.time.Duration;
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
import com.ndr.socialasteroids.infra.data.converters.DurationConverter;
import com.ndr.socialasteroids.infra.data.converters.ZoneIdConverter;

import lombok.Data;

@Data
@Entity
@Table(name = "matches")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, 
    property = "id")
public class Match
{  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_on", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime matchOn;

    @Column(name = "time_zone_id", nullable = false)
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId zoneId;

    @Column(name = "duration", nullable = false)
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @Column(name = "score", nullable = false)
    private Long score;

    @Column(name = "ammo_spent", nullable = false)
    private Long ammoSpent;

    @Column(name = "destroyed_targets", nullable = false)
    private Long destroyedTargets;

    @ManyToOne
    private User player;

    public Match(){}

    public Match(Long duration, Long score, Long ammoSpent, Long destroyedTargets)
    {
        this.duration = Duration.ofMillis(duration);
        this.score = score;
        this.ammoSpent = ammoSpent;
        this.destroyedTargets = destroyedTargets;
        setTimeNow();
    }

    public void setTimeNow()
    {
        this.matchOn = LocalDateTime.now();
        this.zoneId = ZoneId.systemDefault();
    }
}
