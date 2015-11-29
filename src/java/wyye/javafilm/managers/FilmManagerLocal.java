/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.managers;

import java.util.List;
import javax.ejb.Local;
import wyye.javafilm.entities.Film;
import wyye.javafilm.exceptions.TagException;

/**
 *
 * @author ad
 */
@Local
public interface FilmManagerLocal {

    public Film getFilmById(Long filmId);
    public Film addFilm(Film film) throws TagException;
    public Film deleteFilm(Long filmID) throws TagException;
    public List<Film> getAllFilms();
    public List<Object[]> getFilmCountries() throws TagException ;
}
