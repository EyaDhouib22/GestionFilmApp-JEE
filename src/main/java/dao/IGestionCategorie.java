package dao;

import java.util.List;
import entity.Categorie;

public interface IGestionCategorie {
    void addCategorie(Categorie c);
    List<Categorie> getAllCategories();
    List<Categorie> getCategoriesByMc(String mc);
    Categorie getCategorie(int id);
    void deleteCategorie(int id);
    void updateCategorie(Categorie c);
    
    List<Categorie> getCategoriesByIds(List<Integer> ids);
    
}