package mov.ontology;

import mov.model.Movie;
import mov.model.Person;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieRecommender {

    private OntologyManager ontologyManager;

    public MovieRecommender(OntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    public List<Movie> recommendByGenre(String genre) {
        String queryString =
                "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n" +
                        "WHERE { \n" +
                        "  ?movie rdf:type movie:Movie . \n" +
                        "  ?movie movie:hasGenre ?g . \n" +
                        "  ?g movie:genreName \"" + genre + "\" . \n" +
                        "  ?movie movie:title ?title . \n" +
                        "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
                        "  OPTIONAL { ?movie movie:imdbScore ?score } \n" +
                        "  OPTIONAL { ?movie movie:runtime ?runtime } \n" +
                        "  OPTIONAL { ?movie movie:hasDirector ?dir . ?dir movie:personName ?directorName } \n" +
                        "  OPTIONAL { ?movie movie:hasGenre ?genre2 . ?genre2 movie:genreName ?genreName } \n" +
                        "}";
        return executeRichMovieQuery(queryString);
    }

    public List<Movie> recommendByDirector(String directorName) {
        String queryString =
                "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n" +
                        "WHERE { \n" +
                        "  ?movie rdf:type movie:Movie . \n" +
                        "  ?movie movie:hasDirector ?d . \n" +
                        "  ?d movie:personName \"" + directorName + "\" . \n" +
                        "  ?movie movie:title ?title . \n" +
                        "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
                        "  OPTIONAL { ?movie movie:imdbScore ?score } \n" +
                        "  OPTIONAL { ?movie movie:runtime ?runtime } \n" +
                        "  OPTIONAL { ?movie movie:hasDirector ?dir . ?dir movie:personName ?directorName } \n" +
                        "  OPTIONAL { ?movie movie:hasGenre ?genre . ?genre movie:genreName ?genreName } \n" +
                        "}";
        return executeRichMovieQuery(queryString);
    }

    public List<Movie> recommendByActor(String actorName) {
        String queryString =
                "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n" +
                        "WHERE { \n" +
                        "  ?movie rdf:type movie:Movie . \n" +
                        "  ?movie movie:hasActor ?a . \n" +
                        "  ?a movie:personName \"" + actorName + "\" . \n" +
                        "  ?movie movie:title ?title . \n" +
                        "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
                        "  OPTIONAL { ?movie movie:imdbScore ?score } \n" +
                        "  OPTIONAL { ?movie movie:runtime ?runtime } \n" +
                        "  OPTIONAL { ?movie movie:hasDirector ?dir . ?dir movie:personName ?directorName } \n" +
                        "  OPTIONAL { ?movie movie:hasGenre ?genre . ?genre movie:genreName ?genreName } \n" +
                        "}";
        return executeRichMovieQuery(queryString);
    }

    public List<Movie> recommendByYearRange(int startYear, int endYear) {
        String queryString =
                "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n" +
                        "WHERE { \n" +
                        "  ?movie rdf:type movie:Movie . \n" +
                        "  ?movie movie:title ?title . \n" +
                        "  ?movie movie:releaseYear ?year . \n" +
                        "  FILTER (?year >= " + startYear + " && ?year <= " + endYear + ") \n" +
                        "  OPTIONAL { ?movie movie:imdbScore ?score } \n" +
                        "  OPTIONAL { ?movie movie:runtime ?runtime } \n" +
                        "  OPTIONAL { ?movie movie:hasDirector ?dir . ?dir movie:personName ?directorName } \n" +
                        "  OPTIONAL { ?movie movie:hasGenre ?genre . ?genre movie:genreName ?genreName } \n" +
                        "}";
        return executeRichMovieQuery(queryString);
    }

    public List<Movie> recommendByMultipleCriteria(String genre, String director, Integer minYear) {
        StringBuilder query = new StringBuilder();
        query.append("PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n");
        query.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n");
        query.append("SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n");
        query.append("WHERE { \n");
        query.append("  ?movie rdf:type movie:Movie . \n");
        query.append("  ?movie movie:title ?title . \n");

        if (genre != null && !genre.isEmpty()) {
            query.append("  ?movie movie:hasGenre ?g . \n");
            query.append("  ?g movie:genreName \"" + genre + "\" . \n");
        }
        if (director != null && !director.isEmpty()) {
            query.append("  ?movie movie:hasDirector ?d . \n");
            query.append("  ?d movie:personName \"" + director + "\" . \n");
        }
        if (minYear != null) {
            query.append("  ?movie movie:releaseYear ?year . \n");
            query.append("  FILTER (?year >= " + minYear + ") \n");
        } else {
            query.append("  OPTIONAL { ?movie movie:releaseYear ?year } \n");
        }
        query.append("  OPTIONAL { ?movie movie:imdbScore ?score } \n");
        query.append("  OPTIONAL { ?movie movie:runtime ?runtime } \n");
        query.append("  OPTIONAL { ?movie movie:hasDirector ?dir . ?dir movie:personName ?directorName } \n");
        query.append("  OPTIONAL { ?movie movie:hasGenre ?genre2 . ?genre2 movie:genreName ?genreName } \n");
        query.append("}");
        return executeRichMovieQuery(query.toString());
    }

    /**
     * Get all movies with full data for CBR (genres, directors, rating)
     */
    public List<Movie> getAllMovies() {
        String queryString =
                "PREFIX movie: <http://www.semanticweb.org/ontologies/movie#> \n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
                        "SELECT ?movie ?title ?year ?score ?directorName ?genreName ?runtime \n" +
                        "WHERE { \n" +
                        "  ?movie rdf:type movie:Movie . \n" +
                        "  ?movie movie:title ?title . \n" +
                        "  OPTIONAL { ?movie movie:releaseYear ?year } \n" +
                        "  OPTIONAL { ?movie movie:imdbScore ?score } \n" +
                        "  OPTIONAL { ?movie movie:runtime ?runtime } \n" +
                        "  OPTIONAL { ?movie movie:hasDirector ?director . ?director movie:personName ?directorName } \n" +
                        "  OPTIONAL { ?movie movie:hasGenre ?genre . ?genre movie:genreName ?genreName } \n" +
                        "}";
        return executeRichMovieQuery(queryString);
    }

    /**
     * Executes a SPARQL query and returns fully populated Movie objects
     * (title, year, imdbScore, runtime, directors, genres)
     */
    private List<Movie> executeRichMovieQuery(String queryString) {
        Map<String, Movie> movieMap = new LinkedHashMap<>();
        try {
            ResultSet results = ontologyManager.executeSPARQLQuery(queryString);
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                String uri = solution.getResource("movie").getURI();

                Movie movie = movieMap.getOrDefault(uri, new Movie());
                movie.setUri(uri);
                movie.setTitle(solution.getLiteral("title").getString());

                if (solution.contains("year"))
                    movie.setYear(solution.getLiteral("year").getInt());
                if (solution.contains("score"))
                    movie.setRating(solution.getLiteral("score").getDouble());
                if (solution.contains("runtime"))
                    movie.setRuntime(solution.getLiteral("runtime").getInt());
                if (solution.contains("directorName")) {
                    String dName = solution.getLiteral("directorName").getString();
                    boolean exists = movie.getDirectors().stream().anyMatch(d -> d.getName().equals(dName));
                    if (!exists) movie.addDirector(new Person(dName));
                }
                if (solution.contains("genreName")) {
                    String g = solution.getLiteral("genreName").getString();
                    if (!movie.getGenres().contains(g)) movie.addGenre(g);
                }

                movieMap.put(uri, movie);
            }
        } catch (Exception e) {
            System.err.println("Error executing SPARQL query: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>(movieMap.values());
    }
}