package com.online.api;

import java.util.List;

import com.online.controllers.CourseRepository;
import com.online.controllers.RatingRepository;
import com.online.model.Rating;

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

  @POST
  @Path("/submit")
  public Response submitCourseRating(Rating rating) {
    Rating result = ratingRepo.makeRating(rating);
    if (result == null) {
      return Response.serverError().build();
    }

    double numberOfRatings = ratingRepo.getNumberOfRatings(rating.getCourseId());
    if (numberOfRatings == -1) {
      return Response.serverError().build();
    }
    boolean updated = courseRepo.updateCourseRating(rating.getCourseId(), rating.getRating(),
        numberOfRatings);
    if (!updated) {
      return Response.serverError().build();
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
