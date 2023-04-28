import java.util.*;
import java.io.*;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CS245A2
{
    public static void main(String[] args) throws IOException, ParseException
    {
        //Must be run with a try-catch statement in order to handle ParseException
        try
        {
            HashSet<String> setOfActors = new HashSet<String>(); //Uses a HashSet to store all actors
            HashMap<String, ArrayList<String>> mapOfMovies = new HashMap<String, ArrayList<String>>(); //Uses a HashMap to find connections with movies as the keys
            
            JSONParser dataParse = new JSONParser(); //Allows the data stored as JSON to be parsed

            BufferedReader fileInput = new BufferedReader(new FileReader(args[0])); //Allows the file specified from the command line to be read
            fileInput.readLine();
            String line = fileInput.readLine();
            
            while (line != null)
            {
                //Associates index values of delimiter values in order to parse more efficiently
                int delim1 = line.indexOf(",");
                int delim2 = line.indexOf(",", delim1 + 1);
                
                String movieName = line.substring(delim1 + 1, delim2); //Reads the movie name between delimiters

                line = line.substring(delim2 + 1); //Allows for parsing of actor names
        
                int listTrack = 1; //Index used to keep track of nested lists
                delim1 = line.indexOf("[");

                for (int i = delim1 + 1; i < line.length(); i++)
                {
                    //Increments the number of nested lists when another one is recognized
                    if (line.charAt(i) == '[')
                    {
                        listTrack++;
                    }

                    else if (line.charAt(i) == ']' && listTrack == 1) //Recognizes the end of a singular list when the final bracket is identified
                    {
                        delim2 = i;

                        break;
                    }

                    else if(line.charAt(i) == ']' && listTrack > 1) //Recognizes the end of a nested list when the final bracket is identified
                    {
                        listTrack--;
                    } 

                    else
                    {
                        continue;
                    }
                }

                String readLine = line.substring(delim1, delim2 + 1).replaceAll("\"\"", "\""); //Formats the data to be readable by an array
                JSONArray jsonArr = (JSONArray) dataParse.parse(readLine); //Casts the data as a JSONArray

                ArrayList<String> arrayOfActors = new ArrayList<String>(); //Creates an ArrayList to read the names of actors

                for (int i = 0; i < jsonArr.size(); i++) //Adds all actors to both the ArrayList and HashSet
                {
                    JSONObject jsonObj = (JSONObject) jsonArr.get(i);

                    String actorName = ((String) jsonObj.get("name")).toLowerCase(); //Stores all actors names as lower case to prevent false negatives when retrieving actors by name
                    
                    arrayOfActors.add(actorName);
                    setOfActors.add(actorName);
                }

                mapOfMovies.put(movieName, arrayOfActors); //Adds the actor to the HashMap using the name of their movie as the key

                line = fileInput.readLine(); //Iterates to next line
            }

            Graph graphOfActors = new Graph(); //Creates the graph

            for (String actorInSet : setOfActors) //Adds vertices to the graph
            {
                graphOfActors.addActor(actorInSet);
            }
            
            for (String movieInMap : mapOfMovies.keySet()) //Creates edges in the graph from the movies
            {
                ArrayList<String> movieActors = mapOfMovies.get(movieInMap);

                for (int i = 0; i < movieActors.size(); i++)
                {
                    for (int j = i + 1; j < movieActors.size(); j++)
                    {
                        graphOfActors.addEdge(movieActors.get(i), movieActors.get(j));
                    }
                }
            }
            
            //Formats the output of the graph
            Scanner userInput = new Scanner(System.in);

            System.out.print("Actor 1 Name: "); //Prompts the user for the name of an actor
            
            String actor1Capitalized = userInput.nextLine(); //Preserves the capitalized version of the name for clean outputting
            String actor1 = actor1Capitalized.toLowerCase(); //Converts the name to lower case in order to match storage method and prevent false negatives

            System.out.print("Actor 2 Name: ");

            String actor2Capitalized = userInput.nextLine();
            String actor2 = actor2Capitalized.toLowerCase();
            
            if (setOfActors.contains(actor1) == false || setOfActors.contains(actor2) == false) //Indicates if no actor exists
            {
                System.out.println("No such actor.");

                System.exit(1);
            }

            ArrayList<String> path = graphOfActors.ShortestPath(actor1, actor2); //Calculates the shortest path between both specified actors
            
            if (path == null) //Indicates if no path exists
            {
                System.out.println("No such path.");
            }
            
            else //Outputs the path one exists
            {
                System.out.println("Path between " + actor1Capitalized + " and " + actor2Capitalized + ": ");

                for (int i = 0; i < path.size(); i++)
                {
                    if (i != path.size() - 1) //Represents a connection using an arrow if it is not the last element
                    {
                        System.out.print(path.get(i));
                        System.out.print( " ---> ");
                    }
                    
                    else
                    {
                        System.out.print(path.get(i));
                    }
                }
            }

            fileInput.close(); //Prevents a resource leak
            userInput.close();
        }

        catch (Exception e)
        {
            System.exit(1);
        }
    }
}