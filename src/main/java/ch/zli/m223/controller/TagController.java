package ch.zli.m223.controller;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.dto.TagDto;
import ch.zli.m223.security.Roles;
import ch.zli.m223.service.TagService;

@Path("/tags")
@Tag(name = "Tags", description = "Handling of tags")
@RolesAllowed({ Roles.USER, Roles.ADMIN })
public class TagController {

    @Inject
    TagService tagService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all tags.", description = "Returns a list of all tags.")
    public List<TagDto> index() {
        return tagService.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a single tag.", description = "Returns the tag with the given id.")
    public TagDto get(@PathParam("id") Long id) {
        return tagService.findById(id);
    }

    @POST
    @RolesAllowed(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new tag.", description = "Creates a new tag and returns it.")
    public TagDto create(@Valid TagDto tag) {
        return tagService.createTag(tag);
    }

    @PUT
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a tag.", description = "Updates the tag with the given id and returns it.")
    public TagDto update(@PathParam("id") Long id, @Valid TagDto tag) {
        return tagService.updateTag(id, tag);
    }

    @DELETE
    @RolesAllowed(Roles.ADMIN)
    @Path("/{id}")
    @Operation(summary = "Deletes a tag.", description = "Deletes the tag with the given id.")
    public void delete(@PathParam("id") Long id) {
        tagService.deleteTag(id);
    }
}
