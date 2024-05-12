package com.online.api;

import com.online.controllers.CourseRepository;
import com.online.controllers.RatingRepository;
import com.online.model.Rating;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
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
    String result = ratingRepo.makeRating(rating);
    if (result == null) {
      return Response.serverError().build();
    }

    double avgRating = courseRepo.getCourseRating(rating.getCourseId());
    if (avgRating == -1) {
      return Response.ok(avgRating).build();
    }

    double numberOfRatings = ratingRepo.getNumberOfRatings(rating.getCourseId());
    double newRating = (avgRating * (numberOfRatings - 1) + rating.getRating()) / numberOfRatings;

    boolean res = courseRepo.updateCourseRating(rating.getCourseId(), newRating);
    if(!res) {
      return Response.serverError().build();
    }
    return Response.ok("Rating submitted!").build();
  }

  

}
