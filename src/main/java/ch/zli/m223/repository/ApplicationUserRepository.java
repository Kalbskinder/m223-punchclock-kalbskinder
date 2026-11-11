package ch.zli.m223.repository;

import java.util.Optional;

import ch.zli.m223.model.ApplicationUser;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationUserRepository implements PanacheRepository<ApplicationUser> {

    public Optional<ApplicationUser> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
