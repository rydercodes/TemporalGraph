# A model and query language for temporal graph databases
#### The project concept is derived from a research [paper](https://link.springer.com/article/10.1007/s00778-021-00675-4) titled "A model and query language for temporal graph databases," authored by Ariel Debrouvier, Eliseo Parodi, Matías Perazzo, Valeria Soliani, and Alejandro Vaisman. The paper served as the inspiration and reference for the project's ideas and the development of the query language used.   
Run the project on Neo4j Windows.  
1- If it is not already installed, get [OpenJDK 17](https://openjdk.org/) or [Oracle Java 17](https://www.oracle.com/java/technologies/downloads)  
-  Upon completing the installation process, it is recommended to create the Java_HOME environment variable and include the Java root directory path as its value. This step ensures the correct configuration of your system after the installation of Neo4j on Windows.   
 
2- Download the latest release from [Neo4j Download Center](https://neo4j.com/deployment-center/). Select the appropriate ZIP distribution.  

-  Extract the files in the appropriate directory. For example: ```C:\Program Files\Neo4j``` and create Neo4j_HOME enviroment variable and include the Neo4j root directory path as its values.

3- To run Neo4j as a console application, use in the terminal : ```neo4j console```. or, To install Neo4j as a service use in the terminal: ```neo4j windows-service install```.   
4- Open http://localhost:7474 in your web browser and connect using the username neo4j with the default password neo4j. You will then be prompted to change the password.   
5- After connecting to the desired database, you can implement nodes and edges by adding the commands in this [link]( https://github.com/rydercodes/TemporalGraph/blob/main/src/Node%20and%20Edges.sh) in the Neo4j browser.   

- Once you implement the codes. by running ```MATCH(n) RETURN n``` in Neo4j browser, you will see the graph with all nodes and edges.

In this project, we are looking for to find ```continuous paths```, ```pairwise continuous paths```, ```earliest arrival path```, ```latest departure path```, ```fastest path```, and ```shortest path``` between the cities by using the edge flight which connect the airports together.   

By running the following queries, we can run the each path we need:   
1- ```Continuous Paths```: Find all the paths which connect two cities together without consider any attributes.   
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
RETURN edges
```
2- ```Pairwise continuous paths```: In our scenario, a pairwise continuous path refers to a sequence of flights where the arrival time of each flight is later than the departure time of the next flight.   
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
RETURN edges
```
3- ```Earliest Arrival Path```: Find a path with the earliest arrival time between all the paths which connected two cities together.
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
RETURN edges, edges[-1].arrival_time AS earliestArrivalTime
ORDER BY earliestArrivalTime ASC
LIMIT 1
```
4- ```Latest Departure Path```: Find a path with the latest departure time between all the paths which connected two cities together.
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(1, size(edges) - 1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, edges[0].departure_time AS firstDepartureTime
ORDER BY firstDepartureTime DESC
RETURN edges
LIMIT 1
```   
5- ```Fastest Path```: Find fastest path between two cities when they are connected together via different ways.
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, reduce(duration = 0, edge in edges | duration + (edge.arrival_time - edge.departure_time)) AS TotalDuration
ORDER BY TotalDuration ASC
RETURN edges, TotalDuration
LIMIT 1
```
6- ```Shortest Path```: find shortest path between two cities when they are connected together via different ways.
```ruby
MATCH (c:City {name: 'Anchorage'})<-[:LocatedAt*]-(a:Airport)
MATCH path = (a:Airport)-[e:Flight*]->(b:Airport)
MATCH (b:Airport)-[:LocatedAt*]->(c1:City {name: 'Los Angeles'})
WITH c, a, b, c1, relationships(path) AS edges
WHERE all(i in range(0, size(edges)-1) WHERE edges[i-1].arrival_time > edges[i].departure_time)
WITH edges, reduce(distance = 0, edge in edges | distance + (edge.distance)) AS totalDistance
ORDER BY totalDistance ASC
RETURN edges, totalDistance
LIMIT 1
```
