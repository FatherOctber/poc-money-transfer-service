package com.fatheroctober.moneytransfer.api.service;


import com.fatheroctober.moneytransfer.api.model.Account;
import com.fatheroctober.moneytransfer.api.model.Amount;
import com.fatheroctober.moneytransfer.api.model.Currency;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountService {

    @GET
    @Path("/{number}")
    public Response getAccount(@PathParam("number") String number) {
        return Response
                .ok(new Account(number, new Amount(BigDecimal.ONE, Currency.RUR)))
                .build();
    }

    @POST
    @Path("/create")
    public Response createAccount(Account account) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{number}")
    public Response deleteAccount(@PathParam("number") String number) {
        return Response.ok().build();
    }
}
