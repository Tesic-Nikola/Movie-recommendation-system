package mov.ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class OntologyManager {
    
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OntModel jenaModel; // For SPARQL queries
    private String ontologyIRI;
    
    public OntologyManager() {
        this.manager = OWLManager.createOWLOntologyManager();
        this.dataFactory = manager.getOWLDataFactory();
    }
    
    /**
     * Load ontology from file
     */
    public void loadOntology(String filePath) throws OWLOntologyCreationException {
        File file = new File(filePath);
        this.ontology = manager.loadOntologyFromOntologyDocument(file);
        this.ontologyIRI = ontology.getOntologyID().getOntologyIRI().get().toString();
        
        // Also load into Jena for SPARQL support
        loadIntoJena(filePath);
        
        System.out.println("Ontology loaded: " + ontologyIRI);
        System.out.println("Axioms count: " + ontology.getAxiomCount());
    }
    
    /**
     * Load ontology into Jena model for SPARQL queries
     */
    private void loadIntoJena(String filePath) {
        jenaModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        InputStream in = FileManager.get().open(filePath);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        jenaModel.read(in, null);
        System.out.println("Jena model loaded for SPARQL queries");
    }
    
    /**
     * Create a new empty ontology
     */
    public void createOntology(String iri) throws OWLOntologyCreationException {
        IRI ontologyIRI = IRI.create(iri);
        this.ontology = manager.createOntology(ontologyIRI);
        this.ontologyIRI = iri;
        System.out.println("New ontology created: " + iri);
    }
    
    /**
     * Save ontology to file
     */
    public void saveOntology(String filePath) throws Exception {
        File file = new File(filePath);
        manager.saveOntology(ontology, new FileOutputStream(file));
        System.out.println("Ontology saved to: " + filePath);
    }
    
    /**
     * Execute SPARQL SELECT query
     */
    public ResultSet executeSPARQLQuery(String queryString) {
        if (jenaModel == null) {
            throw new IllegalStateException("Jena model not loaded. Call loadOntology first.");
        }
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, jenaModel);
        return qexec.execSelect();
    }
    
    /**
     * Execute SPARQL ASK query
     */
    public boolean executeSPARQLAsk(String queryString) {
        if (jenaModel == null) {
            throw new IllegalStateException("Jena model not loaded. Call loadOntology first.");
        }
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, jenaModel);
        return qexec.execAsk();
    }
    
    /**
     * Add a class to the ontology
     */
    public OWLClass addClass(String className) {
        IRI classIRI = IRI.create(ontologyIRI + "#" + className);
        OWLClass owlClass = dataFactory.getOWLClass(classIRI);
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(owlClass);
        manager.addAxiom(ontology, declarationAxiom);
        return owlClass;
    }
    
    /**
     * Add an object property to the ontology
     */
    public OWLObjectProperty addObjectProperty(String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "#" + propertyName);
        OWLObjectProperty property = dataFactory.getOWLObjectProperty(propertyIRI);
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(property);
        manager.addAxiom(ontology, declarationAxiom);
        return property;
    }
    
    /**
     * Add a data property to the ontology
     */
    public OWLDataProperty addDataProperty(String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "#" + propertyName);
        OWLDataProperty property = dataFactory.getOWLDataProperty(propertyIRI);
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(property);
        manager.addAxiom(ontology, declarationAxiom);
        return property;
    }
    
    /**
     * Add an individual (instance) to the ontology
     */
    public OWLNamedIndividual addIndividual(String individualName, OWLClass owlClass) {
        IRI individualIRI = IRI.create(ontologyIRI + "#" + individualName);
        OWLNamedIndividual individual = dataFactory.getOWLNamedIndividual(individualIRI);
        
        // Declare the individual
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(individual);
        manager.addAxiom(ontology, declarationAxiom);
        
        // Assert class membership
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(owlClass, individual);
        manager.addAxiom(ontology, classAssertion);
        
        return individual;
    }
    
    // Getters
    public OWLOntology getOntology() {
        return ontology;
    }
    
    public OWLOntologyManager getManager() {
        return manager;
    }
    
    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }
    
    public OntModel getJenaModel() {
        return jenaModel;
    }
    
    public String getOntologyIRI() {
        return ontologyIRI;
    }
}