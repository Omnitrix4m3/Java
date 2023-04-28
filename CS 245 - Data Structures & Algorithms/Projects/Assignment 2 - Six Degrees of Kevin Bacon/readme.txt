How to:
In order to run the code you must ensure both the csv and .jar file are in the same directory
Then, on the command line enter the following
	"javac -cp "json-simple-1.1.1.jar" *.java CS245A2.java" to compile
	"java -cp "json-simple-1.1.1.jar" CS245A2 tmdb_5000_credits.csv" to run

Runtime Analysis:
The estimated running time of this assignment is BigTheta(|V| + |E|) with V for vertices and E for edges.