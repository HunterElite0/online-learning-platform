package com.online.api;

import java.util.List;

import com.online.controllers.EnrollmentRepository;
import com.online.controllers.NotificationRepository;
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
  private EnrollmentRepository enrollmentRepo;

  @EJB
  private NotificationRepository notificationRepo;

  @PUT
  @Path("/accept/{id}")
  public Response acceptEnrollment(@PathParam("id") long id) {
    String response = enrollmentRepo.acceptEnrollment(id);
    if (response != null) {
      Enrollment enrollment = enrollmentRepo.getEnrollmentById(id);
      notificationRepo.makeNotification(enrollment.getStudentId(), "Enrollment accepted");

      return Response.ok(response).build();
    }
    return Response.serverError().build();
  }

  @PUT
  @Path("/reject/{id}")
  public Response rejectEnrollment(@PathParam("id") long id) {
    String response = enrollmentRepo.rejectEnrollment(id);
    if (response != null) {
      Enrollment enrollment = enrollmentRepo.getEnrollmentById(id);
      notificationRepo.makeNotification(enrollment.getStudentId(), "Enrollment rejected");

      return Response.ok(response).build();
    }
    return Response.serverError().build();
  }

  @GET
  @Path("/{id}")
  public Response getEnrollmentById(@PathParam("id") long id) {
    Enrollment enrollment = enrollmentRepo.getEnrollmentById(id);
    if (enrollment != null) {
      return Response.ok(enrollment).build();
    }
    return Response.serverError().build();
  }

  @GET
  @Path("/student/{id}")
  public Response getEnrollmentsByStudentId(@PathParam("id") long id) {
    List<Enrollment> enrollments = enrollmentRepo.getEnrollmentsByStudentId(id);
    if (enrollments != null) {
      return Response.ok(enrollments).build();
    }
    return Response.serverError().build();
  }
}
