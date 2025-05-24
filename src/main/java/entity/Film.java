package entity;

import java.util.HashSet; 
import java.util.Set;     
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titre;
    private String realisateur;
    private int anneeSortie;
    private int duree;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "film_categorie",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private Set<Categorie> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "film_acteur",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "acteur_id")
    )
    private Set<Acteur> acteurs = new HashSet<>(); 

    public Film() {
        super();
    }

    public Film(String titre, String realisateur, int anneeSortie, int duree) {
        this.titre = titre;
        this.realisateur = realisateur;
        this.anneeSortie = anneeSortie;
        this.duree = duree;
    }

    public Film(int id, String titre, String realisateur, int anneeSortie, int duree) {
        this.id = id;
        this.titre = titre;
        this.realisateur = realisateur;
        this.anneeSortie = anneeSortie;
        this.duree = duree;
    }

    public int getId() { return id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getRealisateur() { return realisateur; }
    public void setRealisateur(String realisateur) { this.realisateur = realisateur; }

    public int getAnneeSortie() { return anneeSortie; }
    public void setAnneeSortie(int anneeSortie) { this.anneeSortie = anneeSortie; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public Set<Categorie> getCategories() { return categories; }
    public void setCategories(Set<Categorie> categories) { this.categories = categories; }

    public Set<Acteur> getActeurs() { return acteurs; }
    public void setActeurs(Set<Acteur> acteurs) { this.acteurs = acteurs; }

    @Override
    public String toString() {
        Set<String> nomsCategories = new HashSet<>();
        if (categories != null) {
            for (Categorie cat : categories) {
                nomsCategories.add(cat != null ? cat.getNom() : "null");
            }
        }
        String categoriesStr = nomsCategories.isEmpty() ? "aucune" : String.join(", ", nomsCategories);

        Set<String> nomsActeurs = new HashSet<>();
        if (acteurs != null) {
            for (Acteur act : acteurs) {
                 nomsActeurs.add(act != null ? act.getPrenom() + " " + act.getNom() : "null");
            }
        }
        String acteursStr = nomsActeurs.isEmpty() ? "aucun" : String.join(", ", nomsActeurs);

        return "Film [id=" + id + ", titre=" + titre + ", realisateur=" + realisateur +
               ", anneeSortie=" + anneeSortie + ", duree=" + duree + " min" +
               ", categories=[" + categoriesStr + "]" +
               ", acteurs=[" + acteursStr + "]" +
               "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id != 0 && id == film.id; 
    }

    @Override
    public int hashCode() {
        return id != 0 ? Objects.hash(id) : super.hashCode(); 
    }

	public void setId(int int1) {
		this.id=id;		
	}
}