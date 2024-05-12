package com.online.api;

import java.util.List;

import com.online.controllers.EnrollmentRepository;
import com.online.model.Enrollment;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/enrollment")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnrollmentApi {

  @EJB
  private EnrollmentRepository repository;

  @PUT
  @Path("/accept/{id}")
  public Response acceptEnrollment(@PathParam("id") long id) {
    String response = repository.acceptEnrollment(id);
    if (response != null) {
      repository.getEnrollmentById(id);
      
      return Response.ok(response).build();
    }
    return Response.serverError().build();
  }

  @PUT
  @Path("/reject/{id}")
  public Response rejectEnrollment(@PathParam("id") long id) {
    String response = repository.rejectEnrollment(id);
    if (response != null) {
      return Response.ok(response).build();
    }
    return Response.serverError().build();
  }

  @GET
  @Path("/{id}")
  public Response getEnrollmentById(@PathParam("id") long id) {
    Enrollment enrollment = repository.getEnrollmentById(id);
    if (enrollment != null) {
      return Response.ok(enrollment).build();
    }
    return Response.serverError().build();
  }

  @GET
  @Path("/student/{id}")
  public Response getEnrollmentsByStudentId(@PathParam("id") long id) {
    List<Enrollment> enrollments = repository.getEnrollmentsByStudentId(id);
    if (enrollments != null) {
      return Response.ok(enrollments).build();
    }
    return Response.serverError().build();
  }
}
