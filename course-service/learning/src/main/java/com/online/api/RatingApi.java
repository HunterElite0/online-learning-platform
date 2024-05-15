package com.online.api;

import java.util.List;

import org.jose4j.jwt.JwtClaims;

import com.online.controllers.CourseRepository;
import com.online.controllers.RatingRepository;
import com.online.model.Rating;
import com.online.service.JwtParser;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Stateless
@Path("/rating")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RatingApi {

  @EJB
  RatingRepository ratingRepo;

  @EJB
  CourseRepository courseRepo;

  JwtParser jwtParser = new JwtParser();

  @POST
  @Path("/submit")
  public Response submitCourseRating(String jwt, Rating rating) {

    if (jwt == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(jwt);
    if (claims == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equals("student")) {
      return Response.status(Response.Status.UNAUTHORIZED).entity("Only students can rate courses").build();
    }

    long studentId = Long.parseLong(claims.getClaimValue("id").toString());
    rating.setStudentId(studentId);

    if (courseRepo.findCourseById(rating.getCourseId()) == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Course not found").build();
    }

    Rating result = ratingRepo.makeRating(rating);
    if (result == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error submitting course rating").build();
    }

    double numberOfRatings = ratingRepo.getNumberOfRatings(rating.getCourseId());
    boolean updated = courseRepo.updateCourseRating(rating.getCourseId(), rating.getRating(),
        numberOfRatings);
    if (!updated) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error submitting course rating").build();
    }
    return Response.ok("Rating submitted!").build();
  }

  @GET
  @Path("/all")
  public Response getAllRatings() {
    List<Rating> ratings = ratingRepo.getAllRatings();
    if (ratings == null) {
      return Response.serverError().build();
    }
    return Response.ok(ratings).build();
  }

}
