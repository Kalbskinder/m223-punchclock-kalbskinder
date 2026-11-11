package ch.zli.m223.controller;

import java.time.LocalDate;
import java.util.Map;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.security.Roles;
import ch.zli.m223.service.EntryService;

@Path("/statistics")
@Tag(name = "Statistics", description = "Statistics about the recorded time")
@RolesAllowed({ Roles.USER, Roles.ADMIN })
public class StatisticsController {

    @Inject
    EntryService entryService;

    @GET
    @Path("/time-summaries")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Recorded time per day.",
        description = "Returns the total recorded time per day (YYYY-MM-DD) in the format HH:mm:ss.")
    public Map<LocalDate, String> timeSummaries() {
        return entryService.getTimeSummaries();
    }
}
