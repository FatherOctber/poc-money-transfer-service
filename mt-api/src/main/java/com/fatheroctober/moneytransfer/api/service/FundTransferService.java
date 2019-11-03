package com.fatheroctober.moneytransfer.api.service;

import com.fatheroctober.moneytransfer.api.model.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FundTransferService {

    @POST
    public Response transfer(Transaction transaction) {
        return Response.ok().build();
    }
}
