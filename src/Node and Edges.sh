CREATE (:City {name: 'Anchorage', state: 'AK',  country:'USA'});
CREATE (:City {name: 'Seattle', state: 'WA',	country:'USA'});
CREATE (:City {name: 'Los Angeles', state: 'CA',	country:'USA'});
CREATE (:City {name: 'West Palm Beach', state: 'FL',	country:'USA'});
CREATE (:City {name: 'Minneapolis', state: 'MN',	country:'USA'});
CREATE (:City {name: 'Phoenix', state: 'AZ',	country:'USA'});
CREATE (:City {name: 'Chicago', state: 'IL', country:'USA'});
CREATE (:City {name: 'Charlotte', state: 'NC', country:'USA'});
CREATE (:City {name: 'San Juan', state: 'PR', country:'USA'}); 
CREATE (:City {name: 'New York', state: 'NY', country:'USA'});



CREATE (:Airport {iata_code: 'ANC', name: 'Ted Stevens Anchorage International Airport'});
CREATE (:Airport {iata_code: 'SEA', name: 'Seattle-Tacoma International Airport'});
CREATE (:Airport {iata_code: 'LAX', name: 'Los Angeles International Airport'});
CREATE (:Airport {iata_code: 'PBI', name: 'Palm Beach International Airport'});
CREATE (:Airport {iata_code: 'MSP', name: 'Minneapolis-Saint Paul International Airport'});
CREATE (:Airport {iata_code: 'PHX', name: 'Phoenix Sky Harbor International Airport'});
CREATE (:Airport {iata_code: 'ORD', name: "Chicago O'Hare International Airport"});
CREATE (:Airport {iata_code: 'CLT', name: "Charlotte Douglas International Airport"});
CREATE (:Airport {iata_code: 'SJU', name: "Luis Muñoz Marín International Airport"});
CREATE (:Airport {iata_code: 'JFK', name: "John F. Kennedy International Airport"});



MATCH (a:City) , (b:Airport)
WHERE a.name= 'Anchorage' AND b.iata_code='ANC'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Seattle' AND b.iata_code='SEA'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Los Angeles' AND b.iata_code='LAX'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'West Palm Beach' AND b.iata_code='PBI'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Minneapolis' AND b.iata_code='MSP'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Phoenix' AND b.iata_code='PHX'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Chicago' AND b.iata_code='ORD'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'Charlotte' AND b.iata_code='CLT'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'San Juan' AND b.iata_code='SJU'
CREATE (a)<-[:LocatedAt]-(b);

MATCH (a:City) , (b:Airport)
WHERE a.name= 'New York' AND b.iata_code='JFK'
CREATE (a)<-[:LocatedAt]-(b);



MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='ANC' AND b.iata_code='SEA'
CREATE (a)-[e:Flight{departure_time:425, arrival_time:450, distance:1448}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='SEA' AND b.iata_code='LAX'
CREATE (a)-[e:Flight{departure_time:445, arrival_time:470, distance:2330}]->(b);


MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='ANC' AND b.iata_code='SEA'
CREATE (a)-[e:Flight{departure_time:430, arrival_time:460, distance:1448}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='SEA' AND b.iata_code='LAX'
CREATE (a)-[e:Flight{departure_time:455, arrival_time:480, distance:2330}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='ANC' AND b.iata_code='SEA'
CREATE (a)-[e:Flight{departure_time:400, arrival_time:430, distance:1200}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='SEA' AND b.iata_code='LAX'
CREATE (a)-[e:Flight{departure_time:420, arrival_time:440, distance:1800}]->(b);



MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='LAX' AND b.iata_code='MSP'
CREATE (a)-[e:Flight{departure_time:465, arrival_time:500, distance:1535}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='SEA' AND b.iata_code='PHX'
CREATE (a)-[e:Flight{departure_time:510, arrival_time:600, distance:1107}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='PHX' AND b.iata_code='ORD'
CREATE (a)-[e:Flight{departure_time:610, arrival_time:700, distance:1440}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='ORD' AND b.iata_code='CLT'
CREATE (a)-[e:Flight{departure_time:710, arrival_time:1139, distance:1744}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='PHX' AND b.iata_code='CLT'
CREATE (a)-[e:Flight{departure_time:209, arrival_time:728, distance:1773}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='SJU' AND b.iata_code='JFK'
CREATE (a)-[e:Flight{departure_time:627, arrival_time:1001, distance:1598}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='JFK' AND b.iata_code='SJU'
CREATE (a)-[e:Flight{departure_time:618, arrival_time:1039, distance:1598}]->(b);

MATCH (a:Airport),(b:Airport)
WHERE a.iata_code='CLT' AND b.iata_code='JFK'
CREATE (a)-[e:Flight{departure_time:620, arrival_time:1413, distance:2475}]->(b);
