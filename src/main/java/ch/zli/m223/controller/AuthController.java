package ch.zli.m223.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.ApplicationUserDto;
import ch.zli.m223.dto.CredentialsDto;
import ch.zli.m223.dto.TokenDto;
import ch.zli.m223.service.AuthService;

@Path("/")
@PermitAll
@Tag(name = "Authentication", description = "Registration and login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("register")
    @Operation(summary = "Registers a new user.", description = "Creates a user with the role 'User'. Returns 409 if the email is already taken.")
    public ApplicationUserDto register(@Valid CredentialsDto credentials) {
        return authService.register(credentials);
    }

    @POST
    @Path("login")
    @Operation(summary = "Logs in a user.", description = "Returns a JWT for the Authorization header (Bearer). Returns 401 for wrong credentials.")
    public TokenDto login(@Valid CredentialsDto credentials) {
        return authService.login(credentials);
    }
}
