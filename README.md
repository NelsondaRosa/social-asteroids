# Social Asteroids

Social Asteroids is a place to play the classic Asteroids game while you share your scores, meet friends and discuss on the forum.

The purpose of the project is to serve as a portfolio to demonstrate my capabilities with the Spring ecosystem in Java. **DDD** architecture concepts are implemented, as well as the criteria of a **REST** application. I also want it to serve as a reference for those who need content for the technologies used.

##### Table of Contents  
- [Running](#running)
 - [Architecture](#architecture)   
 - [Endpoints](#endpoints)
 - [Errors](#errors)
 - [Tests](#tests)

<a name="running"/>

## Running

To run the application you need to have installed on your machine:

 - [Maven](https://maven.apache.org/download.cgi)
 - [Java JDK 11](https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html) or any [OpenJDK 11](https://www.openlogic.com/openjdk-downloads) version
 
To run, clone or download the project from this page. Go to your root page and run the commands

    mvn dependency:resolve
    mvn spring-boot:run

#### Frontend
There is a template frontend application for functional testing (don't expect to find anything pretty there) under development:
[Social Asteroids Frontend](https://github.com/nelsondrp/social-asteroids-frontend)

To run the frontend, install [NodeJS](https://nodejs.org/en/download/) and [Nodemon](https://www.npmjs.com/package/nodemon). 
In the root folder of the project run the following commands

    npm init
    nodemon index.js

<a name="architecture"/>

## Architecture

In the application a simple layered architecture for a *monolith* was applied.
<div align = "center">
<img src="https://raw.githubusercontent.com/nelsondrp/social-asteroids-backend/main/diagram.svg">
</div>
 
 #### Domain
In the domain is the heart of the business, the entities that contain the data necessary for all operations and give purpose to the application, as well as their mappings are found here.

#### Security
The security layer contains its own entities, repositories, services and controllers. As it's not necessarily part of the application's main context, a layer was created especially for it.

#### Infrastructure
The infrastructure layer is responsible for the application backbone. Repositories, request interceptors, and error handling are its responsibility.

#### Business
All the application's logic is concentrated in the business layer, it is also responsible for handling the data that arrives and leaves it.

#### Presentation
The presentation layer receives the data from *requests*, forwards them for processing and receives the data that should be forwarded to the *response*.
<br>
### Stateless
Starting from the main principle of a REST application, this API is stateless, that is, it doesn't stores any client state on the server. Each request is independent and needs to contain everything necessary to access the intended resources. Thinking about it, the security layer uses ***J**son **W**eb **T**okens* for authentication.

#### JWT
It allows the user to navigate between server requests without having to authenticate at each request, as well as maintaining the Stateless server's aspect.

After the user is logged in, a token is created on the server with the user data and encrypted with a key. This token has a short validity time, and is sent to the user through a cookie. 

Along with the JWT, a Refresh Token is created, it's stored in a database table and has a longer validity time. Every time a expired JWT is sent, the server will capture the Refresh Token from its cookie, which is also encrypted with another secret, and perform its validation. Having passed the validation, a new JWT is generated and his cookie value is updated. When the Refresh Token expires the user will need to authenticate with their credentials again.

With the Refresh Token, if the user's JWT is spoofed, the attacker will have a short period of authentication time.

#### HATEOAS
Just as JWT is important to keep the server stateless, HATEOAS is an important feature for decoupling the backend from the frontend.

With HATEOAS there is no need for the client to know the paths used by the server to request resources beyond the Entry Point. Every resource will return with it the information needed to access related resources.

<a name="endpoints"/>

## Endpoints
#### Response Entities:

    User :
	    Long : id
	    String : username
	    String : email
	    Hypermedia Link, User : self - api/user/{id}
	    Hypermedia Link, List<Match> : matches - api/match/player/{id}
	    Hypermedia Link, List<Friendship> : friends - api/friend/get/{id}
	    Hypermedia Link, List<Thread> : threads - api/forum/threads-author/{id}
	    Hypermedia Link, List<Post> : posts - api/forum/posts-author/{id}	  
<br>

    Match:
		Long : id
		Duration : duration
		Long : score
		Long : ammoSpent
		Long : destroyedTargets
		Hypermedia Link, Match : self - api/match/{id}
		Hypermedia Link, User : player - api/user/{id}
<br>

    Friendship:
		 Hypermedia Link, User : user - api/user/{id}
		 Hypermedia Link, User : friend - api/user/{id}
<br>

    Thread:
	    Long : id
	    String : title
	    String : authorName
	    Instant : createdAt
	    Instant : updatedAt
	    Long : postsCount
	    Hypermedia Link, Thread : self - api/forum/thread/{id}
	    Hypermedia Link, List<Post> : posts - api/forum/posts-thread/{id}
	    Hypermedia Link, User : author - api/user/{id}
	    Hypermedia Link, List<Thread> : threads - api/forum/{id}
<br>

    Post:
	    Long : id
	    String : content
	    Instant : postedAt
	    Instant : editedAt
	    Hypermedia Link, Post : self - api/forum/post/{id}
	    Hypermedia Link, Thread : thread - api/forum/thread/{id}
	    Hypermedia Link, User : author - api/user/{id}
	    
<hr/>

## Home
|Request URI|Method|
|--|--|
|api/home|GET|
    Response: 200 OK
	    Payload:
	    	    Hypermedia Link, login : api/auth/login
		    Hypermedia Link, signup : api/auth/signup
		    Hypermedia Link, searchUser : api/user/search
		    Hypermedia Link, getUser : api/user
		    Hypermedia Link, logout : api/auth/logout
		    Hypermedia Link, activeUser : api/user/active
		    Hypermedia Link, threads : api/forum
		    Hypermedia Link, matches : api/match
<hr/>

## Authentication
#### Signup
|Request URI|Method|
|--|--|
|api/auth/signup|POST|
    Request:
	    Payload:
	    	String : username,
	    	String : email,
	    	String : password
    ------------------------------------------		 
    Response: 201 CREATED
	    Payload:
		    User : user
<hr/>

#### Login
|Request URI|Method|
|--|--|
|api/auth/login|POST|
    Request:
	    Payload:
	    	String : username,
	    	String : password
    ------------------------------------------		 
    Response: 200 OK
	    Headers:
		    Cookie : auth,
		    Cookie : refresh-token
		Payload:
		    User : user
<hr/>

#### Logout
|Request URI|Method|
|--|--|
|api/auth/logout|GET|
    Response: 200 OK
	    Headers:
		    Cookie : auth,
		    Cookie : refresh-token
<hr/>

## User 
#### Active
|Request URI|Method|
|--|--|
|api/user/active|GET|
    Response: 200 OK
	    Payload:
		    User : user
	------------------------------------------	    
	Response: 204 NO CONTENT
<hr/>

#### Update
|Request URI|Method|
|--|--|
|api/user/update|POST|
    Request:
	    Payload:
	    	Long  : id,
	    	String : username,
	    	String : email
	------------------------------------------	     	
    Response: 200 OK
		Payload:
		    User : user
<hr/>

#### Update Password
|Request URI|Method|
|--|--|
|api/user/update-password|POST|
    Request:
	    Payload:
	    	Long  : id,
	    	String : actualPassword,
	    	String : newPassword
	------------------------------------------		     
    Response: 200 OK
		Payload:
		    User : user
<hr/>

#### Get User
|Request URI|Method|
|--|--|
|api/user/{id}|GET|
    Request:
	    Path variable:
			User ID
	------------------------------------------		     
    Response: 200 OK
		Payload:
		    User : user
<hr/>

#### Search
|Request URI|Method|
|--|--|
|api/user/{search}|GET|
    Request:
	    Path variable:
			Search Query
	------------------------------------------		     
    Response: 200 OK
		Payload:
		    List<User> : users
    ------------------------------------------	    
	Response: 204 NO CONTENT
<hr/>

## Match
#### Add
|Request URI|Method|
|--|--|
|api/match/add|POST|
    Request:
	    Payload:
		    Long : durationInMilis,
		    Long : score,
		    Long : ammoSpent,
		    Long : destroyedTargets,
		    Long : playerId
	------------------------------------------		     
    Response: 201 CREATED
	    Payload:
		    Match : match    
<hr/>

#### Get Matches by Player
|Request URI|Method|Details|
|--|--|--|
|api/match/player/{id}|GET|Pageable|
    Request:
	    Path Variable:
		    User ID
	------------------------------------------		     
    Response: 200 OK
	    Payload:
		    Page<Match> : matches
<hr/>

#### Get Match
|Request URI|Method|
|--|--|
|api/match/{id}|GET
    Request:
	    Path Variable:
		    Match ID
	------------------------------------------		     
    Response: 200 OK
	    Payload:
		    Match : match
<hr/>

## Friendship
#### Invite
|Request URI|Method|
|--|--|
|api/friend/send-invite|POST|
    Request:
	    Payload:
		    Long : userId,
		    Long: friendId
	------------------------------------------		     
    Response: 201 CREATED    
<hr/>

#### Answer Invite
|Request URI|Method|
|--|--|
|api/friend/answer-invite|POST|
    Request:
	    Payload:
		    Long : userId,
		    Long: friendId,
		    boolean : accepted
	------------------------------------------		     
    Response: 201 CREATED
	    Payload:
		    User : inviter
<hr/>

#### Unfriend
|Request URI|Method|
|--|--|
|api/friend/unfriend|DELETE|
    Request:
	    Payload:
		    Long : userId,
		    Long: friendId
	------------------------------------------		     
    Response: 204 NO CONTENT
<hr/>
    
 #### Undo Request
|Request URI|Method|
|--|--|
|api/friend/undo-request|DELETE|
    Request:
	    Payload:
		    Long : userId,
		    Long: friendId
	------------------------------------------		     
    Response: 204 NO CONTENT
<hr/>

 #### Get Friends
|Request URI|Method|
|--|--|
|api/friend/{id}|GET|
    Request:
	    Path Variable:
		    User ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			List<Friendship> : friends
	------------------------------------------		     
    Response: 204 NO CONTENT
<hr/>

#### Get Invites
|Request URI|Method|
|--|--|
|api/friend/invites/{id}|GET|
    Request:
	    Path Variable:
		    User ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			List<Friendship> : friendInvites
	------------------------------------------		     
    Response: 204 NO CONTENT
<hr/>

## Forum
#### Get threads
|Request URI|Method|Details|
|--|--|--|
|api/forum|GET|Pageable|
    Response: 200 OK
	    Payload:
		    Page<Thread> : threads
<hr/>

#### Create Thread
|Request URI|Method|
|--|--|
|api/forum/create-thread|POST|
    Request:
	    Payload:
		    Long : authorId,
		    String : title
	------------------------------------------		
	Response: 201 CREATED
		Payload:
			Thread : thread
<hr/>


#### Create Post
|Request URI|Method|
|--|--|
|api/forum/create-post|POST|
    Request:
	    Payload:
		    Long : authorId,
		    Long : threadId,
		    String : content
	------------------------------------------		
	Response: 201 CREATED
		Payload:
			Post : post
<hr/>

#### Edit Post
|Request URI|Method|
|--|--|
|api/forum/edit-post|POST|
    Request:
	    Payload:
		    Long : postId,
		    Long : threadId,
		    String : content
	------------------------------------------		
	Response: 200 OK
		Payload:
			Post : post
<hr/>

#### Delete Thread
|Request URI|Method|
|--|--|
|api/forum/delete-thread|DELETE|
    Request:
	    Payload:
		    Long : authorId,
		    Long : entityId,
	------------------------------------------		
	Response: 204 NO CONTENT
<hr/>

#### Delete Post
|Request URI|Method|
|--|--|
|api/forum/delete-Post|DELETE|
    Request:
	    Payload:
		    Long : authorId,
		    Long : entityId,
	------------------------------------------		
	Response: 204 NO CONTENT
<hr/>

#### Get Thread
|Request URI|Method|
|--|--|
|api/forum/thread/{id}|GET|
    Request:
	    Path Variable:
		    Thread ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			Thread : thread
<hr/>


#### Get Threads by Author
|Request URI|Method|Details|
|--|--|--|
|api/forum/threads-author/{id}|GET|Pageable
    Request:
	    Path Variable:
		    Author ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			Page<Thread> : threads
<hr/>

#### Get Post
|Request URI|Method|
|--|--|
|api/forum/post/{id}|GET|
    Request:
	    Path Variable:
		    Post ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			Post : post
<hr/>

#### Get Posts by Thread
|Request URI|Method|Details
|--|--|--|
|api/forum/posts-thread/{id}|GET|Pageable
    Request:
	    Path Variable:
		    Thread ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			Page<Post> : post
<hr/>

#### Get Posts by Author
|Request URI|Method|Details
|--|--|--|
|api/forum/posts-author/{id}|GET|Pageable
    Request:
	    Path Variable:
		    Author ID
	------------------------------------------		
	Response: 200 OK
		Payload:
			Page<Post> : post
<hr/>

<a name="errors"/>

## Errors
#### Data Inconsistency
Happens when there is an attempt to create data that generates inconsistency in the database

	Response: 409 CONFLICT
			Payload:
				String : message
<hr/>

#### Duplicate Value
Happens when there is an attempt to create replicated data in a unique data field

	Response: 409 CONFLICT
			Payload:
				String : message
<hr/>

#### JWT
Happens when a JWT related issue happens

	Response: 401 UNAUTHORIZED
			Payload:
				String : message
<hr/>

#### Refresh Token
Happens when a Refresh Token related issue happens

	Response: 401 UNAUTHORIZED
			Payload:
				String : message
<hr/>

#### Encoder
Happens when encoder fails to encrypt or decrypt data

	Response: 500 INTERNAL SERVER ERROR
			Payload:
				String : message
<hr/>

#### Method Argument Not Valid
Happens when the data sent is not valid

	Response: 400 BAD REQUEST
			Payload:
				Map<String, String> : argument, errorMessage
<hr/>

#### Entity Not Found
Happens when there is a request for an entity that does not exist

	Response: 404 NOT FOUND
		Payload:
			message : Data can't be found
<hr/>

#### Illegal Agument
Happens when trying to make a request with arguments not compatible with those in need

	Response: 400 BAD REQUEST
		Payload:
			message : Sent data is incorrect
<hr/>

#### Access Denied
Happens when there is an attempt to access a resource by a disallowed user

	Response: 401 UNAUTHORIZED
		Payload:
			message : Access Denied for this operation
<hr/>

#### Username not found
Happens when there is a login attempt with non-existent username

	Response: 404 NOT FOUND
		Payload:
			message : Wrong login credentials
<hr/>

#### Authentication
Happens when there is an unhandled issue related to authentication

	Response: 400 BAD REQUEST
		Payload:
			message : Authentication failed
<hr/>

#### Error
Happens when a problem not handled by the backend application occurs.

	Response: 500 INTERNAL SERVER ERROR
			Payload:
				message : Unexpected error

<a name="tests"/>

## Tests

### Work in progress...
