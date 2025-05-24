# 🎬 Gestion Films - Application Web JEE

Une application web dynamique de gestion de films développée avec les technologies JEE (Servlets, JSP, JSTL, EL) et JPA/Hibernate, suivant une architecture MVC.


## 📋 Description du Projet

Cette application permet la gestion complète d'une base de données de films avec leurs catégories et acteurs. Elle implémente un système CRUD complet pour chaque entité et respecte les spécifications techniques du module JEE.

## 🎯 Fonctionnalités

### 🎭 Gestion des Films
- **Créer un film** - Ajouter de nouveaux films avec détails complets
- **Lire/Afficher** - Visualiser la liste des films et leurs détails
- **Modifier** - Mettre à jour les informations des films existants
- **Supprimer** - Retirer des films de la base de données
- **Association** - Lier les films aux catégories et acteurs

### 📂 Gestion des Catégories
- **CRUD complet** - Créer, lire, modifier, supprimer des catégories
- **Classification** - Organiser les films par genres/catégories
- **Relations** - Gérer les associations films-catégories

### 🎪 Gestion des Acteurs
- **CRUD complet** - Gestion complète des acteurs
- **Filmographie** - Visualiser les films par acteur
- **Casting** - Associer les acteurs aux films

### 🔗 Fonctionnalités Relationnelles
- **Films ↔ Catégories** - Relation Many-to-Many
- **Films ↔ Acteurs** - Relation Many-to-Many
- **Recherche et filtrage** - Par catégorie, acteur, ou titre
- **Vues associatives** - Affichage des relations entre entités

## 🏗️ Architecture Technique

### 📐 Modèle de Données (Entités JPA)

```
📊 BASE DE DONNÉES
├── 🎬 Film
│   ├── id (Long)
│   ├── titre (String)
│   ├── description (String)
│   ├── dateRealisation (Date)
│   └── duree (Integer)
│   
│
├── 📂 Category
│   ├── id (Long)
│   ├── nom (String)
│   └── description (String)
│
└── 🎭 Actor
    ├── id (Long)
    ├── nom (String)
    └── prenom (String)
```

### 🔗 Relations JPA/Hibernate

```java
// Film Entity - Relations Many-to-Many
@Entity
public class Film {
    @ManyToMany
    @JoinTable(name = "film_category")
    private Set<Category> categories;
    
    @ManyToMany
    @JoinTable(name = "film_actor")
    private Set<Actor> actors;
}
```

## 🛠️ Stack Technique

| Couche | Technologie | Version |
|--------|-------------|---------|
| **Frontend** | JSP + JSTL + EL | - |
| **Backend** | Java Servlets | Java EE 8 |
| **Persistance** | JPA + Hibernate | 5.4+ |
| **Base de données** | MySQL | 8.0+ |
| **Build** | Maven | 3.6+ |
| **Serveur** | Apache Tomcat | 9.0+ |


## 🚀 Installation et Configuration

### Prérequis
- **JDK 8+** - Java Development Kit
- **Apache Tomcat 9+** - Serveur d'application
- **MySQL 8+** - Base de données
- **Maven 3.6+** - Gestionnaire de dépendances
- **IDE** - Eclipse/IntelliJ IDEA

### 1. Cloner le Projet
```bash
git clone https://github.com/EyaDhouib22/gestion-films-jee.git
cd gestion-films-jee
```

### 2. Configuration Base de Données
```sql
-- Créer la base de données
CREATE DATABASE gestion_films;
USE gestion_films;

-- Les tables seront créées automatiquement par Hibernate
```

### 3. Configuration JPA (persistence.xml)
```xml
<persistence-unit name="gestionFilmsPU">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <properties>
        <property name="javax.persistence.jdbc.url" 
                  value="jdbc:mysql://localhost:3306/gestion_films"/>
        <property name="javax.persistence.jdbc.user" value="root"/>
        <property name="javax.persistence.jdbc.password" value="password"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <property name="hibernate.dialect" 
                  value="org.hibernate.dialect.MySQL8Dialect"/>
    </properties>
</persistence-unit>
```

### 4. Build et Déploiement
```bash
# Compiler le projet
mvn clean compile

# Créer le WAR
mvn package

# Déployer sur Tomcat
cp target/gestion-films.war $TOMCAT_HOME/webapps/
```

### 5. Lancer l'Application
```bash
# Démarrer Tomcat
$TOMCAT_HOME/bin/startup.sh

# Accéder à l'application
http://localhost:8080/gestion-films
```

## 🔧 Configuration Maven (pom.xml)

```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSP API -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>
        <version>2.2</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- JSTL -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- Hibernate -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.4.32.Final</version>
    </dependency>
    
    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
</dependencies>
```

## 🎨 Interface Utilisateur

### Pages Principales
- **🏠 Accueil** - Dashboard avec statistiques
- **🎬 Films** - Liste, création, modification des films
- **📂 Catégories** - Gestion des genres de films
- **🎭 Acteurs** - Gestion du casting
- **🔗 Associations** - Liaison films-catégories-acteurs

### Fonctionnalités Interface
- **Navigation intuitive** avec menu responsive
- **Formulaires dynamiques** avec validation
- **Tableaux interactifs** pour l'affichage des données
- **Recherche et filtrage** en temps réel
- **Messages de confirmation** pour les actions CRUD

## 🧪 Tests et Validation

### Tests Fonctionnels
- [ ] Création, lecture, modification, suppression des films
- [ ] Gestion complète des catégories
- [ ] Gestion complète des acteurs
- [ ] Relations Many-to-Many fonctionnelles
- [ ] Intégrité des données
- [ ] Navigation et interface utilisateur

### Validation Technique
- [x] Architecture MVC respectée
- [x] Pas de frameworks externes (Spring exclu)
- [x] JPA/Hibernate pour la persistance
- [x] Relations OneToMany et ManyToMany implémentées
- [x] Structure Maven appropriée

## 📄 License

Projet développé dans le cadre du module JEE - Utilisation académique uniquement.
