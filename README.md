# A model and query language for temporal graph databases
Run the project on Neo4j Windows.  
1- If it is not already installed, get [OpenJDK 17](https://openjdk.org/) or [Oracle Java 17](https://www.oracle.com/java/technologies/downloads)  
-  Upon completing the installation process, it is recommended to create the Java_HOME environment variable and include the Java root directory path as its value. This step ensures the correct configuration of your system after the installation of Neo4j on Windows.   
 
2- Download the latest release from [Neo4j Download Center](https://neo4j.com/deployment-center/). Select the appropriate ZIP distribution.  

-  Extract the files in the appropriate directory. For example: ```C:\Program Files\Neo4j``` and create Neo4j_HOME enviroment variable and include the Neo4j root directory path as its values.

3- To run Neo4j as a console application, use in the terminal : ```neo4j console```. or, To install Neo4j as a service use in the terminal: ```neo4j windows-service install```.   
4- Open http://localhost:7474 in your web browser and connect using the username neo4j with the default password neo4j. You will then be prompted to change the password.   
5- After connecting to the desired database, you can implement nodes and edges by adding the commands in this [link]( https://github.com/rydercodes/TemporalGraph/blob/main/src/Node%20and%20Edges.sh) in the Neo4j browser.
