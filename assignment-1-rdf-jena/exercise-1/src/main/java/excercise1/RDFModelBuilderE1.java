package excercise1;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

public class RDFModelBuilderE1 {
    public static void main(String[] args) {

        // Create model
        Model model = ModelFactory.createDefaultModel();

        // Namespaces
        String ns = "http://example.org/";

        // Resources
        Resource person = model.createResource(ns + "RalphBergmann");
        Resource website = model.createResource("https://www.uni-trier.de/universitaet/fachbereiche-faecher/fachbereich-iv/faecher/informatikwissenschaften/professuren/wirtschaftsinformatik-2/professur");
        Resource lecture = model.createResource(ns + "SemanticTechnologies");
        Resource seminar = model.createResource(ns + "ProjectSeminar");
        Resource alex = model.createResource(ns + "AlexanderSchultheis");

        // Properties
        Property creator = model.createProperty(ns, "creator");
        Property name = model.createProperty(ns, "name");
        Property email = model.createProperty(ns, "email");
        Property givesLecture = model.createProperty(ns, "givesLecture");
        Property hasSeminar = model.createProperty(ns, "hasSeminar");
        Property hasAssignments = model.createProperty(ns, "hasAssignments");
        Property givesSeminar = model.createProperty(ns, "givesSeminar");

        // --- Statements ---

        // Person details
        model.add(person, name, "Ralph Bergmann");
        model.add(person, email, "bergmann@uni-trier.de");

        // Creator of website
        model.add(website, creator, person);

        // Lecture
        model.add(person, givesLecture, lecture);

        // Seminar
        model.add(lecture, hasSeminar, seminar);
        model.add(seminar, hasAssignments, "3");

        // Alexander Schultheis
        model.add(alex, name, "Alexander Schultheis");
        model.add(alex, email, "Alexander.Schultheis@uni-trier.de");
        model.add(alex, givesSeminar, seminar);

        // Output RDF/XML
        model.write(System.out, "RDF/XML");

        // Save to file
        try {
            model.write(new java.io.FileOutputStream("E1.rdf"), "RDF/XML");
        } catch (Exception e) {
            System.err.println("Error writing RDF file: " + e.getMessage());
        }
    }
}