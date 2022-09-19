package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.domain.model.Post;
import io.github.matheuspadilha.quarkussocial.resource.dto.CreatePostRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.PostResponse;
import io.github.matheuspadilha.quarkussocial.service.PostService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    PostService postService;

    @POST
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest postRequest) {
        Post post = postService.create(userId, postRequest);
        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId) {
        List<PostResponse> posts = postService.findAllByUser(userId, followerId);
        return Response.ok(posts).build();
    }
}
