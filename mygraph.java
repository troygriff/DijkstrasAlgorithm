/* 
 * This program creates a graph representation and uses Dijkstra's algorithm to find
 * the shortest path.
 */

import java.util.*;

public class MyGraph implements Graph {
    private final HashMap<Vertex, ArrayList<Edge>> myMap;
    private final Collection<Vertex> vert;
    private final Collection<Edge> ed;
	
    /**
     * Creates the MyGraph object with the Collections of vertex and edges that are passed through.
     * 
     * @param vertexCollection is the collection of vertices that was passed into the graph
     * @param edgeCollection is the collection of edges that was passed into the graph
     * @throws IllegalArgumentExceptions if:
     * 		1. The current edge is already in the map
     * 		2. The weight is negative
     * 		3. If currentEdge and otherEdge are the same but the weights are different.
     */
    public MyGraph(Collection<Vertex> vertexCollection, Collection<Edge> edgeCollection) {
    	vert = new ArrayList<Vertex>();
    	ed = new ArrayList<Edge>();
    	myMap = new HashMap<Vertex, ArrayList<Edge>>();
    	
    	for(Vertex tempV: vertexCollection) {
    		vert.add(tempV);
    	}
    	
    	for(Vertex tempV: vert) {
    		myMap.put(tempV, new ArrayList<Edge>());
    	}
        
    	for (Edge currentEdge : edgeCollection) {
    		if (myMap.containsValue(currentEdge) || !myMap.containsKey(currentEdge.getDestination())) {
    			throw new IllegalArgumentException("This current edge is already in the map!: " + currentEdge);
    		}
    		
    		if (currentEdge.getWeight() < 0) {
    			throw new IllegalArgumentException("The weight cannot be negative (less than 0)!");
    		}  
    		
    		for(Edge otherEdge : edgeCollection) {
    			if(currentEdge.getSource().equals(otherEdge.getSource()) && 
    					currentEdge.getDestination().equals(otherEdge.getDestination()) &&
    					currentEdge.getWeight() != otherEdge.getWeight()) {
    				throw new IllegalArgumentException(currentEdge + " and " + otherEdge + " are the same" +
    						"but their weights are not.");
    			}
    		
    		}
    		
    		ArrayList<Edge> edge = myMap.get(currentEdge.getSource()); 
    		edge.add(currentEdge);
    		myMap.put(currentEdge.getSource(), edge);
    		ed.add(currentEdge);
    	} 
    }
   
    /** 
     * Returns the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable) 
     */
    public Collection<Vertex> vertices() {
    	return vert;
    }

    /** 
     * Returns the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
    	return ed;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
    	if(!vert.contains(v)) {
    		throw new IllegalArgumentException();
    	}
    	
    	Collection<Vertex> adjacentCollection = new ArrayList<Vertex>();
    	ArrayList<Edge> edges = myMap.get(v);
    	
    	for (Edge edge : edges) {
    		adjacentCollection.add(edge.getDestination());
    	}
    	return adjacentCollection;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
    	if(!myMap.containsKey(a) || !myMap.containsKey(b)) {
    		throw new IllegalArgumentException("One or both of your input vertices are invalid");
    	}
    	
    	ArrayList<Edge> edges = myMap.get(a);
    	
    	for (Edge edge : edges) {
    		if (edge.getSource().equals(a) && edge.getDestination().equals(b)) {
    			return edge.getWeight();
    		}
    	}
    	return -1;
    }
    
    /**
     * Returns the shortest path from the given Vertex a and Vertex b in the graph.
     * If there is no path it'll return a null.
     * All edge weights are assumed nonnegative.
     * Implements Dijkstra's algorithm to find shortest path.
     * @param a : the starting Vertex
     * @param b : the ending Vertex
     * @return a Path where the vertices indicate the path from a to be in the order it contains.
     * 		   It'll contain a (first) and b (last) and the cost is the cost of the path.
     * 		   It'll return a null if the b is not reachable from a.
     * @throws IllegalARgumentException if a or b do not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	if(!myMap.containsKey(a) || !myMap.containsKey(b)) {
    		throw new IllegalArgumentException("One or both of your input vertices are invalid");
    	}
    	
    	Map<Vertex, Vertex> path = new HashMap<Vertex, Vertex>();
    	Map<Vertex, Integer> costMap = new HashMap<Vertex, Integer>();
    	Map<Vertex, Boolean> knownNodes = new HashMap<Vertex, Boolean>();
    	
    	if(a.equals(b)) {
    		List<Vertex> oneVertex = new ArrayList<Vertex>();
			oneVertex.add(a);
			return new Path(oneVertex, 0);
    	}
    	
    	for (Vertex tempVertex: myMap.keySet()) {
    		path.put(tempVertex, null);
    		costMap.put(tempVertex, Integer.MAX_VALUE);
    		knownNodes.put(tempVertex, false);
    	}
    	
    	costMap.put(a, 0);
    	
    	Vertex min = null;
    	
    	//Implementation of Dijkstra starts here!
    	while (knownNodes.values().contains(false)) {
    		
    		min = findMin(knownNodes, costMap); 
    		knownNodes.put(min, true);
    		
    		//Compares for adjacentVertices in the Graph
    		for (Vertex adj: adjacentVertices(min)) { 
    			if (knownNodes.get(adj) == false) { 
    				
    				int cost1 = costMap.get(min) + edgeCost(min, adj); 
    				int cost2 = costMap.get(adj); 
    				
    				if (cost1 < cost2) {
    					costMap.put(adj, cost1);
    					path.put(adj, min);
    				}
    			}
    		}
    	}
    	
    	//Creates the path that gets printed in Find Path
    	Vertex current = b;
    	List<Vertex> preFinalList = new ArrayList<Vertex>();
    	List<Vertex> finalList = new ArrayList<Vertex>();
    	
    	while(!current.equals(a)) {
    		if(path.get(b) == null) {
    			return null;
    		}
    		preFinalList.add(current);
    		current = path.get(current);
    	}
    	preFinalList.add(a);
    
    	//Reverses the above arrayList to ensure that printed Path way follows
    	//start to finish. (ex. a => b => c instead of c => b => a)
		for(int i = preFinalList.size() - 1 ; i >=0; i--) {
			Vertex add = preFinalList.get(i);
			finalList.add(add);
		}
		
    	return new Path(finalList, costMap.get(b));
    }
    
    /**
     * Private helpers to find unknown Vertex with the smallest cost
     * @param a map containing whether or not a Vertex is known or unknown
     * @param a map containing the Vertex and the cost
     * @return the smallest cost Vertex
     */
    private Vertex findMin(Map<Vertex, Boolean> known, Map<Vertex, Integer> costMap) {
    	Vertex min = null;
    	
    	for (Vertex temp: costMap.keySet()) {
			if (known.get(temp) == false) { 
				if(min == null) {
					min = temp;
				}
				
				if (costMap.get(temp) < costMap.get(min)) {
					min = temp;
				}
			}	
    	}
    	return min;
   }
}
