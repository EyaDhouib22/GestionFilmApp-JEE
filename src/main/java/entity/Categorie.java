package entity;

import java.util.HashSet; 
import java.util.Set;     
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Film> films = new HashSet<>();

    public Categorie() {
        super();
    }

    public Categorie(String nom) {
        super();
        this.nom = nom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Set<Film> getFilms() { return films; }
    public void setFilms(Set<Film> films) { this.films = films; }

    @Override
    public String toString() {
        return "Categorie [id=" + id + ", nom=" + nom + "]"; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return id != 0 && id == categorie.id;
    }

    @Override
    public int hashCode() {
        return id != 0 ? Objects.hash(id) : super.hashCode();
    }
}