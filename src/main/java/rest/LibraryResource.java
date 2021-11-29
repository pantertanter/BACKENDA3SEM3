package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.LibraryItemDTO;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("library")
public class LibraryResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade userFacade = UserFacade.getUserFacade(EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Welcome to the user library.";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add/{key}")
    @RolesAllowed("user")
    public String addBook(@PathParam("key") String key) {
        String username = securityContext.getUserPrincipal().getName();
        LibraryItemDTO itemDTO = new LibraryItemDTO(key);
        LibraryItemDTO resultDTO = userFacade.addBook(username, itemDTO);
        return GSON.toJson(resultDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    @RolesAllowed("user")
    public String getLibrary() {
        String username = securityContext.getUserPrincipal().getName();
        return GSON.toJson(userFacade.getLibrary(username));
    }
}