package exercise2;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.datatypes.xsd.XSDDatatype;

import java.io.FileOutputStream;

public class AirlineE2 {

    public static void main(String[] args) throws Exception {

        Model model = ModelFactory.createDefaultModel();

        String ns = "http://example.org/airline#";

        model.setNsPrefix("air", ns);
        model.setNsPrefix("rdfs", RDFS.getURI());

        // =========================
        // CLASSES
        // =========================
        Resource Airline = model.createResource(ns + "Airline");
        Resource Airport = model.createResource(ns + "Airport");
        Resource FlightConnection = model.createResource(ns + "FlightConnection");
        Resource Flight = model.createResource(ns + "Flight");
        Resource Trip = model.createResource(ns + "Trip");
        Resource Customer = model.createResource(ns + "Customer");
        Resource Plane = model.createResource(ns + "Plane");

        model.add(Flight, RDFS.subClassOf, FlightConnection);

        // =========================
        // PROPERTIES
        // =========================
        Property hasAirport = model.createProperty(ns, "hasAirport");
        Property from = model.createProperty(ns, "fromAirport");
        Property to = model.createProperty(ns, "toAirport");

        Property hasFlight = model.createProperty(ns, "hasFlight");
        Property flightOrder = model.createProperty(ns, "flightOrder");

        Property bookedBy = model.createProperty(ns, "bookedBy");
        Property startTime = model.createProperty(ns, "startTime");
        Property endTime = model.createProperty(ns, "endTime");

        Property duration = model.createProperty(ns, "durationHours");
        Property price = model.createProperty(ns, "price");

        Property planeModel = model.createProperty(ns, "planeModel");
        Property maxPassengers = model.createProperty(ns, "maxPassengers");

        // =========================
        // AIRLINE
        // =========================
        Resource airline = model.createResource(ns + "GlobalAirline")
                .addProperty(RDF.type, Airline)
                .addProperty(model.createProperty(ns, "manager"), "Dr. Smith")
                .addProperty(model.createProperty(ns, "employees"), "1500");

        // =========================
        // AIRPORTS
        // =========================
        Resource BER = airport(model, ns, "BER", "Berlin", 2005);
        Resource JFK = airport(model, ns, "JFK", "New York", 1999);
        Resource FCO = airport(model, ns, "FCO", "Rome", 2003);
        Resource LHR = airport(model, ns, "LHR", "London", 1990);

        airline.addProperty(hasAirport, BER)
                .addProperty(hasAirport, JFK)
                .addProperty(hasAirport, FCO)
                .addProperty(hasAirport, LHR);

        // =========================
        // PLANE
        // =========================
        Resource plane = model.createResource(ns + "Boeing737")
                .addProperty(RDF.type, Plane)
                .addProperty(maxPassengers, "180");

        // =========================
        // FLIGHT CONNECTIONS
        // =========================
        connection(model, ns, "C1", BER, JFK, 8, plane);
        connection(model, ns, "C2", JFK, FCO, 9, plane);
        connection(model, ns, "C3", FCO, LHR, 3, plane);
        connection(model, ns, "C4", LHR, BER, 2, plane);

        // =========================
        // CUSTOMER
        // =========================
        Resource customer = model.createResource(ns + "Aafaq")
                .addProperty(RDF.type, Customer)
                .addProperty(model.createProperty(ns, "name"), "Aafaq Ahmad")
                .addProperty(model.createProperty(ns, "passport"), "DEMO-001");

        // =========================
        // TRIP 1
        // =========================
        Resource trip1 = trip(model, ns, "Trip1", customer);

        addFlight(model, trip1, ns, "F1", 1, BER, JFK);
        addFlight(model, trip1, ns, "F2", 2, JFK, FCO);
        addFlight(model, trip1, ns, "F3", 3, FCO, LHR);
        addFlight(model, trip1, ns, "F4", 4, LHR, BER);

        // =========================
        // TRIP 2
        // =========================
        Resource customer2 = model.createResource(ns + "John")
                .addProperty(RDF.type, Customer);

        Resource trip2 = trip(model, ns, "Trip2", customer2);

        addFlight(model, trip2, ns, "F5", 1, JFK, BER);
        addFlight(model, trip2, ns, "F6", 2, BER, FCO);
        addFlight(model, trip2, ns, "F7", 3, FCO, LHR);
        addFlight(model, trip2, ns, "F8", 4, LHR, JFK);

        // =========================
        // TRIP 3
        // =========================
        Resource customer3 = model.createResource(ns + "Emma")
                .addProperty(RDF.type, Customer);

        Resource trip3 = trip(model, ns, "Trip3", customer3);

        addFlight(model, trip3, ns, "F9", 1, BER, FCO);
        addFlight(model, trip3, ns, "F10", 2, FCO, JFK);
        addFlight(model, trip3, ns, "F11", 3, JFK, LHR);
        addFlight(model, trip3, ns, "F12", 4, LHR, BER);

        // =========================
        // OUTPUT
        // =========================
        FileOutputStream out = new FileOutputStream("E2.rdf");
        model.write(out, "RDF/XML-ABBREV");
        out.close();

        System.out.println("E2 RDF file generated successfully!");
    }

    // =========================
    // HELPERS
    // =========================

    static Resource airport(Model model, String ns, String code, String city, int year) {
        return model.createResource(ns + code)
                .addProperty(RDF.type, model.createResource(ns + "Airport"))
                .addProperty(model.createProperty(ns, "city"), city)
                .addProperty(model.createProperty(ns, "startYear"), String.valueOf(year));
    }

    static void connection(Model model, String ns, String id,
                           Resource from, Resource to, int duration, Resource plane) {

        model.createResource(ns + id)
                .addProperty(RDF.type, model.createResource(ns + "FlightConnection"))
                .addProperty(model.createProperty(ns, "fromAirport"), from)
                .addProperty(model.createProperty(ns, "toAirport"), to)
                .addProperty(model.createProperty(ns, "durationHours"), String.valueOf(duration))
                .addProperty(model.createProperty(ns, "planeModel"), plane);
    }

    static Resource trip(Model model, String ns, String id, Resource customer) {

        return model.createResource(ns + id)
                .addProperty(RDF.type, model.createResource(ns + "Trip"))
                .addProperty(model.createProperty(ns, "bookedBy"), customer)
                .addProperty(model.createProperty(ns, "startTime"),
                        model.createTypedLiteral("2025-05-01T00:00:00",
                                XSDDatatype.XSDdateTime))
                .addProperty(model.createProperty(ns, "price"), "1200");
    }

    static void addFlight(Model model, Resource trip,
                          String ns, String id,
                          int order,
                          Resource from, Resource to) {

        Resource flight = model.createResource(ns + id)
                .addProperty(RDF.type, model.createResource(ns + "Flight"))
                .addProperty(model.createProperty(ns, "fromAirport"), from)
                .addProperty(model.createProperty(ns, "toAirport"), to)
                .addProperty(model.createProperty(ns, "flightOrder"), String.valueOf(order))
                .addProperty(model.createProperty(ns, "startTime"),
                        model.createTypedLiteral("2025-05-01T00:00:00",
                                XSDDatatype.XSDdateTime))
                .addProperty(model.createProperty(ns, "endTime"),
                        model.createTypedLiteral("2025-05-01T03:00:00",
                                XSDDatatype.XSDdateTime));

        trip.addProperty(model.createProperty(ns, "hasFlight"), flight);
    }
}