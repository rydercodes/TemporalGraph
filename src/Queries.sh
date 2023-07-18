Continuous Paths:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
RETURN edges





Pairwise Continuous Paths:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
RETURN edges




Earliest Arrival Path:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
RETURN edges, edges[-1].arrival_time AS earliestArrivalTime
ORDER BY earliestArrivalTime ASC
LIMIT 1



Latest Departure Path:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(1, size(edges) - 1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, edges[0].departure_time AS firstDepartureTime
ORDER BY firstDepartureTime DESC
RETURN edges
LIMIT 1


Fastest Path:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, reduce(duration = 0, edge in edges | duration + (edge.arrival_time - edge.departure_time)) AS TotalDuration
ORDER BY TotalDuration ASC
RETURN edges, TotalDuration
LIMIT 1


Shortest Path:

MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, reduce(distance = 0, edge in edges | distance + (edge.distance)) AS totalDistance
ORDER BY totalDistance ASC
RETURN edges, totalDistance
LIMIT 1

