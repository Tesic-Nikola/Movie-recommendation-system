package mov.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.io.File;

public class FuzzyQualityEvaluator {
    
    private FIS fis;
    
    /**
     * Load fuzzy logic configuration from FCL file
     */
    public void loadFuzzySystem(String fclFilePath) {
        try {
            fis = FIS.load(fclFilePath, true);
            
            if (fis == null) {
                throw new RuntimeException("Cannot load fuzzy system from: " + fclFilePath);
            }
            
            System.out.println("Fuzzy system loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Error loading fuzzy system: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Evaluate movie quality based on multiple criteria
     * 
     * @param directing Score for directing (0-10)
     * @param acting Score for acting (0-10)
     * @param screenplay Score for screenplay (0-10)
     * @param visualEffects Score for visual effects (0-10)
     * @param culturalSignificance Score for cultural significance (0-10)
     * @return Quality rating (0-10)
     */
    public double evaluateQuality(double directing, double acting, double screenplay, 
                                   double visualEffects, double culturalSignificance) {
        
        if (fis == null) {
            throw new IllegalStateException("Fuzzy system not loaded. Call loadFuzzySystem first.");
        }
        
        // Set input variables
        fis.setVariable("directing", directing);
        fis.setVariable("acting", acting);
        fis.setVariable("screenplay", screenplay);
        fis.setVariable("visualEffects", visualEffects);
        fis.setVariable("culturalSignificance", culturalSignificance);
        
        // Evaluate
        fis.evaluate();
        
        // Get output variable
        Variable quality = fis.getVariable("quality");
        
        return quality.getValue();
    }
    
    /**
     * Get quality as linguistic term (Bad, Good, Excellent)
     */
    public String getQualityLabel(double qualityScore) {
        if (qualityScore < 4.0) {
            return "Bad";
        } else if (qualityScore < 7.0) {
            return "Good";
        } else {
            return "Excellent";
        }
    }
    
    /**
     * Evaluate and return linguistic label
     */
    public String evaluateQualityLabel(double directing, double acting, double screenplay, 
                                        double visualEffects, double culturalSignificance) {
        double score = evaluateQuality(directing, acting, screenplay, visualEffects, culturalSignificance);
        return getQualityLabel(score);
    }
}