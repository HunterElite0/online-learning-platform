package com.online.api;

import com.online.controllers.AccountRepository;
import com.online.model.Account;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.json.*;

@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountApi {

  @EJB
  AccountRepository accountRepository;

  @GET
  @Path("/test")
  public String test() {
    return "Hello World!";
  }

  @POST
  @Path("/register")
  public Response registerAccount(Account account) {
    Account acc = accountRepository.register(account.getName(), account.getEmail(), account.getPassword());
    if (acc == null) {
      return Response.serverError().build();
    }
    return Response.ok(acc).build();
  }

  @GET
  @Path("/login")
  public Response loginAccount(String jsonString) {

    JSONObject obj = new JSONObject(jsonString);
    String email = obj.getString("email");
    String password = obj.getString("password");

    Account acc = accountRepository.login(email, password);

    if (acc == null) {
      return Response.serverError().build();
    }
    return Response.ok(acc).build();
  }
}
