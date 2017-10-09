/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acme.service.payment;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0-SNAPSHOT
 */
@SwaggerDefinition(
        info = @Info(
                description = "This is the payment service provided by acme group",
                version = "V1",
                title = "ACME Payment API for Order Management"
        ))
@Path("/paymentservice")
public class PaymentMgtService {

    private Map<String, Payment> paymentRegister = new ConcurrentHashMap<String, Payment>();


    @GET
    @Path("/payment/{id}")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "get the payment item from payment id"
    )
    public Response getPayment(@PathParam("id") String id) {
        Payment payment = paymentRegister.get(id);
        if(payment != null) {
            return Response.status(Response.Status.OK).entity(paymentRegister.get(id)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity(null).build();
    }

    @POST
    @Path("/payment")
    @Consumes("application/json")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "make payment method"
    )
    public Response makePayment(@ApiParam(value = "Payment object", required = true) Payment payment) {
        Payment newPayment = paymentRegister.get(payment.getOrderId());
        if (newPayment != null) {
            return Response.status(Response.Status.OK).entity(new PaymentStatus("Duplicate Payment", newPayment)).build();
        }

        newPayment = new Payment(payment.getOrderId(), Double.parseDouble(payment.getAmount()));
        newPayment.setCardNumber(payment.getCardNumber());
        newPayment.setExpiryDate(payment.getExpiryDate());
        newPayment.setName(payment.getName());
        paymentRegister.put(payment.getOrderId(), newPayment);
        System.out.print(newPayment.getOrderId());
        return Response.status(Response.Status.OK).entity(new PaymentStatus("Payment Accepted", newPayment)).build();

    }

    @PUT
    @Path("/")
    @ApiOperation(
            value = "updating the payment item from id"
    )
    public void put() {
        // TODO: Implementation for HTTP PUT request
        System.out.println("PUT invoked");
    }

    @DELETE
    @Path("/payment/{orderId}")
    @ApiOperation(
            value = "removing the payment item from id"
    )
    public void removePayment(@PathParam("orderId") String orderId) {
        paymentRegister.remove(orderId);
    }
}
