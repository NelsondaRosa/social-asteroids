package com.ndr.socialasteroids.presentation.payload.response;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.ndr.socialasteroids.presentation.controller.ForumController;
import com.ndr.socialasteroids.presentation.controller.MatchController;
import com.ndr.socialasteroids.presentation.controller.UserController;
import com.ndr.socialasteroids.security.comunication.AuthController;

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
        Pageable pageable = PageRequest.ofSize(20);
        Link login = Link.of(buildBaseURL() + "/auth/login", "login");
        Link signup = Link.of(buildBaseURL() + "/auth/signup", "signup");
        Link searchUser = Link.of(buildBaseURL() + "/user/search/", "searchUser");
        Link getUser = Link.of(buildBaseURL() + "/user/{userId}", "getUser");
        
        add(login);
        add(signup);
        add(getUser);
        add(searchUser);
        add(linkTo(methodOn(AuthController.class).logout()).withRel("logout"));
        add(linkTo(methodOn(UserController.class).getActiveUser()).withRel("activeUser"));
        add(linkTo(methodOn(ForumController.class).getPaged(pageable)).withRel("threads"));
        add(linkTo(methodOn(MatchController.class).getPaged(pageable)).withRel("matches"));
    }    

    private String buildBaseURL()
    {
        return scheme + subdomain + domain + topdomain + endpoint; 
    }

}
