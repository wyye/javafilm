/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.controllers;

import wyye.javafilm.entities.Film;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author ad
 */
@ManagedBean
@ViewScoped
public class FilmController implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @Resource
    UserTransaction utx;

    public List<Film> getFilms() {
        return em.createQuery("select f from Film f", Film.class).getResultList();
    }

    public void deleteFilm(Long filmId) {
        if (filmId == null) {
            return;
        }
        try {
            utx.begin();
            Film film = em.find(Film.class, filmId);
            if (film != null) {
                em.remove(film);
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

    public void createFilm(FilmEditController newFilm) {
        if (newFilm == null || newFilm.getName() == null || newFilm.getName().isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Film name is not specified", ""));
            return;
        }
        String name = newFilm.getName();
        try {
            utx.begin();
            Film film = new Film();
            film.setId(newFilm.getId());
            film.setName(newFilm.getName());
            film.setDuration(newFilm.getDuration());
            film.setDescription(newFilm.getDescription());
            film.setRelease(newFilm.getRelease());
            film.setStatus(newFilm.getStatus());
            film.setIsSerial(newFilm.getIsSerial());
            em.persist(film);
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
