package com.leoosato.project.resource;


import com.leoosato.project.dto.LoginRequest;
import com.leoosato.project.dto.LoginResponse;
import com.leoosato.project.dto.RegisterRequest;
import com.leoosato.project.model.User;
import com.leoosato.project.repository.UserRepository;
import com.leoosato.project.services.PasswordService;
import com.leoosato.project.services.TokenService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject UserRepository users;
    @Inject PasswordService password;
    @Inject TokenService tokens;

    @POST @Path("/login")
    @Transactional
    public LoginResponse login(LoginRequest req){
        User u = users.findByUsername(req.username());
        if (u == null || !password.matches(req.password(), u.passwordHash))
            throw new NotAuthorizedException("invalid_credentials");
        return new LoginResponse(tokens.generate(u.username, u.role));
    }

    @POST @Path("/register")
    @PermitAll
    @Transactional
    public Response register(@Valid RegisterRequest req) {
        // username único?
        if (users.findByUsername(req.username()) != null) {
            throw new WebApplicationException("username_already_exists", 409);
        }
        // cria USER
        User u = new User();
        u.username = req.username();
        u.passwordHash = password.hash(req.password());
        u.role = "USER";
        u.persist();

        // opcional: já retorna token pra UX melhor
        LoginResponse token = new LoginResponse(tokens.generate(u.username, u.role));
        return Response.status(Response.Status.CREATED).entity(token).build();
    }
}
