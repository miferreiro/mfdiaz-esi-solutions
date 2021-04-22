package es.uvigo.esei.dgss.exercises.rest;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import es.uvigo.esei.dgss.exercise.service.PostEJB;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.Video;

/**
 * Resource that represents the posts in the application.
 * 
 * @author Miguel Ferreiro Díaz
 */
@Path("post")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostsResource {
	
	@EJB
	private PostEJB postEJB;
	
	@Context
	private UriInfo uriInfo;
	
	/**
	 * Returns the list of posts of the current user.
	 * 
	 * @param login the author of the posts.
	 * @return an {@code OK} response containing the list of posts of the current user
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@GET
	@Path("{login}")
	public Response getPosts(@PathParam("login") String login) throws SecurityException {
		
		final List<Post> list = this.postEJB.findPostByAuthor(login);
		
	    for(Post p :list) {	    	
	    	p.getUsersLike().size();
	    }
		
		return Response.ok(list).build();
	}
	
	/**
	 * Creates a new link.
	 * 
	 * @param link a new link to be stored.
	 * @param login author of the post.
	 * @return a {@code CREATED} response with the URI of the new link in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if link is {@code null} or if an link
	 * with the same id already exists.
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@POST	
	@Path("link/{login}")
	public Response createLink(Link link, @PathParam("login") String login) throws SecurityException {
		try {			
			if (link == null)
				throw new IllegalArgumentException("link can't be null");
			
			final Link newLink = this.postEJB.createLinkSecurity(link, login);
			
			final URI linkUri = uriInfo.getAbsolutePathBuilder()
				.path("link/" + newLink.getId())
			.build();
			
			return Response.created(linkUri).build();
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The link already exists");
		}		
	}	
	
	/**
	 * Creates a new photo. 
	 * 
	 * @param photo a new photo to be stored.
	 * @param login author of the post.
	 * @return a {@code CREATED} response with the URI of the new photo in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if photo is {@code null} or if an photo
	 * with the same id already exists.
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@POST
	@Path("photo/{login}")
	public Response createPhoto(Photo photo, @PathParam("login") String login) throws SecurityException {
		try {
			if (photo == null)
				throw new IllegalArgumentException("photo can't be null");
			
			final Post newPhoto = this.postEJB.createPhotoSecurity(photo, login);
			
			final URI photoUri = uriInfo.getAbsolutePathBuilder()
				.path("photo/" + newPhoto.getId())
			.build();
			
			return Response.created(photoUri).build();
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The photo already exists");
		}		
	}	
	
	/**
	 * Creates a new video. 
	 * 
	 * @param video a new video to be stored.
	 * @param login author of the post.
	 * @return a {@code CREATED} response with the URI of the new video in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if video is {@code null} or if an video
	 * with the same id already exists.
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@POST
	@Path("video/{login}")
	public Response createVideo(Video video, @PathParam("login") String login) throws SecurityException {
		try {			
			if (video == null)
				throw new IllegalArgumentException("video can't be null");
			
			final Post newVideo = this.postEJB.createVideoSecurity(video, login);
			
			final URI videoUri = uriInfo.getAbsolutePathBuilder()
				.path("video/" + newVideo.getId())
			.build();
			
			return Response.created(videoUri).build();
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The video already exists");
		}		
	}	
	
	/**
	 * Like a given post. 
	 * 
	 * @param id the identified of a post.
	 * @param login the identified of an user.
	 * @return a {@code CREATED} response with the URI of the new video in the
	 * {@code Location} header.
	 * @throws IllegalArgumentException if id or login is {@code null} or if the like post
	 * with the same id already exists.
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@PUT
	@Path("{id}/like/{login}")
	public Response likePost(@PathParam("id") String id, @PathParam("login") String login) throws SecurityException {
		try {					
			return Response.ok(this.postEJB.likePostSecurity(login, id)).build();	
		} catch (EntityExistsException e) {
			throw new IllegalArgumentException("The like already exists");
		}	
	}	
	
	/**
	 * Updates the information of a link. If the link is not stored, it will be created.
	 * 
	 * @param link a link to be updated.
	 * @return an empty {@code OK} response.
	 * @throws IllegalArgumentException if post is {@code null}
	 * @throws SecurityException if the post's owner is not the current user.
	 */	
	@PUT
	@Path("link/")
	public Response updateLink(Link link) throws SecurityException {
		if (link == null)
			throw new IllegalArgumentException("link can't be null");				
		return Response.ok(this.postEJB.updateLinkSecurity(link)).build();
	}
	
	/**
	 * Updates the information of a photo. If the photo is not stored, it will be
	 * created.
	 * 
	 * @param photo a photo to be updated.
	 * @return an empty {@code OK} response.
	 * @throws IllegalArgumentException if post is {@code null}
	 * @throws SecurityException if the post's owner is not the current user.
	 */	
	@PUT
	@Path("photo/")
	public Response updatePhoto(Photo photo) throws SecurityException {
		if (photo == null)
			throw new IllegalArgumentException("photo can't be null");		
		return Response.ok(this.postEJB.updatePhotoSecurity(photo)).build();
	}
	
	/**
	 * Updates the information of a video. If the video is not stored, it will be
	 * created.
	 * 
	 * @param video a video to be updated.
	 * @return an empty {@code OK} response.
	 * @throws IllegalArgumentException if video is {@code null}
	 * @throws SecurityException if the post's owner is not the current user.
	 */	
	@PUT
	@Path("video/")
	public Response updateVideo(Video video) throws SecurityException {
		if (video == null)
			throw new IllegalArgumentException("video can't be null");			
		return Response.ok(this.postEJB.updateVideoSecurity(video)).build();
	}
	
	/**
	 * Deletes a post.
	 * 
	 * @param id the identifier of the post to be deleted.
	 * @return an empty {@code OK} response.
	 * @throws IllegalArgumentException if there is no post with the provided identifier.
	 * @throws SecurityException if the post's owner is not the current user.
	 */
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") String id) throws SecurityException {		
		this.postEJB.removePost(this.postEJB.findPostById(id));		
		return Response.ok().build();
	}
}
