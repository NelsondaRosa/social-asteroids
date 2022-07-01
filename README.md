# Social Asteroids

Social Asteroids is a place to play the classic Asteroids game while sharing your scores, meet friends and to discuss on the forum.

The purpose of the project is to serve as a portfolio to demonstrate capabilities with the Spring ecosystem in Java. In it, **DDD** architecture concepts are implemented, as well as the criteria of a **REST** application. I also want it to serve as a reference for those who need content for the technologies used.


## Architecture

In the application a simple layered architecture for a *monolith* was applied.
<div align = "center">
<img src="https://raw.githubusercontent.com/nelsondrp/social-asteroids-backend/main/diagram.svg">
</div>
 
 #### Domain
In the domain is the heart of the business, the entities that contain the data necessary for its operation, as well as their mappings are found here.

#### Security
The security layer contains its own entities, repositories, services and controllers. As it is not necessarily part of the logical context of the rest of the application, a layer was created especially for it.

#### Infrastructure
The infrastructure layer is responsible for the application backbone. Repositories, request interceptors, and error handling are its responsibility.

#### Business
In the business layer, all the application logic is concentrated, it is also responsible for handling the data that arrives and leaves it.

### Presentation
The presentation layer receives the data from *requests*, forwards them for processing and receives the data that should be forwarded to the *response*.

## Stateless
Starting from the main principle of a REST application, this API is stateless, that is, it does not store any client state on the server, each request is independent and needs to contain everything necessary to access resources. For this, the security layer uses *Json Web Tokens* for authentication.

#### JWT
It allows the user to navigate between server requests without having to authenticate at each request, as well as maintaining the Stateless server.

After the user is logged in, a token is created on the server with the necessary user data and and encrypted with a key belonging to the server. This token has a short validity time, and is sent to the user through a cookie. Along with the JWT, a Refresh Token is created that is stored in a database table, it has a longer validity time. Every time a cookie with expired JWT is sent, the server will capture the Refresh Token data, which is also encrypted with a server secret, and perform its validation, having passed the validation, a new JWT is generated and aggregated. as a cookie to the response. When the Refresh Token expires, the user will need to authenticate with their credentials again.

With the Refresh Token, if the user's JWT is spoofed, the attacker will have a short period of authentication time.



