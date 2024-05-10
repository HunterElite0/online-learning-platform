package com.online.api;

import com.online.controllers.CourseRepository;
import com.online.model.Course;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

import org.json.*;

@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class CourseApi {

  @EJB
  CourseRepository courseRepo;

  @POST
  @Path("/create")
  public Response createCourse(Course course) {
    Course obj = courseRepo.create(course);
    if (obj == null) {
      return Response.serverError().build();
    }
    return Response.ok(obj).build();
  }

  @GET
  @Path("/courses")
  public Response getAllCourses() {
    List<Course> courses = courseRepo.listAllCourses();
    if (courses == null) {
      return Response.serverError().build();
    }
    return Response.ok(courses).build();
  }

  @GET
  @Path("/search/{searchTerm}")
  public Response getCourseByTerm(@PathParam("searchTerm") String searchTerm) {
    List<Course> courses = courseRepo.findCourses(searchTerm);
    if (courses == null) {
      return Response.serverError().build();
    }
    return Response.ok(courses).build();
  }

  @PUT
  @Path("/update/{id}")
  public Response updateCourse(@PathParam("id") long id, Course course) {
    String response = courseRepo.updateCourse(id, course);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @PUT
  @Path("/accept/{id}")
  public Response acceptCourseById(@PathParam("id") long id) {
    String response = courseRepo.acceptCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @PUT
  @Path("/reject/{id}")
  public Response rejectCourseById(@PathParam("id") long id) {
    String response = courseRepo.rejectCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }

  @DELETE
  @Path("/remove/{id}")
  public Response removeCourseById(@PathParam("id") long id) {
    String response = courseRepo.removeCourse(id);
    if (response == null) {
      return Response.serverError().build();
    }
    return Response.ok(response).build();
  }
}
