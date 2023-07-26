package com.komodo.repository;

import com.komodo.entitiy.Portfolio;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class PortfolioRepository implements PanacheRepositoryBase<Portfolio,String> {

    public List<Portfolio> getPortfolioByName(String name)
    {
        return Portfolio.find("name",name).list();
    }

    public List<Portfolio> getPortfolioByNameAndDescription(String name,String description)
    {
        return Portfolio.find("name = :nm and description = : des", Parameters.with("nm",name)
                .and("des",description).map()
        ).list();
    }

}
