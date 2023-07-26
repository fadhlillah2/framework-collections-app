package com.komodo.resource;

import com.komodo.entitiy.Portfolio;
import com.komodo.repository.PortfolioRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Path("/portfolios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RestController
public class PortfolioResource {

    @Inject
    PortfolioRepository repository;

    @GET
    public List<Portfolio> getAllPortfolios() {
        return repository.listAll();
    }

    @GET
    @Path("/{id}")
    public Portfolio getPortfolioById(@PathParam("id") String id) {
        return repository.findById(id);
    }

    @POST
    @Transactional
    public Response createPortfolio(Portfolio portfolio) {
        repository.persist(portfolio);
        return Response.status(Response.Status.CREATED).entity(portfolio).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Portfolio updatePortfolio(@PathParam("id") String id,  Portfolio portfolio) {
        Portfolio existingPortfolio = repository.findById(id);
        if (existingPortfolio == null) {
            throw new WebApplicationException("Portfolio with id " + id + " not found", Response.Status.NOT_FOUND);
        }
        existingPortfolio.setName(portfolio.getName());
        existingPortfolio.setDescription(portfolio.getDescription());
        return existingPortfolio;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePortfolio(@PathParam("id") String id) {
        Portfolio portfolio = repository.findById(id);
        if (portfolio == null) {
            throw new WebApplicationException("Portfolio with id " + id + " not found", Response.Status.NOT_FOUND);
        }
        repository.delete(portfolio);
        return Response.noContent().build();
    }

    @GET
    @Path("/name/{name}")
    public List<Portfolio> getPortfolioByName(@PathVariable("name") String name)
    {
        return repository.getPortfolioByName(name);
    }

    @GET
    @Path("/name/{name}/{description}")
    public List<Portfolio> getPortfolioByNameAndDescription(@PathVariable("name") String name, @PathVariable("description") String description)
    {
        return repository.getPortfolioByNameAndDescription(name,description);
    }

}

