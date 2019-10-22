import java.util.*;
/**
 * class that implements depth first search algorithm that finds a path given start vertex 
 * @author Dana Curca, dcurca, 250976773
 *
 */
public class DepthFirstSearch {
	//initialize instance variables 
	RouteGraph inputGraph;
	Stack<Intersection> stack;
	
	public DepthFirstSearch(RouteGraph graph) { 
		stack = new Stack<Intersection>();
		inputGraph = graph;
	}
	/**
	 * creates a stack and returns it with the necessary paths 
	 */
	public Stack<Intersection> path(Intersection startVertex, Intersection endVertex) throws GraphException {
		Stack<Intersection> newStack = new Stack<Intersection>();
		pathRec(startVertex, endVertex);
		return newStack;
	}
	/**
	 * implements depth first search algorithm to find paths 
	 */
	public boolean pathRec(Intersection startVertex, Intersection endVertex) throws GraphException {
		startVertex.setMark(true);
		stack.push(startVertex);
		if(startVertex.getLabel() == endVertex.getLabel()) {
			return true; }
		//checks to make sure there is a next intersection 
		else { 
			Iterator<Road> edges = inputGraph.incidentRoads(startVertex);
			while(edges.hasNext()) {
				Road road = edges.next();
				if(!road.getFirstEndpoint().getMark()) {
				//checks the undirected edge whether it is the second endpoint 
					if(pathRec(road.getFirstEndpoint(), endVertex)) {
						return true;
					}
				}
				else if(!road.getFirstEndpoint().getMark()) {
				//or if it is the first endpoint 
					if (pathRec(road.getSecondEndpoint(),endVertex)) {
						return true; 
					}
				}
			}
		stack.pop();
		return false;
			}
		}
	}


