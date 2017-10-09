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

package com.madewell.service.ordermgt;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@SwaggerDefinition(
        info = @Info(
                description = "This is Madewell order microservice",
                version = "V1",
                title = "Madewell Order API"
        ))
@Path("/orderservice")
public class OrderService {

    private Map<String, Order> ordersList = new ConcurrentHashMap<String, Order>();

    public synchronized Order updateOrder(String orderId, String itemName, String locations) {
        Order order = ordersList.get(orderId);
        if (order != null) {
            if (order.isLocked()) {
                return order;
            } else {
                if (itemName != null && !"".equals(itemName)) {
                    order.setitemName(itemName);
                }
                order.setItemCode(locations);
                return order;
            }
        }
        return null;
    }


    public synchronized Order lockOrder(String orderId) {
        Order order = ordersList.get(orderId);
        if (order != null) {
            order.setLocked(true);
            return order;
        }
        return null;
    }

    @GET
    @Path("/order/{id}")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "return the order corresponding to the order id"
    )
    public Response getOrder(@PathParam("id") String id) {
        // TODO: Implementation for HTTP GET request
        Order order = ordersList.get(id);
        if(order != null) {
            return Response.status(Response.Status.OK).entity(order).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity(null).build();

    }

    @GET
    @Path("/order/pending")
    @Produces({"application/json", "text/xml"})
    @ApiOperation(
            value = "return all pending orders"
    )
    public Response getPendingOrders() {
        List<Order> orders = new ArrayList<Order>();
        for (Order order : ordersList.values()) {
            if (!order.isLocked()) {
                orders.add(order);
            }
        }

        if(orders.size() > 0) {
            return Response.status(Response.Status.OK).entity(orders.toArray(new Order[orders.size()])).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity(null).build();

    }

    @POST
    @Path("/order")
    @Consumes("application/json")
    @ApiOperation(
            value = "create a new Order"
    )
    public void addOrder(@ApiParam(value = "Order object", required = true) Order order) {
        Order newOrder = new Order(order.getitemName(), order.getItemCode(), order.getCustomerId());
        System.out.println(newOrder.getOrderId());
        ordersList.put(newOrder.getOrderId(), newOrder);
    }

    @PUT
    @Path("/order")
    @Consumes("application/json")
    @ApiOperation(
            value = "update the order by existing order id"
    )
    public void put(@ApiParam(value = "Order object", required = true) Order order) {
        updateOrder(order.getOrderId(), order.getitemName(), order.getItemCode());
    }

    @PUT
    @Path("/order/lock")
    @Consumes("application/json")
    @ApiOperation(
            value = "Lock the order is payment is processed"
    )
    public void lock(@ApiParam(value = "Order object", required = true) Order order) {
        lockOrder(order.getOrderId());
    }

    @DELETE
    @Path("/")
    @ApiOperation(
            value = "delete the order by order id"
    )
    public void delete() {
        // TODO: Implementation for HTTP DELETE request
        System.out.println("DELETE invoked");
    }
}
