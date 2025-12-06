package mov.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String uri; // unique identifier from ontology
    private String name;
    private int birthYear;
    private String nationality;
    private List<String> roles; // director, actor, writer, producer
    private List<String> awards;
    
    public Person() {
        this.roles = new ArrayList<>();
        this.awards = new ArrayList<>();
    }
    
    public Person(String name) {
        this();
        this.name = name;
    }
    
    // Getters and Setters
    public String getUri() {
        return uri;
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getBirthYear() {
        return birthYear;
    }
    
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public void addRole(String role) {
        this.roles.add(role);
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
        return "Person [name=" + name + ", birthYear=" + birthYear + 
               ", nationality=" + nationality + ", roles=" + roles + "]";
    }
}