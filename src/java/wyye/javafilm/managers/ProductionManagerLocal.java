/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.managers;

import java.util.List;
import javax.ejb.Local;
import wyye.javafilm.entities.Production;
import wyye.javafilm.exceptions.TagException;

/**
 *
 * @author ad
 */
@Local
public interface ProductionManagerLocal {
    public Production getProductionById(Long id);
    public Production addProduction(Production production) throws TagException;
    public Production deleteProduction(Long id) throws TagException;
    public List<Production> getAllProductions();
}
