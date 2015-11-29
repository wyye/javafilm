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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import wyye.javafilm.entities.Film;
import wyye.javafilm.entities.Production;
import wyye.javafilm.exceptions.TagException;
import wyye.javafilm.managers.FilmManagerLocal;

/**
 * REST Web Service
 *
 * @author ad
 */
@Path("Film")
@Stateless
public class FilmResource {
    @EJB
    FilmManagerLocal filmManager;

    /**
     * Creates a new instance of GenericResource
     */
    public FilmResource() {
    }
    
    // http://localhost:8080/JavaFilm/webresources/Film/getFilm?filmId=104
    @GET
    @Path("getFilm")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilmById(@QueryParam("filmId") Long filmId) {
        Film film = filmManager.getFilmById(filmId);
        return (new Gson()).toJson(film);
    }
    
    // http://localhost:8080/JavaFilm/webresources/Film/getAllFilms
    @GET
    @Path("getAllFilms")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllFilms() {
            List<Film> filmList = filmManager.getAllFilms();
            return (new Gson()).toJson(filmList);
    }
    
    @POST
    @Path("addFilm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addFilm(String data) throws TagException {
            Film film = new Gson().fromJson(data, Film.class);
            film = filmManager.addFilm(film);
            return (new Gson()).toJson(film);
    }
    
    @GET
    @Path("deleteFilm")
    public String deleteFilm(@QueryParam("filmId") Long filmId) throws TagException {
        return (new Gson()).toJson(filmManager.deleteFilm(filmId));
    }
    
    @GET
    @Path("getFilmCountries")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilmCountries() throws TagException {
            List<Object[]> filmList = filmManager.getFilmCountries();
            
            return (new Gson()).toJson(filmList);
    }
}
