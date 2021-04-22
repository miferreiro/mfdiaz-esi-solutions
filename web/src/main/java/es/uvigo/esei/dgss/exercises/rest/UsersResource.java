package es.uvigo.esei.dgss.exercises.rest;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.uvigo.esei.dgss.exercise.service.UserEJB;
import es.uvigo.esei.dgss.exercises.domain.Friendship;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.User;


/**
 * Resource that represents the users in the application.
 * 
 * @author Miguel Ferreiro Díaz
 */
@Path("user")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
	
	@Context
	private UriInfo uriInfo;
	
	@EJB
	private UserEJB userEJB;

	/**
	 * Returns the list of users stored in the application. (To test the exercises)
	 * 
	 * @return an {@code OK} response containing the list of users stored in
	 * the application.
	 */
	@GET
	public Response getUsers() {
		return Response.ok(this.userEJB.findAllUsers()).build();
	}
	
	/**
	 * Creates a new user. 
	 * (not authenticated -> Anyone can create an user despite the role of the user created. 
	 * In the case of controlling the role that is introduced, an if would be necessary to carry out the check,
	 * or to establish the strategy of creating all the users with the user role.)
	 * 
	 * @param user a new user to be stored.
	 * @return a {@code CREATED} response with the URI of the new user in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if user is {@code null} or if an user
	 * with the same login already exists.
	 * @throws SecurityException if the login is not the current user.
	 */
	@POST	
	public Response create(User user) {
		try {
			if (user == null)
				throw new IllegalArgumentException("user can't be null");
			
			final User newUser = this.userEJB.createUser(user);
			final URI userUri = uriInfo.getAbsolutePathBuilder()
				.path(newUser.getLogin())
			.build();
			
			return Response.created(userUri).build();
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The user already exists");
		}		
	}		
		
	/**
	 * Creates a new request of friendship. 
	 * 
	 * @param login1 the user who receives the friend request.
	 * @param login2 the user who makes the friend request.
	 * @return a {@code CREATED} response with the URI of the new user in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if login2 is {@code null} or if an user
	 * with the same login already exists.
	 * @throws SecurityException if the login is not the current user.
	 */
	@POST	
	@Path("{login}/friendsrequests")
	public Response requestFriendship(@PathParam("login") String login1, String login2) throws SecurityException {
		try {			
			
			if (login2 == null)
				throw new IllegalArgumentException("login2 can't be null");
			
			final Friendship newFriendship = this.userEJB.requestFriendship(login1, login2, new Date());
			
			final URI friendshipUri = uriInfo.getAbsolutePathBuilder()
				.path(newFriendship.getUser1().getLogin() + "/friendsrequests/")
			.build();
			
			return Response.created(friendshipUri).build();
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The friendship already exists");
		}		
	}
	
	/**
	 * Returns the list of friends of the current user.
	 * 
	 * @param login the login of a user.
	 * @return an {@code OK} response containing the friends requests of the current user.
	 * @throws SecurityException if the login is not the current user.
	 */
	@GET	
	@Path("{login}/friends")
	public Response getFriendsRequests(@PathParam("login") String login) throws SecurityException {			
		return Response.ok(this.userEJB.listFriendshipRequests(login)).build();		
	}
	
	/**
	 * Accepts a request of friendship. 
	 * 
	 * @param login1 the user who receives the friend request.
	 * @param login2 the user who makes the friend request.
	 * @param statusRequest the response of the request.
	 * @return an empty {@code OK} response.
	 * @throws IllegalArgumentException if statusRequest is {@code null} 
	 * @throws SecurityException if the login is not the current user.
	 */
	@PUT
	@Path("{login}/friendsrequests/{login_requested}")
	public Response acceptFriendship(@PathParam("login") String login1, @PathParam("login_requested") String login2, Boolean statusRequest) throws SecurityException {
		if (statusRequest == null)
			throw new IllegalArgumentException("statusRequest can't be null");
		return Response.ok(this.userEJB.acceptFriendship(login1, login2, statusRequest)).build();
	}
	
	/**
	 * Returns the wall posts of the current user.
	 * 
	 * @param login the user who receives the friend request.
	 * @return an {@code OK} response containing the wall posts of the current user.
	 * @throws SecurityException if the login is not the current user.
	 */
	@GET	
	@Path("{login}/wall")
	public Response getWallPosts(@PathParam("login") String login) throws SecurityException {	
		
	    final List<Post> list = this.userEJB.getWallPosts(login);
	    
	    for(Post p :list) {
	    	p.getUsersLike().size();
	    }
		
		return Response.ok(list).build();		
	}	
}
