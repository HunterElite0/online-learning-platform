package com.online.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.model.Enrollment;

import jakarta.annotation.Resource;
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

  @POST
  public Response makeEnrollmentRequest(Enrollment enrollment) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String enrollmentJson = mapper.writeValueAsString(enrollment);
      context.createProducer().send(queue, enrollmentJson);
      return Response.ok("Request sent").build();
    } catch (Exception e) {
      return Response.serverError().entity("Request failed").build();
    }
  }
}