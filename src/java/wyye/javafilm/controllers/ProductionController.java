/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import wyye.javafilm.entities.Production;

/**
 *
 * @author ad
 */
@ManagedBean
@ViewScoped
public class ProductionController implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction utx;

    public List<Production> getProductions() {
        return em.createQuery("select p from Production p", Production.class).getResultList();
    }

    public void deleteProduction(Long id) {
        if (id == null) {
            return;
        }
        try {
            utx.begin();
            Production production = em.find(Production.class, id);
            if (production != null) {
                em.remove(production);
            }
            utx.commit();
        } catch (Exception ex) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DB Error:", ex.getLocalizedMessage()));
            ex.printStackTrace(System.err);
            try {
                utx.rollback();
            } catch (Exception exc) {
                exc.printStackTrace(System.err);
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DB Error:", exc.getLocalizedMessage()));
            }
        }
    }

    public void createProduction(ProductionEditController newProduction) {
        if (newProduction == null || newProduction.getName() == null || newProduction.getName().isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Name is not specified", ""));
            return;
        }
        try {
            utx.begin();
            Production production = new Production();
            production.setId(newProduction.getId());
            production.setName(newProduction.getName());
            production.setCountry(newProduction.getCountry());
            em.persist(production);
            utx.commit();
        } catch (Exception ex) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "DB Error:", ex.getLocalizedMessage()));
            ex.printStackTrace(System.err);
            try {
                utx.rollback();
            } catch (Exception exc) {
                exc.printStackTrace(System.err);
            }
        }
    }
}
