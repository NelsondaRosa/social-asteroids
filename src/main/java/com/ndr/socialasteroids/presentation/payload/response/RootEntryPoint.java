package com.ndr.socialasteroids.presentation.payload.response;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.presentation.controller.ForumController;
import com.ndr.socialasteroids.presentation.controller.MatchController;

public class RootEntryPoint extends RepresentationModel<RootEntryPoint>
{
    //TODO: put in config
    private final String scheme = "http://";
    private final String subdomain = "";
    private final String domain = "localhost:8080";
    private final String topdomain = "";
    private final String endpoint = "/api";

    public RootEntryPoint()
    {
        Link login = Link.of(buildBaseURL() + "/auth/login", "login");
        Link signup = Link.of(buildBaseURL() + "/auth/signup", "signup");
        Link searchUser = Link.of(buildBaseURL() + "/user/search/", "search-user");
        //Link forum = Link.of(buildBaseURL() + "/forum/");
        add(login);
        add(signup);
        add(searchUser);
        add(linkTo(methodOn(ForumController.class)).withRel("threads"));
        add(linkTo(methodOn(MatchController.class).getAll()).withRel("matches"));
    }    

    private String buildBaseURL()
    {
        return scheme + subdomain + domain + topdomain + endpoint; 
    }

}
