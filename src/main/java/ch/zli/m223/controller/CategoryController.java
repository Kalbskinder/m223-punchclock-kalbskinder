package ch.zli.m223.controller;

import java.util.List;

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

import ch.zli.m223.dto.CategoryDto;
import ch.zli.m223.service.CategoryService;

@Path("/categories")
@Tag(name = "Categorys", description = "Handling of categories")
public class CategoryController {

    @Inject
    CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all categories.", description = "Returns a list of all categories.")
    public List<CategoryDto> index() {
        return categoryService.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a single category.", description = "Returns the category with the given id.")
    public CategoryDto get(@PathParam("id") Long id) {
        return categoryService.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new category.", description = "Creates a new category and returns it.")
    public CategoryDto create(@Valid CategoryDto category) {
        return categoryService.createCategory(category);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Updates a category.", description = "Updates the category with the given id and returns it.")
    public CategoryDto update(@PathParam("id") Long id, @Valid CategoryDto category) {
        return categoryService.updateCategory(id, category);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes a category.", description = "Deletes the category with the given id.")
    public void delete(@PathParam("id") Long id) {
        categoryService.deleteCategory(id);
    }
}
