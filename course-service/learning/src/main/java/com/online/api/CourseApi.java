package com.online.api;

import com.online.controllers.CourseRepository;
import com.online.model.Course;
import com.online.service.JwtParser;
import com.online.wrappers.CourseRequest;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.jose4j.jwt.JwtClaims;

@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class CourseApi {

  @EJB
  CourseRepository repository;

  JwtParser jwtParser = new JwtParser();

  @GET
  @Path("/test")
  public Response test(String jwt) {
    try {
      System.out.println(jwt);

      if (jwt == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }

      JwtClaims claims = jwtParser.parseClaims(jwt);
      if (claims == null) {
        System.out.println("Claims are null");
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }

      System.out.println("ID:    " + claims.getClaimValue("id"));
      System.out.println("ROLE:  " + claims.getClaimValue("role"));

      return Response.ok("Test").build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
  }

  @POST
  @Path("/create")
  public Response createCourse(CourseRequest request) {

    if (request.getJwt() == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(request.getJwt());
    if (claims == null) {
      System.out.println("Claims are null");
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equalsIgnoreCase("instructor")) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    Course obj = repository.create(request.getCourse());
    if (obj == null) {
      return Response.serverError().build();
    }
    return Response.ok(obj).build();
  }

  @GET
  @Path("/courses")
  public Response getAllCourses() {
    // TODO: implement after creating admin page
    // List<Course> courses = repository.listAllValidCourses();
    List<Course> courses = repository.listAllCourses();
    if (courses == null) {
      return Response.serverError().build();
    }
    return Response.ok(courses).build();
  }

  @GET
  @Path("/search/{searchTerm}")
  public Response getCourseByTerm(@PathParam("searchTerm") String searchTerm) {
    List<Course> courses = repository.findCourses(searchTerm);
    if (courses == null) {
      return Response.serverError().build();
    }
    return Response.ok(courses).build();
  }

  @PUT
  @Path("/update/{id}")
  public Response updateCourse(@PathParam("id") long id, CourseRequest request) {
    if (request.getJwt() == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(request.getJwt());
    if (claims == null) {
      System.out.println("Claims are null");
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equalsIgnoreCase("instructor")) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    String response = repository.updateCourse(id, request.getCourse());
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @PUT
  @Path("/accept/{id}")
  public Response acceptCourseById(@PathParam("id") long id, @HeaderParam("jwt") String jwt){
    if (jwt == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(jwt);
    if (claims == null) {
      System.out.println("Claims are null");
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equalsIgnoreCase("admin")) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    String response = repository.acceptCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @PUT
  @Path("/reject/{id}")
  public Response rejectCourseById(@PathParam("id") long id, @HeaderParam("jwt") String jwt){

    if (jwt == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(jwt);
    if (claims == null) {
      System.out.println("Claims are null");
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equalsIgnoreCase("admin")) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    String response = repository.rejectCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @DELETE
  @Path("/remove/{id}")
  public Response removeCourseById(@PathParam("id") long id, @HeaderParam("jwt") String jwt){
    if (jwt == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    JwtClaims claims = jwtParser.parseClaims(jwt);
    if (claims == null) {
      System.out.println("Claims are null");
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    if (!claims.getClaimValue("role").toString().equalsIgnoreCase("admin")) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    String response = repository.removeCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @GET
  @Path("/courses/{id}")
  public Response getCoursesByInstructorId(@PathParam("id") long id) {
    List<Course> result = repository.getCoursesByInstructorId(id);
    if (result == null) {
      return Response.serverError().build();
    }
    return Response.ok(result).build();
  }

}
