package functions;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Relationship;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class EarliestArrival {
    private static final String uri = "bolt://localhost:7687"; // Neo4j database URI
    private static final String username = "neo4j"; // Neo4j username
    private static final String password = "13711992"; // Neo4j password

    public static class ArrivalResult {
        public String path;
        public ZonedDateTime earliestArrivalTime;

        public ArrivalResult(String path, ZonedDateTime earliestArrivalTime) {
            this.path = path;
            this.earliestArrivalTime = earliestArrivalTime;
        }
    }

    @Procedure(name = "earliestArrivalPath", mode = Mode.READ)
    @Description("Returns earliest arrival path between two cities.")
    public Stream<ArrivalResult> earliestArrival(@Name("startCity") String startCity, @Name("flight") String relationshipType, @Name("endCity") String endCity) {
        List<ArrivalResult> paths = new ArrayList<>();

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {

            String query =
                    "MATCH (c:City {name: '" + startCity + "'})<-[:LocatedAt*]-(a:Airport)\n" +
                            "MATCH path = (a:Airport)-[e:" + relationshipType + "]->(b:Airport)\n" +
                            "MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: '" + endCity + "'})\n" +
                            "WITH c, a, b, c1, relationships(path) AS edges\n" +
                            "WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)\n" +
                            "RETURN edges, edges[-1].arrival_time AS earliestArrivalTime\n" +
                            "ORDER BY earliestArrivalTime ASC\n";

            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                List<Object> flights = record.get("edges").asList();
                ZonedDateTime earliestArrivalTime = record.get("earliestArrivalTime").asZonedDateTime();

                StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append("Flights: ");
                for (Object obj : flights) {
                    Relationship flight = (Relationship) obj;
                    Map<String, Object> properties = flight.asMap();
                    for (String key : properties.keySet()) {
                        Object value = properties.get(key);
                        pathBuilder.append(key).append(": ").append(value).append(", ");
                    }
                }
                pathBuilder.append("Earliest Arrival Time: ").append(earliestArrivalTime.toString());

                paths.add(new ArrivalResult(pathBuilder.toString(), earliestArrivalTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paths.stream();
    }

    public static void main(String[] args) {
        String startCity = "Anchorage";
        String relationshipType = "Flight*";
        String endCity = "Los Angeles";

        EarliestArrival app = new EarliestArrival();
        Stream<ArrivalResult> paths = app.earliestArrival(startCity, relationshipType, endCity);
        paths.forEach(path -> System.out.println(path.path));
    }
}
