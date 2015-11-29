/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.services;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import wyye.javafilm.entities.Production;
import wyye.javafilm.exceptions.TagException;
import wyye.javafilm.managers.ProductionManagerLocal;


/**
 * REST Web Service
 *
 * @author ad
 */
@Path("Production")
@Stateless
public class ProductionResource {
    @EJB
    ProductionManagerLocal productionManager;

    /**
     * Creates a new instance of GenericResource
     */
    public ProductionResource() {
    }
    
    @GET
    @Path("getProduction")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductionById(@QueryParam("productionId") Long productionId) {
        Production production = productionManager.getProductionById(productionId);
        return (new Gson()).toJson(production);
    }
    
    @GET
    @Path("getAllProductions")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllProductions() {
            List<Production> productionList = productionManager.getAllProductions();
            return (new Gson()).toJson(productionList);
    }
    
    @POST
    @Path("addProduction")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addProduction(String data) throws TagException {
            Production production = new Gson().fromJson(data, Production.class);
            production = productionManager.addProduction(production);
            return (new Gson()).toJson(production);
    }
    
    @GET
    @Path("deleteProduction")
    public String deleteProduction(@QueryParam("productionId") Long productionId) throws TagException {
        return (new Gson()).toJson(productionManager.deleteProduction(productionId));
    }
}
