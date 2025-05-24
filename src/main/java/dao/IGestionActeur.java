package dao;

import java.util.List;
import entity.Acteur;

public interface IGestionActeur {
    void addActeur(Acteur a);
    List<Acteur> getAllActeurs();
    List<Acteur> getActeursByMc(String mc);
    Acteur getActeur(int id);
    void deleteActeur(int id);
    
    void updateActeur(Acteur a);
    List<Acteur> getActeursByIds(List<Integer> ids);
}