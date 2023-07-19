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

public class LatestDeparture {
    private static final String uri = "bolt://localhost:7687"; // Neo4j database URI
    private static final String username = "neo4j"; // Neo4j username
    private static final String password = "13711992"; // Neo4j password

    public static class DepartureResult {
        public String path;
        public ZonedDateTime latestDepartureTime;

        public DepartureResult(String path, ZonedDateTime latestDepartureTime) {
            this.path = path;
            this.latestDepartureTime = latestDepartureTime;
        }
    }

    @Procedure(name = "latestDeparture", mode = Mode.READ)
    @Description("Returns latest departure path between two cities.")
    public Stream<DepartureResult> latestDeparture(@Name("startCity") String startCity, @Name("flight") String relationshipType, @Name("endCity") String endCity) {
        List<DepartureResult> paths = new ArrayList<>();

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {

            String query =
                    "MATCH (c:City {name: '" + startCity + "'})<-[:LocatedAt*]-(a:Airport)\n" +
                            "MATCH path = (a:Airport)-[e:" + relationshipType + "]->(b:Airport)\n" +
                            "MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: '" + endCity + "'})\n" +
                            "WITH c, a, b, c1, relationships(path) AS edges\n" +
                            "WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)\n" +
                            "WITH edges, edges[0].departure_time AS latestDepartureTime\n" +
                            "ORDER BY latestDepartureTime DESC\n" +
                            "RETURN edges, latestDepartureTime\n" ;

            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                List<Object> flights = record.get("edges").asList();
                ZonedDateTime latestDepartureTime = record.get("latestDepartureTime").asZonedDateTime();

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
                pathBuilder.append("Latest Departure Time: ").append(latestDepartureTime.toString());

                paths.add(new DepartureResult(pathBuilder.toString(), latestDepartureTime));
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

        LatestDeparture app = new LatestDeparture();
        Stream<DepartureResult> paths = app.latestDeparture(startCity, relationshipType, endCity);
        paths.forEach(path -> System.out.println(path.path));
    }
}
