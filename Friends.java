package friends;

import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> res = new ArrayList<String>();
		
		Queue<Person> people = new Queue<>();
		boolean[] visited = new boolean[g.members.length];
		int[] prev = new int[g.members.length];
		
		Person person1 = new Person();
		Person person2 = new Person();
		for (int i = 0; i < g.members.length; i++) {
			if (g.members[i].name.equals(p1))
				person1 = g.members[i];
			else if (g.members[i].name.equals(p2))
				person2 = g.members[i];
		}
		
		boolean found = false;
		
		for(int i = 0; i < prev.length; i++)
			prev[i] = -1;
		
		people.enqueue(person1);
		while(!people.isEmpty()) {
			Person person = people.dequeue();
			if(person.equals(person2)) {
				found = true;
				break;
			}
			
			int num = g.map.get(person.name);
			visited[num] = true;
			
			Friend temp = g.members[num].first;
			while(temp != null) {
				int index = temp.fnum;
				if(!visited[index]) {
					people.enqueue(g.members[index]);
					visited[index] = true;
					prev[index] = num;
				}
				temp = temp.next;
			}
		}
		
		if(found) {
			Stack<String> namesReversed = new Stack<>();
			
			int backIndex = g.map.get(person2.name);
			int previous = prev[backIndex];
			namesReversed.push(person2.name);
			
			while(previous != -1) {
				namesReversed.push(g.members[previous].name);
				previous = prev[previous];
			}
			
			while(!namesReversed.isEmpty())
				res.add(namesReversed.pop());
			
			return res;
		}
		else
			return null;
	}
	
	/*private static void findPath (int start, int curr , int end, ArrayList<Integer> array, Graph g, ArrayList<Integer> result, int distance) {
		array.add(start);
		
		Friend temp = g.members[start].first;
		
		while (temp != null) {
			if (temp.fnum == end) {
				array.add(temp.fnum);
				if (array.size() < result.size())
					result = array;
				array = new ArrayList<Integer>();
				//return array;
			}
			else if (!array.contains(temp.fnum))
				findPath(start, temp.fnum, end, array, g, result, distance);
			else {
				
			}
			
			temp = temp.next;
		}

		//return result;
	}*/
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for (int i = 0; i < g.members.length; i++) {
			if (g.members[i].student) {
				if (g.members[i].school.equals(school))
					temp.add(i);
			}
		}
		
		while (!temp.isEmpty()) {
			ArrayList<String> temp2 = new ArrayList<String>();
			ArrayList<Integer> indexes = searchThrough(g, school, new ArrayList<Integer>(), temp.get(temp.size() - 1));
			ArrayList<Integer> finalIndex = new ArrayList<Integer>();
			//System.out.println("*");
			
			for (int i = 0; i < indexes.size(); i++) {
				if (!finalIndex.contains(indexes.get(i)))
					finalIndex.add(indexes.get(i));
			}
			
			indexes = finalIndex;
			int size = indexes.size();
			for (int j = 0; j < size; j++) {
				temp2.add(g.members[indexes.get(j)].name);
				//System.out.println("-" + indexes.get(j));
				temp.remove(indexes.get(j));
			}
			
			res.add(temp2);
		}
		
		return res;
	}
	
	private static ArrayList<Integer> searchThrough (Graph g, String school, ArrayList<Integer> array, int current) {
		if (!array.contains(current))
			array.add(current);
		
		Friend temp = g.members[current].first;
		
		while (temp != null) {
			int index = temp.fnum;
			if (g.members[index].student) {
				if (g.members[index].school.equals(school) && !array.contains(index))
					array.addAll(searchThrough(g, school, array, index));
			}
			temp = temp.next;
		}
		
		return array;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> res = new ArrayList<>();
		
		int size = g.members.length;
		boolean[] visited = new boolean[size];
		boolean[] backedUp = new boolean[size];
		int[] dfsnums = new int[size];
		int[] backs = new int[size];

		for(int i = 0; i < size; i++) {
			if(!visited[i])
				connecting(g, i, i, i, visited, backedUp, dfsnums, backs, res);
		}
		
		return res;
	}
	
	private static void connecting(Graph g, int start, int prev, int current, boolean[] visited, boolean[] backedUp, int[] dfsnums, int backs[], ArrayList<String> res) {
		if(visited[current])
			return;
		else {
			dfsnums[current] = dfsnums[prev] + 1;
			backs[current] = dfsnums[current];
			visited[current] = true;
			
			Friend temp = g.members[current].first;
			while(temp != null) {
				if(visited[temp.fnum]) {
					if (dfsnums[temp.fnum] < backs[current])
						backs[current] = dfsnums[temp.fnum];
				}
				else {
					connecting(g, start, current, temp.fnum, visited, backedUp, dfsnums, backs, res);
					
					if(!res.contains(g.members[current].name) && dfsnums[current] <= backs[temp.fnum]) {
						if(current != start || backedUp[current])
							res.add(g.members[current].name);
					}
					
					if(dfsnums[current] > backs[temp.fnum]){
						if (backs[current] > backs[temp.fnum])
							backs[current] = backs[temp.fnum];
					}
					
					backedUp[current] = true;
				}
				temp = temp.next;
			}
		}
	}
}

