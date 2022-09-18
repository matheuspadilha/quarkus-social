package io.github.matheuspadilha.quarkussocial.resource;

import io.github.matheuspadilha.quarkussocial.resource.dto.FollowerRequest;
import io.github.matheuspadilha.quarkussocial.resource.dto.FollowersPerUserResponse;
import io.github.matheuspadilha.quarkussocial.service.FollowerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    @Inject
    FollowerService followerService;

    @PUT
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerRequest) {
        followerService.followUser(userId, followerRequest);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId) {
        FollowersPerUserResponse response = followerService.findByUser(userId);
        return Response.ok(response).build();
    }

    @DELETE
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId) {
        followerService.unfollowUser(userId, followerId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
