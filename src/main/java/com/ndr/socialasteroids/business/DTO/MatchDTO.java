package com.ndr.socialasteroids.business.DTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.Duration;

import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.domain.entity.Match;
import com.ndr.socialasteroids.presentation.controller.MatchController;
import com.ndr.socialasteroids.presentation.controller.UserController;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MatchDTO extends RepresentationModel<MatchDTO>
{
    private Long id;
    private Duration duration;
    private Long score;
    private Long ammoSpent;
    private Long destroyedTargets;
    
    public MatchDTO(Match match)
    {
        this.id = match.getId();
        this.duration = match.getDuration();
        this.score = match.getScore();
        this.ammoSpent = match.getAmmoSpent();
        this.destroyedTargets = match.getAmmoSpent();
        //Self - player
        add(linkTo(methodOn(MatchController.class).getMatch(match.getId().toString())).withSelfRel());
        add(linkTo(methodOn(UserController.class).getUserById(match.getPlayer().getId().toString())).withRel("player"));
    }
}
