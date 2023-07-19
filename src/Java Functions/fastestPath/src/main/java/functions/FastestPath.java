package functions;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.IsoDuration;
import org.neo4j.driver.types.Relationship;
import org.neo4j.procedure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FastestPath {
    private static final String uri = "bolt://localhost:7687"; // Neo4j database URI
    private static final String username = "neo4j"; // Neo4j username
    private static final String password = "13711992"; // Neo4j password

    public static class PathResult {
        public String path;
        public String totalDuration;

        public PathResult(String path, String totalDuration) {
            this.path = path;
            this.totalDuration = totalDuration;
        }
    }

    @Procedure(name = "fastestPath", mode = Mode.READ)
    @Description("Return fastest path between two cities.")
    public Stream<PathResult> fastestPath(@Name("startCity") String startCity, @Name("flight") String relationshipType, @Name("endCity") String endCity) {
        List<PathResult> paths = new ArrayList<>();

        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
             Session session = driver.session()) {

            String query =
                    "MATCH (c:City {name: '" + startCity + "'})<-[:LocatedAt*]-(a:Airport)\n" +
                            "MATCH path = (a:Airport)-[e:" + relationshipType + "]->(b:Airport)\n" +
                            "MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: '" + endCity + "'})\n" +
                            "WITH c, a, b, c1, relationships(path) AS edges\n" +
                            "WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)\n" +
                            "WITH edges, reduce(duration = duration('P0D'), edge in edges | duration + duration.between(edge.departure_time, edge.arrival_time)) AS TotalDuration\n" +
                            "ORDER BY TotalDuration ASC\n" +
                            "RETURN edges, TotalDuration\n";

            Result result = session.run(query);

            while (result.hasNext()) {
                Record record = result.next();
                List<Object> flights = record.get("edges").asList();
                IsoDuration totalDuration = record.get("TotalDuration").asIsoDuration();

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

                paths.add(new PathResult(pathBuilder.toString(), formatDuration(totalDuration)));
            }

            return paths.stream();
        } catch (Exception e) {
            e.printStackTrace();
            return Stream.empty(); // Return an empty stream in case of an error
        }
    }

    public static void main(String[] args) {
        String startCity = "Anchorage";
        String relationshipType = "Flight*";
        String endCity = "Los Angeles";

        FastestPath app = new FastestPath();
        Stream<PathResult> paths = app.fastestPath(startCity, relationshipType, endCity);

        paths.forEach(path -> {
            System.out.println(path.path);
            System.out.println("Total Duration: " + path.totalDuration);
        });
    }

    private static String formatDuration(IsoDuration duration) {
        long totalSeconds = duration.seconds();
        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = ((totalSeconds % (24 * 3600)) % 3600) / 60;
        long seconds = ((totalSeconds % (24 * 3600)) % 3600) % 60;

        return days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds";
    }
}
