package com.online.api;

import com.online.controllers.CourseRepository;
import com.online.model.Course;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.json.*;

@Path("/course")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseApi {

  @EJB
  CourseRepository courseRepo;

  @GET
  @Path("/test")
  public String test() {
    return "Hello World!";
  }

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
  @Path("/course")
  public Response getCourseByTerm(String searchTerm) {
    List<Course> courses = courseRepo.findCourses(searchTerm);
    if (courses == null) {
      return Response.serverError().build();
    }
    return Response.ok(courses).build();
  }

  
}
