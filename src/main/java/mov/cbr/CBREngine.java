package mov.cbr;

import mov.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Case-Based Reasoning engine for finding similar movies
 */
public class CBREngine {
    
    private List<CaseRepresentation> caseBase;
    private SimilarityCalculator similarityCalculator;
    
    public CBREngine() {
        this.caseBase = new ArrayList<>();
        this.similarityCalculator = new SimilarityCalculator();
    }
    
    /**
     * Load cases from a list of movies
     */
    public void loadCases(List<Movie> movies) {
        caseBase.clear();
        for (Movie movie : movies) {
            CaseRepresentation caseRep = new CaseRepresentation(movie);
            caseBase.add(caseRep);
        }
        System.out.println("Loaded " + caseBase.size() + " cases into CBR system");
    }
    
    /**
     * Add a single case to the case base
     */
    public void addCase(Movie movie) {
        CaseRepresentation caseRep = new CaseRepresentation(movie);
        caseBase.add(caseRep);
    }
    
    /**
     * Find similar movies to the target movie
     * 
     * @param targetMovie The movie to compare against
     * @param k Number of similar movies to return
     * @return List of k most similar movies
     */
    public List<Movie> findSimilarMovies(Movie targetMovie, int k) {
        if (caseBase.isEmpty()) {
            System.err.println("Case base is empty. Load cases first.");
            return new ArrayList<>();
        }
        
        // Calculate similarity for each case
        List<CaseRepresentation> rankedCases = new ArrayList<>();
        
        for (CaseRepresentation caseRep : caseBase) {
            // Skip the target movie itself
            if (caseRep.getMovie().getTitle().equals(targetMovie.getTitle())) {
                continue;
            }
            
            double similarity = similarityCalculator.calculateSimilarity(targetMovie, caseRep.getMovie());
            caseRep.setSimilarity(similarity);
            rankedCases.add(caseRep);
        }
        
        // Sort by similarity (descending)
        Collections.sort(rankedCases, new Comparator<CaseRepresentation>() {
            @Override
            public int compare(CaseRepresentation c1, CaseRepresentation c2) {
                return Double.compare(c2.getSimilarity(), c1.getSimilarity());
            }
        });
        
        // Return top k results
        List<Movie> similarMovies = new ArrayList<>();
        int limit = Math.min(k, rankedCases.size());
        
        for (int i = 0; i < limit; i++) {
            similarMovies.add(rankedCases.get(i).getMovie());
        }
        
        return similarMovies;
    }
    
    /**
     * Find similar movies with similarity scores
     */
    public List<CaseRepresentation> findSimilarCases(Movie targetMovie, int k) {
        if (caseBase.isEmpty()) {
            System.err.println("Case base is empty. Load cases first.");
            return new ArrayList<>();
        }
        
        // Calculate similarity for each case
        List<CaseRepresentation> rankedCases = new ArrayList<>();
        
        for (CaseRepresentation caseRep : caseBase) {
            // Skip the target movie itself
            if (caseRep.getMovie().getTitle().equals(targetMovie.getTitle())) {
                continue;
            }
            
            double similarity = similarityCalculator.calculateSimilarity(targetMovie, caseRep.getMovie());
            caseRep.setSimilarity(similarity);
            rankedCases.add(caseRep);
        }
        
        // Sort by similarity (descending)
        Collections.sort(rankedCases, new Comparator<CaseRepresentation>() {
            @Override
            public int compare(CaseRepresentation c1, CaseRepresentation c2) {
                return Double.compare(c2.getSimilarity(), c1.getSimilarity());
            }
        });
        
        // Return top k results
        int limit = Math.min(k, rankedCases.size());
        return rankedCases.subList(0, limit);
    }
    
    /**
     * Get all cases in the case base
     */
    public List<CaseRepresentation> getCaseBase() {
        return caseBase;
    }
    
    /**
     * Get the number of cases in the case base
     */
    public int getCaseCount() {
        return caseBase.size();
    }
}