package ch.zli.m223.service;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import ch.zli.m223.dto.ApplicationUserDto;
import ch.zli.m223.dto.CredentialsDto;
import ch.zli.m223.dto.TokenDto;
import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.repository.ApplicationUserRepository;
import ch.zli.m223.security.Roles;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class AuthService {
    @Inject
    ApplicationUserRepository userRepository;

    /** Creates a new user with the role "User". Responds with 409 if the email is already taken. */
    @Transactional
    public ApplicationUserDto register(CredentialsDto credentials) {
        String email = normalize(credentials.getEmail());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new WebApplicationException("Email is already registered.", Response.Status.CONFLICT);
        }
        return createUser(email, credentials.getPassword(), Roles.USER).toDto();
    }

    /** Checks the credentials and returns a signed JWT. Responds with 401 if they are wrong. */
    public TokenDto login(CredentialsDto credentials) {
        ApplicationUser user = userRepository.findByEmail(normalize(credentials.getEmail()))
                .filter(u -> BcryptUtil.matches(credentials.getPassword(), u.getPasswordHash()))
                // Same message for unknown email and wrong password, so nobody can probe for accounts.
                .orElseThrow(() -> new WebApplicationException("Invalid email or password.",
                        Response.Status.UNAUTHORIZED));

        // Issuer and lifespan come from application.properties (smallrye.jwt.new-token.*)
        String token = Jwt.upn(user.getEmail())
                .subject(user.getId().toString())
                .groups(Set.of(user.getRole()))
                .sign();
        return new TokenDto(token);
    }

    @Transactional
    public ApplicationUser createUser(String email, String password, String role) {
        ApplicationUser user = new ApplicationUser();
        user.setEmail(normalize(email));
        user.setPasswordHash(BcryptUtil.bcryptHash(password));
        user.setRole(role);
        userRepository.persist(user);
        return user;
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }
}
