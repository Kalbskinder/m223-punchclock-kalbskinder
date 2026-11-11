package ch.zli.m223.controller;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.EntryDetailDto;
import ch.zli.m223.security.Roles;
import ch.zli.m223.service.EntryService;

@Path("/entry-details")
@Tag(name = "Entry details", description = "Entries with their category and tags")
@RolesAllowed({ Roles.USER, Roles.ADMIN })
public class EntryDetailController {

    @Inject
    EntryService entryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all entries with details.", description = "Returns all entries including category and tags.")
    public List<EntryDetailDto> index() {
        return entryService.findAllDetails();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a single entry with details.", description = "Returns the entry with the given id including category and tags.")
    public EntryDetailDto get(@PathParam("id") Long id) {
        return entryService.findDetailById(id);
    }
}
