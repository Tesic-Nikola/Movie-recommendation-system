package mov.ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class OntologyManager {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OntModel jenaModel;
    private String ontologyIRI;

    public OntologyManager() {
        this.manager = OWLManager.createOWLOntologyManager();
        this.dataFactory = manager.getOWLDataFactory();
    }

    /**
     * Load single ontology file (loads both schema + instances into Jena)
     * instancesPath is inferred by replacing "instances" filename
     */
    public void loadOntology(String instancesPath) throws Exception {
        // Derive schema path from instances path
        String schemaPath = instancesPath.replace("movies-instances.owl", "movies-schema.owl");
        loadOntology(schemaPath, instancesPath);
    }

    /**
     * Load schema + instances into Jena for SPARQL
     */
    public void loadOntology(String schemaPath, String instancesPath) throws Exception {
        File schemaFile = new File(schemaPath);
        this.ontology = manager.loadOntologyFromOntologyDocument(schemaFile);
        this.ontologyIRI = ontology.getOntologyID().getOntologyIRI().get().toString();

        jenaModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        jenaModel.read(new FileInputStream(schemaPath), null);
        jenaModel.read(new FileInputStream(instancesPath), null);

        System.out.println("Ontology loaded: " + ontologyIRI);
        System.out.println("Axioms count: " + ontology.getAxiomCount());
        System.out.println("Jena model loaded with schema + instances.");
    }

    public void saveOntology(String filePath) throws Exception {
        File file = new File(filePath);
        manager.saveOntology(ontology, new FileOutputStream(file));
        System.out.println("Ontology saved to: " + filePath);
    }

    public ResultSet executeSPARQLQuery(String queryString) {
        if (jenaModel == null) {
            throw new IllegalStateException("Jena model not loaded. Call loadOntology first.");
        }
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, jenaModel);
        return qexec.execSelect();
    }

    public boolean executeSPARQLAsk(String queryString) {
        if (jenaModel == null) {
            throw new IllegalStateException("Jena model not loaded. Call loadOntology first.");
        }
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, jenaModel);
        return qexec.execAsk();
    }

    public OWLClass addClass(String className) {
        IRI classIRI = IRI.create(ontologyIRI + "#" + className);
        OWLClass owlClass = dataFactory.getOWLClass(classIRI);
        manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(owlClass));
        return owlClass;
    }

    public OWLObjectProperty addObjectProperty(String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "#" + propertyName);
        OWLObjectProperty property = dataFactory.getOWLObjectProperty(propertyIRI);
        manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(property));
        return property;
    }

    public OWLDataProperty addDataProperty(String propertyName) {
        IRI propertyIRI = IRI.create(ontologyIRI + "#" + propertyName);
        OWLDataProperty property = dataFactory.getOWLDataProperty(propertyIRI);
        manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(property));
        return property;
    }

    public OWLNamedIndividual addIndividual(String individualName, OWLClass owlClass) {
        IRI individualIRI = IRI.create(ontologyIRI + "#" + individualName);
        OWLNamedIndividual individual = dataFactory.getOWLNamedIndividual(individualIRI);
        manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(individual));
        manager.addAxiom(ontology, dataFactory.getOWLClassAssertionAxiom(owlClass, individual));
        return individual;
    }

    public OWLOntology getOntology() { return ontology; }
    public OWLOntologyManager getManager() { return manager; }
    public OWLDataFactory getDataFactory() { return dataFactory; }
    public OntModel getJenaModel() { return jenaModel; }
    public String getOntologyIRI() { return ontologyIRI; }
}