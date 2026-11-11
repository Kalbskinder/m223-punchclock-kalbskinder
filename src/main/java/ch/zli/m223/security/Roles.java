package ch.zli.m223.security;

/** Role names used in the JWT "groups" claim and in @RolesAllowed. */
public final class Roles {
    public static final String USER = "User";
    public static final String ADMIN = "Admin";

    private Roles() {
    }
}
