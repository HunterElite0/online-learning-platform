package com.online.api;

import java.util.List;

import com.online.controllers.NotificationRepository;
import com.online.model.Notification;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Stateless
@Path("/notifications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationApi {

  @EJB
  NotificationRepository notificationRepo;

  @Path("/{studentId}")
  @GET
  public Response getNotificationsByStudentId(@PathParam("studentId") long studentId) {
    List<Notification> notifications = notificationRepo.getNotificationsByStudentId(studentId);
    if (notifications != null) {
      return Response.ok(notifications).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching notifications").build();
  }

}
