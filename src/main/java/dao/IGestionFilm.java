package dao;

import java.util.List;
import entity.Film;

public interface IGestionFilm {
    void addFilm(Film f);
    List<Film> getAllFilms();
    List<Film> getFilmsByMc(String mc);
    Film getFilm(int id);
    void deleteFilm(int id);
    void updateFilm(Film f);
    List<Film> getFilmsByIds(List<Integer> ids);
    
    

}
