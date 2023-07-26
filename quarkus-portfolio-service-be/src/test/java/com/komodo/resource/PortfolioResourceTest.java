package com.komodo.resource;

import com.komodo.entitiy.Portfolio;
import com.komodo.repository.PortfolioRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PortfolioResourceTest {

    @Mock
    private PortfolioRepository repository;

    @InjectMocks
    private PortfolioResource resource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPortfoliosTest() {
        Portfolio portfolio = new Portfolio();  // create a mock Portfolio
        when(repository.listAll()).thenReturn(Collections.singletonList(portfolio));

        List<Portfolio> result = resource.getAllPortfolios();

        assertEquals(1, result.size());
        verify(repository, times(1)).listAll();
    }

    @Test
    void getPortfolioByIdTest() {
        Portfolio portfolio = new Portfolio();  // create a mock Portfolio
        when(repository.findById(anyString())).thenReturn(portfolio);

        Portfolio result = resource.getPortfolioById("test-id");

        assertEquals(portfolio, result);
        verify(repository, times(1)).findById(anyString());
    }

    @Test
    void createPortfolioTest() {
        Portfolio portfolio = new Portfolio();

        Response result = resource.createPortfolio(portfolio);

        assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
        assertEquals(portfolio, result.getEntity());
        verify(repository, times(1)).persist(any(Portfolio.class));
    }

    @Test
    void updatePortfolioTest() {
        Portfolio existingPortfolio = new Portfolio();
        Portfolio newPortfolio = new Portfolio();
        newPortfolio.setName("new name");
        newPortfolio.setDescription("new description");

        when(repository.findById(anyString())).thenReturn(existingPortfolio);

        Portfolio result = resource.updatePortfolio("test-id", newPortfolio);

        assertEquals(newPortfolio.getName(), result.getName());
        assertEquals(newPortfolio.getDescription(), result.getDescription());
        verify(repository, times(1)).findById(anyString());
    }


    @Test
    void deletePortfolioTest() {
        Portfolio portfolio = new Portfolio();

        when(repository.findById(anyString())).thenReturn(portfolio);

        Response result = resource.deletePortfolio("test-id");

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), result.getStatus());
        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).delete(portfolio);
    }

    @Test
    void getPortfolioByNameTest() {
        Portfolio portfolio = new Portfolio();
        when(repository.getPortfolioByName(anyString())).thenReturn(Collections.singletonList(portfolio));

        List<Portfolio> result = resource.getPortfolioByName("test-name");

        assertEquals(1, result.size());
        verify(repository, times(1)).getPortfolioByName(anyString());
    }

    @Test
    void getPortfolioByNameAndDescriptionTest() {
        Portfolio portfolio = new Portfolio();
        when(repository.getPortfolioByNameAndDescription(anyString(), anyString())).thenReturn(Collections.singletonList(portfolio));

        List<Portfolio> result = resource.getPortfolioByNameAndDescription("test-name", "test-description");

        assertEquals(1, result.size());
        verify(repository, times(1)).getPortfolioByNameAndDescription(anyString(), anyString());
    }


}
