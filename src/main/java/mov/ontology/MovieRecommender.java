package mov.ontology;

import mov.model.Movie;
import mov.model.Person;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class MovieRecommender {
    
    private OntologyManager ontologyManager;
    
    public MovieRecommender(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }
    
    /**
     * Recommend movies by genre
     */
    public List<Movie> recommendByGenre(String genre) {
        String queryString = 
            "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "SELECT ?movie ?title ?year \n" +
            "WHERE { \n" +
            "  ?movie rdf:type movie:Movie . \n" +
            "  ?movie movie:hasGenre ?genre . \n" +
            "  ?genre movie:genreName \"" + genre + "\" . \n" +
            "  ?movie movie:title ?title . \n" +
            "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
            "}";
        
        return executeMovieQuery(queryString);
    }
    
    /**
     * Recommend movies by director
     */
    public List<Movie> recommendByDirector(String directorName) {
        String queryString = 
            "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "SELECT ?movie ?title ?year \n" +
            "WHERE { \n" +
            "  ?movie rdf:type movie:Movie . \n" +
            "  ?movie movie:hasDirector ?director . \n" +
            "  ?director movie:personName \"" + directorName + "\" . \n" +
            "  ?movie movie:title ?title . \n" +
            "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
            "}";
        
        return executeMovieQuery(queryString);
    }
    
    /**
     * Recommend movies by actor
     */
    public List<Movie> recommendByActor(String actorName) {
        String queryString = 
            "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "SELECT ?movie ?title ?year \n" +
            "WHERE { \n" +
            "  ?movie rdf:type movie:Movie . \n" +
            "  ?movie movie:hasActor ?actor . \n" +
            "  ?actor movie:personName \"" + actorName + "\" . \n" +
            "  ?movie movie:title ?title . \n" +
            "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
            "}";
        
        return executeMovieQuery(queryString);
    }
    
    /**
     * Recommend movies by year range
     */
    public List<Movie> recommendByYearRange(int startYear, int endYear) {
        String queryString = 
            "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "SELECT ?movie ?title ?year \n" +
            "WHERE { \n" +
            "  ?movie rdf:type movie:Movie . \n" +
            "  ?movie movie:title ?title . \n" +
            "  ?movie movie:releaseYear ?year . \n" +
            "  FILTER (?year >= " + startYear + " && ?year <= " + endYear + ") \n" +
            "}";
        
        return executeMovieQuery(queryString);
    }
    
    /**
     * Recommend movies by multiple criteria
     */
    public List<Movie> recommendByMultipleCriteria(String genre, String director, Integer minYear) {
        StringBuilder query = new StringBuilder();
        query.append("PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n");
        query.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
        query.append("SELECT ?movie ?title ?year \n");
        query.append("WHERE { \n");
        query.append("  ?movie rdf:type movie:Movie . \n");
        query.append("  ?movie movie:title ?title . \n");
        
        if (genre != null && !genre.isEmpty()) {
            query.append("  ?movie movie:hasGenre ?genre . \n");
            query.append("  ?genre movie:genreName \"" + genre + "\" . \n");
        }
        
        if (director != null && !director.isEmpty()) {
            query.append("  ?movie movie:hasDirector ?director . \n");
            query.append("  ?director movie:personName \"" + director + "\" . \n");
        }
        
        if (minYear != null) {
            query.append("  ?movie movie:releaseYear ?year . \n");
            query.append("  FILTER (?year >= " + minYear + ") \n");
        } else {
            query.append("  OPTIONAL { ?movie movie:releaseYear ?year } \n");
        }
        
        query.append("}");
        
        return executeMovieQuery(query.toString());
    }
    
    /**
     * Execute a SPARQL query and convert results to Movie objects
     */
    private List<Movie> executeMovieQuery(String queryString) {
        List<Movie> movies = new ArrayList<>();
        
        try {
            ResultSet results = ontologyManager.executeSPARQLQuery(queryString);
            
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                
                Movie movie = new Movie();
                movie.setUri(solution.getResource("movie").getURI());
                movie.setTitle(solution.getLiteral("title").getString());
                
                if (solution.contains("year")) {
                    movie.setYear(solution.getLiteral("year").getInt());
                }
                
                movies.add(movie);
            }
            
        } catch (Exception e) {
            System.err.println("Error executing SPARQL query: " + e.getMessage());
            e.printStackTrace();
        }
        
        return movies;
    }
    
    /**
     * Get all movies from ontology
     */
    public List<Movie> getAllMovies() {
        String queryString = 
            "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
            "SELECT ?movie ?title ?year \n" +
            "WHERE { \n" +
            "  ?movie rdf:type movie:Movie . \n" +
            "  ?movie movie:title ?title . \n" +
            "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
            "}";
        
        return executeMovieQuery(queryString);
    }
}