package mov.ui;

import mov.cbr.CBREngine;
import mov.cbr.CaseRepresentation;
import mov.fuzzy.FuzzyQualityEvaluator;
import mov.model.Movie;
import mov.model.Person;
import mov.ontology.MovieRecommender;
import mov.ontology.OntologyManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    
    private static OntologyManager ontologyManager;
    private static MovieRecommender movieRecommender;
    private static FuzzyQualityEvaluator fuzzyEvaluator;
    private static CBREngine cbrEngine;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        System.out.println("=== Movie Recommendation System ===\n");
        
        // Initialize components
        initializeSystem();
        
        // Main menu loop
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    recommendMovies();
                    break;
                case 2:
                    evaluateMovieQuality();
                    break;
                case 3:
                    findSimilarMovies();
                    break;
                case 4:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.\n");
            }
        }
        
        scanner.close();
    }
    
    private static void initializeSystem() {
        System.out.println("Initializing system...\n");
        
        // Initialize ontology manager
        ontologyManager = new OntologyManager();
        
        try {
            // Load ontology (you'll need to create this file first)
            String ontologyPath = "src/main/resources/ontology/movies.owl";
            ontologyManager.loadOntology(ontologyPath);
            
            // Initialize recommender
            movieRecommender = new MovieRecommender(ontologyManager);
            
            // Initialize fuzzy evaluator
            fuzzyEvaluator = new FuzzyQualityEvaluator();
            String fuzzyConfigPath = "src/main/resources/fuzzy/Quality.fcl";
            fuzzyEvaluator.loadFuzzySystem(fuzzyConfigPath);
            
            // Initialize CBR engine
            cbrEngine = new CBREngine();
            List<Movie> allMovies = movieRecommender.getAllMovies();
            cbrEngine.loadCases(allMovies);
            
            System.out.println("System initialized successfully!\n");
            
        } catch (Exception e) {
            System.err.println("Error initializing system: " + e.getMessage());
            System.err.println("Some features may not work properly.\n");
        }
    }
    
    private static void printMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Recommend movies (Ontology + SPARQL)");
        System.out.println("2. Evaluate movie quality (Fuzzy Logic)");
        System.out.println("3. Find similar movies (CBR)");
        System.out.println("4. Exit");
        System.out.println();
    }
    
    private static void recommendMovies() {
        System.out.println("\n=== Movie Recommendation ===");
        System.out.println("1. By genre");
        System.out.println("2. By director");
        System.out.println("3. By actor");
        System.out.println("4. By year range");
        
        int choice = getIntInput("Choose search type: ");
        List<Movie> results = null;
        
        switch (choice) {
            case 1:
                String genre = getStringInput("Enter genre: ");
                results = movieRecommender.recommendByGenre(genre);
                break;
            case 2:
                String director = getStringInput("Enter director name: ");
                results = movieRecommender.recommendByDirector(director);
                break;
            case 3:
                String actor = getStringInput("Enter actor name: ");
                results = movieRecommender.recommendByActor(actor);
                break;
            case 4:
                int startYear = getIntInput("Enter start year: ");
                int endYear = getIntInput("Enter end year: ");
                results = movieRecommender.recommendByYearRange(startYear, endYear);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        displayMovies(results);
    }
    
    private static void evaluateMovieQuality() {
        System.out.println("\n=== Movie Quality Evaluation (Fuzzy Logic) ===");
        System.out.println("Rate the following criteria (0-10):\n");
        
        double directing = getDoubleInput("Directing quality: ");
        double acting = getDoubleInput("Acting quality: ");
        double screenplay = getDoubleInput("Screenplay quality: ");
        double visualEffects = getDoubleInput("Visual effects quality: ");
        double culturalSignificance = getDoubleInput("Cultural significance: ");
        
        double qualityScore = fuzzyEvaluator.evaluateQuality(
            directing, acting, screenplay, visualEffects, culturalSignificance
        );
        
        String qualityLabel = fuzzyEvaluator.getQualityLabel(qualityScore);
        
        System.out.println("\n--- Results ---");
        System.out.println("Quality Score: " + String.format("%.2f", qualityScore) + "/10");
        System.out.println("Quality Rating: " + qualityLabel);
    }
    
    private static void findSimilarMovies() {
        System.out.println("\n=== Find Similar Movies (CBR) ===");
        
        String movieTitle = getStringInput("Enter movie title: ");
        int k = getIntInput("How many similar movies to show: ");
        
        // Find the target movie in case base
        Movie targetMovie = null;
        for (CaseRepresentation caseRep : cbrEngine.getCaseBase()) {
            if (caseRep.getMovie().getTitle().equalsIgnoreCase(movieTitle)) {
                targetMovie = caseRep.getMovie();
                break;
            }
        }
        
        if (targetMovie == null) {
            System.out.println("Movie not found in database.");
            return;
        }
        
        List<CaseRepresentation> similarCases = cbrEngine.findSimilarCases(targetMovie, k);
        
        System.out.println("\n--- Similar Movies ---");
        for (int i = 0; i < similarCases.size(); i++) {
            CaseRepresentation caseRep = similarCases.get(i);
            Movie movie = caseRep.getMovie();
            double similarity = caseRep.getSimilarity();
            
            System.out.println((i + 1) + ". " + movie.getTitle() + 
                             " (" + movie.getYear() + ") - Similarity: " + 
                             String.format("%.2f%%", similarity * 100));
        }
    }
    
    private static void displayMovies(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            System.out.println("\nNo movies found.");
            return;
        }
        
        System.out.println("\n--- Results (" + movies.size() + " movies) ---");
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle() + 
                             (movie.getYear() > 0 ? " (" + movie.getYear() + ")" : ""));
        }
    }
    
    // Helper methods for input
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < 0 || value > 10) {
                    System.out.println("Please enter a value between 0 and 10.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}