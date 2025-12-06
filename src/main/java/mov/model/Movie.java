package mov.model;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String uri; // unique identifier from ontology
    private String title;
    private int year;
    private List<String> genres; // Keep as String for simplicity (genre names)
    private List<Person> directors;
    private List<Person> actors;
    private List<Person> writers;
    private double rating; // IMDb rating or similar
    private int runtime; // in minutes
    private String country;
    private List<String> awards;
    
    // Constructor
    public Movie() {
        this.genres = new ArrayList<>();
        this.directors = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.writers = new ArrayList<>();
        this.awards = new ArrayList<>();
    }
    
    public Movie(String title) {
        this();
        this.title = title;
    }
    
    // Getters and Setters
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public List<String> getGenres() {
        return genres;
    }
    
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    
    public void addGenre(String genre) {
        this.genres.add(genre);
    }
    
    public List<Person> getDirectors() {
        return directors;
    }
    
    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }
    
    public void addDirector(Person director) {
        this.directors.add(director);
    }
    
    public List<Person> getActors() {
        return actors;
    }
    
    public void setActors(List<Person> actors) {
        this.actors = actors;
    }
    
    public void addActor(Person actor) {
        this.actors.add(actor);
    }
    
    public List<Person> getWriters() {
        return writers;
    }
    
    public void setWriters(List<Person> writers) {
        this.writers = writers;
    }
    
    public void addWriter(Person writer) {
        this.writers.add(writer);
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getRuntime() {
        return runtime;
    }
    
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public List<String> getAwards() {
        return awards;
    }
    
    public void setAwards(List<String> awards) {
        this.awards = awards;
    }
    
    public void addAward(String award) {
        this.awards.add(award);
    }
    
    @Override
    public String toString() {
        return "Movie [title=" + title + ", year=" + year + ", genres=" + genres + 
               ", directors=" + directors + ", rating=" + rating + "]";
    }
}