package org.neo4j.driver;

import org.neo4j.driver.types.Relationship;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CPath {
    private static final String uri = "bolt://localhost:7687"; // Neo4j database URI
    private static final String username = "neo4j"; // Neo4j username
    private static final String password = "13711992"; // Neo4j password

    public static class PathResult {
        public String path;

        public PathResult(String path) {
            this.path = path;
        }
    }

    @Procedure(name = "cPath", mode = Mode.READ)
    @Description("Returns all the continuous Paths between two cities.")
    public Stream<PathResult> cPath(@Name("startCity") String startCity, @Name("flight") String relationshipType, @Name("endCity") String endCity) {
        List<PathResult> paths = new ArrayList<>();

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {

            String query =
                    "MATCH (c:City {name:'" + startCity + "'})<-[:LocatedAt*]-(a:Airport)\n" +
                            "MATCH path = (a:Airport)-[e: " + relationshipType + "]->(b:Airport)\n" +
                            "MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: '" + endCity + "'})\n" +
                            "WITH c, a, b, c1, relationships(path) AS edges\n" +
                            "RETURN edges AS flight\n";

            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                List<Object> flights = record.get("flight").asList();

                // Build the path string
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

                paths.add(new PathResult(pathBuilder.toString()));
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

        CPath cPath = new CPath();
        Stream<PathResult> paths = cPath.cPath(startCity, relationshipType, endCity);
        paths.forEach(path -> System.out.println(path.path));
    }
}
