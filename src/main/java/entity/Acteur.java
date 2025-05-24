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
public class Acteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;

    @ManyToMany(mappedBy = "acteurs", fetch = FetchType.LAZY)
    private Set<Film> films = new HashSet<>(); 

    public Acteur() {
        super();
    }

    public Acteur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public Set<Film> getFilms() { return films; }
    public void setFilms(Set<Film> films) { this.films = films; }

    @Override
    public String toString() {
        return "Acteur [id=" + id + ", nom=" + nom + ", prenom=" + prenom + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acteur acteur = (Acteur) o;
        return id != 0 && id == acteur.id;
    }

    @Override
    public int hashCode() {
        return id != 0 ? Objects.hash(id) : super.hashCode();
    }
}