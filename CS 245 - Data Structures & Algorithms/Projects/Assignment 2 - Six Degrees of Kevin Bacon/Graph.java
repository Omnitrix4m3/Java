import java.util.*;

public class Graph
{
    class Node //Node class which holds an actor's (Node's) names and connections (edges)
	{
        String name;

		List<Node> connections;

		public Node(String name)
		{
			this.name = name;
			connections = new ArrayList<Node>();
		}

		public String getName()
		{
			return this.name;
		}

		public List<Node> getConnections()
		{
			return connections;
		}

		public void addEdge(Node connectingNode) //Creates a connection by adding an edge between an origin node and a connection node
		{
            boolean known = false;
            
			for (Node person : connections) //Quits if there already is a connection to that Node
			{
				if (person.getName().equals(connectingNode.getName()))
				{
                    known = true;
                    
					break;
				}
            }
            
			if (known == false)
			{
				connections.add(connectingNode);
			}
		}
	}
    
    //Private Instance Data
	private HashMap<String, Node> actors;

    //Constructor defines HashMap value
	public Graph()
	{
		actors = new HashMap<String, Node>();
	}

	public void addActor(String actorObj) //Method allows for Actor objects to be added to the HashMap
	{
		actors.put(actorObj, new Node(actorObj));
	}

	public void addEdge(String firstActorName, String secondActorName) //Method creates a connection between two actors
	{
		Node firstActor = actors.get(firstActorName);
        Node secondActor = actors.get(secondActorName);
        
		firstActor.addEdge(secondActor);
		secondActor.addEdge(firstActor);
    }
    
    private ArrayList<String> path(Map<String, String> parents, String givenActor) //Method returns a path as a map
	{
        ArrayList<String> pathArray = new ArrayList<String>();
        
		while (givenActor != null)
		{
			pathArray.add(0, givenActor);
			givenActor = parents.get(givenActor);
        }
        
		return pathArray;
	}

	public ArrayList<String> ShortestPath(String actor1, String actor2) //BFS Algorithm that allows for the shortest path to be found
	{
        ArrayList<Node> listOfActors = new ArrayList<Node>();
        listOfActors.add(actors.get(actor1));

		HashMap<String, String> parents = new HashMap<String, String>();
		parents.put(actor1, null);

		while (listOfActors.isEmpty() == false) //Adds actor to a list of actors
		{
            Node currentActor = listOfActors.get(0);
            List<Node> currentConnections = currentActor.getConnections(); //Stores edges for current node
            
			for (int i = 0; i < currentConnections.size(); i++) //Goes through all edges
			{
				Node nextActor = currentConnections.get(i);
				String actorName = nextActor.getName();

                boolean visited = parents.containsKey(actorName);
                
				if (visited == true)
				{
					continue;
                }
                
				else
				{
					listOfActors.add(nextActor);
                    parents.put(actorName, currentActor.getName()); //Adds node to parent HashMap
                    
					if (actorName.equals(actor2)) //Checks for potentially connected node at current level
					{
						return path(parents, actor2);
					}
				}
            }
            
			listOfActors.remove(0);
		}

		return null; //Returns null if there is no edge to be found
	}
}
