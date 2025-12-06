package mov.cbr;

import mov.model.Movie;
import mov.model.Person;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculate similarity between movies based on various attributes
 */
public class SimilarityCalculator {
    
    // Weights for different attributes
    private static final double GENRE_WEIGHT = 0.25;
    private static final double DIRECTOR_WEIGHT = 0.20;
    private static final double ACTOR_WEIGHT = 0.15;
    private static final double YEAR_WEIGHT = 0.15;
    private static final double RATING_WEIGHT = 0.15;
    private static final double COUNTRY_WEIGHT = 0.10;
    
    /**
     * Calculate overall similarity between two movies
     */
    public double calculateSimilarity(Movie movie1, Movie movie2) {
        double genreSim = calculateGenreSimilarity(movie1.getGenres(), movie2.getGenres());
        double directorSim = calculateDirectorSimilarity(movie1.getDirectors(), movie2.getDirectors());
        double actorSim = calculateActorSimilarity(movie1.getActors(), movie2.getActors());
        double yearSim = calculateYearSimilarity(movie1.getYear(), movie2.getYear());
        double ratingSim = calculateRatingSimilarity(movie1.getRating(), movie2.getRating());
        double countrySim = calculateCountrySimilarity(movie1.getCountry(), movie2.getCountry());
        
        double totalSimilarity = 
            (genreSim * GENRE_WEIGHT) +
            (directorSim * DIRECTOR_WEIGHT) +
            (actorSim * ACTOR_WEIGHT) +
            (yearSim * YEAR_WEIGHT) +
            (ratingSim * RATING_WEIGHT) +
            (countrySim * COUNTRY_WEIGHT);
        
        return totalSimilarity;
    }
    
    /**
     * Calculate genre similarity using Jaccard coefficient
     */
    private double calculateGenreSimilarity(List<String> genres1, List<String> genres2) {
        if (genres1 == null || genres2 == null || genres1.isEmpty() || genres2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> set1 = new HashSet<>(genres1);
        Set<String> set2 = new HashSet<>(genres2);
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Calculate director similarity
     */
    private double calculateDirectorSimilarity(List<Person> directors1, List<Person> directors2) {
        if (directors1 == null || directors2 == null || directors1.isEmpty() || directors2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> names1 = new HashSet<>();
        for (Person p : directors1) {
            names1.add(p.getName());
        }
        
        Set<String> names2 = new HashSet<>();
        for (Person p : directors2) {
            names2.add(p.getName());
        }
        
        Set<String> intersection = new HashSet<>(names1);
        intersection.retainAll(names2);
        
        return intersection.isEmpty() ? 0.0 : 1.0; // Binary: same director or not
    }
    
    /**
     * Calculate actor similarity using Jaccard coefficient
     */
    private double calculateActorSimilarity(List<Person> actors1, List<Person> actors2) {
        if (actors1 == null || actors2 == null || actors1.isEmpty() || actors2.isEmpty()) {
            return 0.0;
        }
        
        Set<String> names1 = new HashSet<>();
        for (Person p : actors1) {
            names1.add(p.getName());
        }
        
        Set<String> names2 = new HashSet<>();
        for (Person p : actors2) {
            names2.add(p.getName());
        }
        
        Set<String> intersection = new HashSet<>(names1);
        intersection.retainAll(names2);
        
        Set<String> union = new HashSet<>(names1);
        union.addAll(names2);
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * Calculate year similarity (closer years = higher similarity)
     */
    private double calculateYearSimilarity(int year1, int year2) {
        if (year1 == 0 || year2 == 0) {
            return 0.0;
        }
        
        int difference = Math.abs(year1 - year2);
        
        // Max difference of 20 years for scaling
        if (difference >= 20) {
            return 0.0;
        }
        
        return 1.0 - (difference / 20.0);
    }
    
    /**
     * Calculate rating similarity
     */
    private double calculateRatingSimilarity(double rating1, double rating2) {
        if (rating1 == 0.0 || rating2 == 0.0) {
            return 0.0;
        }
        
        double difference = Math.abs(rating1 - rating2);
        
        // Max difference of 5 points for scaling (assuming 0-10 scale)
        if (difference >= 5.0) {
            return 0.0;
        }
        
        return 1.0 - (difference / 5.0);
    }
    
    /**
     * Calculate country similarity (binary: same or different)
     */
    private double calculateCountrySimilarity(String country1, String country2) {
        if (country1 == null || country2 == null || country1.isEmpty() || country2.isEmpty()) {
            return 0.0;
        }
        
        return country1.equalsIgnoreCase(country2) ? 1.0 : 0.0;
    }
}