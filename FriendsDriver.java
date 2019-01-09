package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class FriendsDriver {
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc = new Scanner(new File("test3.txt"));
		Graph graph = new Graph(sc);		
		
		for (int i = 0; i < graph.members.length; i++) {
			System.out.print(graph.members[i].name + " : ");
			if (graph.members[i].student)
				System.out.println(graph.members[i].school);
			else
				System.out.println("none");
		}
		System.out.println("\n\n");
		
		ArrayList<String> shortest = Friends.shortestChain(graph, "ricardo", "sam");
		if(shortest == null) {
			System.out.println("Found no link");
		} else {
			for(String person : shortest) {
				System.out.println(person);
			}
		}
		System.out.println("\n\n");
		
		ArrayList<ArrayList<String>> cliques = Friends.cliques(graph, "rutgers");
		if(cliques != null) {
			for(ArrayList<String> perClique : cliques) {
				System.out.println(perClique.toString());
			}
		}
		System.out.println("\n\n");
		
		ArrayList<String> connectors = Friends.connectors(graph);
		for(String connector : connectors) {
			System.out.println(connector);
		}
		
	}
	
}