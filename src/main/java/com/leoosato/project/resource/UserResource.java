package com.leoosato.project.resource;

import com.leoosato.project.dto.AdminCreateUserRequest;
import com.leoosato.project.model.User;
import com.leoosato.project.repository.UserRepository;
import com.leoosato.project.services.PasswordService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class UserResource {

    @Inject
    UserRepository users;
    @Inject
    PasswordService password;

    @GET
    public List<User> list() {
        return users.listAll();
    }

    @POST
    @Transactional
    public Response create(@Valid AdminCreateUserRequest req) {
        if (users.findByUsername(req.username()) != null) {
            throw new WebApplicationException("username_already_exists", 409);
        }
        User u = new User();
        u.username = req.username();
        u.passwordHash = password.hash(req.password());
        u.role = req.role();
        u.persist();
        return Response.status(Response.Status.CREATED).entity(u).build();
    }

    @DELETE @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") java.util.UUID id) {
        User u = users.findById(id);
        if (u == null) throw new NotFoundException();
        u.delete();
    }
}