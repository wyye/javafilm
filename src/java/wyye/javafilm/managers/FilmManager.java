/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wyye.javafilm.managers;

import java.util.Collections;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.PersistenceContext;
import javax.persistence.SqlResultSetMapping;
import wyye.javafilm.entities.Film;
import wyye.javafilm.entities.Production;
import wyye.javafilm.exceptions.TagException;

/**
 *
 * @author ad
 */
@Stateless
public class FilmManager implements FilmManagerLocal {

    @PersistenceContext(unitName = "JavaFilmPU")
    private EntityManager em;

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            return null;
        }
        return em.find(Film.class, filmId);
    }

    @Override
    public Film addFilm(Film film) throws TagException {
        if (film == null) {
            throw new TagException("Film is not specified");
        }
        if (film.getName() == null) {
            throw new TagException("Film name is not specified");
        }
        em.persist(film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmList = em.createNativeQuery("select * from film order by name", Film.class).getResultList();
        if (filmList == null || filmList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return filmList;
    }

    @Override
    public Film deleteFilm(Long filmId) throws TagException {
        if (filmId == null) {
            throw new TagException("Film id not specified");
        }
        Film film = em.find(Film.class, filmId);
        if (film == null) {
            throw new TagException("Film not exist");
        }
        em.remove(film);
        return film;
    }

    @Override
    public List<Object[]> getFilmCountries() throws TagException {
        List<Object[]> joinList = em.createNativeQuery("select f.id, f.name, p.country "
                    + "from film f left outer join production p "
                    + "on f.productionId = p.Id order by f.name").getResultList();
        if (joinList == null || joinList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return joinList;
    }

    private Exception TAGException(String abc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
