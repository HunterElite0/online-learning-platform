package com.online.api;

import org.jose4j.jwt.JwtClaims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.controllers.CourseRepository;
import com.online.model.Enrollment;
import com.online.service.JwtParser;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSDestinationDefinitions;
import jakarta.jms.Queue;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Stateless
@Path("/enroll")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JMSDestinationDefinitions(value = {
    @JMSDestinationDefinition(name = "java:/queue/ENROLLMENTQueue", interfaceName = "jakarta.jms.Queue", destinationName = "EnrollmentQueue")
})
public class EnrollRequestApi {

  @Inject
  private transient JMSContext context;

  @Resource(lookup = "java:/queue/ENROLLMENTQueue")
  private transient Queue queue;

  @EJB
  private CourseRepository courseRepo;

  JwtParser jwtParser = new JwtParser();

  @POST
  public Response makeEnrollmentRequest(String jwt, Enrollment enrollment) {
    if (courseRepo.findCourseById(enrollment.getCourseId()) == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("Course not found").build();
    }

    if (jwt == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    try {

      JwtClaims claims = jwtParser.parseClaims(jwt);
      if (claims == null) {
        System.out.println("Claims are null");
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      if (!claims.getClaimValue("role").equals("student")) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }

      enrollment.setStudentId((Long) claims.getClaimValue("id"));

      ObjectMapper mapper = new ObjectMapper();
      String enrollmentJson = mapper.writeValueAsString(enrollment);
      context.createProducer().send(queue, enrollmentJson);
      return Response.ok("Request sent").build();
    } catch (Exception e) {
      return Response.serverError().entity("Request failed").build();
    }
  }
}
