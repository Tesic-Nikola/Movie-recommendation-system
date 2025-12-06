package mov.cbr;

import mov.model.Movie;
import mov.model.Person;

import java.util.List;

/**
 * Represents a movie as a CBR case
 */
public class CaseRepresentation {
    
    private String id;
    private Movie movie;
    private double similarity; // Similarity score when comparing cases
    
    public CaseRepresentation(Movie movie) {
        this.movie = movie;
        this.id = movie.getUri();
        this.similarity = 0.0;
    }
    
    public CaseRepresentation(String id, Movie movie) {
        this.id = id;
        this.movie = movie;
        this.similarity = 0.0;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public double getSimilarity() {
        return similarity;
    }
    
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    
    @Override
    public String toString() {
        return "Case [id=" + id + ", movie=" + movie.getTitle() + ", similarity=" + similarity + "]";
    }
}