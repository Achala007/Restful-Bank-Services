package controllers;

import model.Customer;
import service.CustomerController;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Created by ShimaK on 08-Apr-17.
 */
@Path("/customer")
public class CustomerService {
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject getCustomers() {
        ArrayList<Customer> customers = new CustomerController().getCustomers();

        JsonArrayBuilder builder = Json.createArrayBuilder();
        JsonObjectBuilder objbuild = Json.createObjectBuilder();

        if (customers == null || customers.size() < 1) {
            objbuild.add("status", "fail");
        } else {
            for (Customer customer : customers) {
                objbuild.add("name", customer.getName());
                objbuild.add("dob", customer.getDob().toString());
                objbuild.add("address", customer.getAddress());
                objbuild.add("mobile", customer.getMobile());
                objbuild.add("email", customer.getEmail());
                objbuild.add("accountNum", customer.getAccountNum());
                objbuild.add("accountType", customer.getAccountType());
                builder.add(objbuild);
            }
            objbuild.add("status", "success");
        }

        return objbuild.add("body", builder.build()).build();
    }

    @Path("{accountNum}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject getCustomer(@PathParam("accountNum") String accountNUm) {
        JsonObjectBuilder objbuild = Json.createObjectBuilder();
        Customer customer = new CustomerController().getCustomer(accountNUm);
        if (customer != null) {
            objbuild.add("status", "success");
            objbuild.add("name", customer.getName());
            objbuild.add("dob", customer.getDob().getTime());//date converted to milliseconds from 1970-01-01 00:00:00
            objbuild.add("address", customer.getAddress());
            objbuild.add("mobile", customer.getMobile());
            objbuild.add("email", customer.getEmail());
            objbuild.add("accountType", customer.getAccountType());
            objbuild.add("accountNum", customer.getAccountNum());
            objbuild.add("sortCode", customer.getSortCode());
            objbuild.add("balance", customer.getBalance());
            objbuild.add("card", customer.getCard());
        } else {
            objbuild.add("status", "fail");
        }

        return objbuild.build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public JsonObject createCustomer(JsonObject emp_json) {
        //TODO response error type
        int result = new CustomerController().createCustomer(emp_json.getString("name"), emp_json.getString("dob")
                , emp_json.getString("address"), emp_json.getString("mobile"), emp_json.getString("email")
                , emp_json.getString("accountType"), emp_json.getString("accountNumber"), emp_json.getString("sortCode")
                , emp_json.getString("balance"), emp_json.getString("card"));

        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (result == 0) {
            builder.add("response", "success");
        } else if (result == -1) {
            builder.add("response", "fail");
        } else {
            builder.add("response", "exist");
        }

        return builder.build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject updateCustomer(JsonObject emp_json) {
        //TODO receive password and validate then make changes
        //TODO response error type
        boolean result = new CustomerController().updateCustomer(emp_json.getString("accountNum")
                , emp_json.getString("column"), emp_json.getString("value"));

        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (result) {
            builder.add("response", "success");
        } else {
            builder.add("response", "fail");
        }

        return builder.build();
    }

    @Path("{accountNum}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject deleteCustomer(@PathParam("accountNum") String accountNum) {
        boolean result = new CustomerController().deleteCustomer(accountNum);

        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (result) {
            builder.add("response", "success");
        } else {
            builder.add("response", "fail");
        }

        return builder.build();
    }
}
