package wyye.javafilm.managers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import wyye.javafilm.entities.Production;
import wyye.javafilm.exceptions.TagException;
import wyye.javafilm.managers.ProductionManagerLocal;

/**
 *
 * @author ad
 */
@Stateless
public class ProductionManager implements ProductionManagerLocal {

    @PersistenceContext(unitName = "JavaFilmPU")
    private EntityManager em;

    @Override
    public Production getProductionById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Production.class, id);
    }

    @Override
    public Production addProduction(Production production) throws TagException {
        if (production == null) {
            throw new TagException("Production is not specified");
        }
        if (production.getName() == null) {
            throw new TagException("Production name is not specified"); 
        }
        em.persist(production);
        return production;     
    }

    @Override
    public List<Production> getAllProductions() {
        List<Production> productionList = em.createNativeQuery("select * from production order by name", Production.class).getResultList();
        if (productionList == null || productionList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return productionList;
    }

    @Override
    public Production deleteProduction(Long id) throws TagException {
        if (id == null) 
            throw new TagException("Production id not specified");
        Production production = em.find(Production.class, id);
        if (production == null) 
            throw new TagException("Production not exist");
        em.remove(production);
        return production;
    }
}
