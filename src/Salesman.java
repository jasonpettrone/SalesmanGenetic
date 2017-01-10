import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PVector;

public class Salesman implements Comparator<Salesman>{
	
	private int[] order;	//Order of cities the salesman will traverse
							//This is the salesman's genes
	
	private double fitness;			//This set of genes fitness
	private double dist;			//Distance this salesman travelled based on its order
	private static int numCities;	//Number of cities
	private static Random r  = new Random();
	
	//Constructor - Create a Salesman object with a random order
	//All salesman, regardless of their order, must start at the same city and end at the same city
	public Salesman(int numCities){
		
		ArrayList<Integer> cities = new ArrayList<Integer>();
		Salesman.numCities = numCities;
		order = new int[numCities+1];
		this.fitness = 0;
		order[0] = 0;
		order[numCities] = 0;
		
		for(int i = 1; i < numCities; i++)
			cities.add(i);
		
		for(int i = numCities-1; i >= 1; i--){
			
			if(i==0)
				order[i] = cities.remove(0);
			else
				order[i] = cities.remove(r.nextInt(i));
		}
	}
	
	//Constructor - create a Salesman object with a specified order
	public Salesman(int[] order){
		
		this.fitness = 0;
		this.order = order;
	}
	
	//Get genes
	public int[] getGenes(){
		
		return order;
	}
	
	//Get fitness
	public double getFitness(){
		
		return this.fitness;
	}
	
	//Get distance
	public double getDist(){
		
		return dist;
	}
	
	//Fitness function
	//Fitness of a given salesman is defined by how short of a distance it took for them to traverse the cities
	public double fitness(ArrayList<PVector> cities){
		
		double fitness = 0;
		
		for(int i = 0; i < order.length-1; i++){
			int currentCity = order[i];
			int nextCity = order[i+1];
			
			fitness += PApplet.dist(cities.get(currentCity).x, cities.get(currentCity).y, cities.get(nextCity).x, cities.get(nextCity).y);
		}
		dist = fitness;
		fitness = 1.0/fitness;
		this.fitness = fitness;
		
		return fitness;
	}
	
	//Reproduction method - Mates two parents to create a child, returns the child
	//The child's genes are produced by alternating between the two parents genes 
	public static Salesman reproduce(Salesman parent1, Salesman parent2, double mutationRate){
		
		Salesman child;
		int[] parent1Genes = parent1.getGenes();
		int[] parent2Genes = parent2.getGenes();
		int[] childGenes = new int[parent1Genes.length];
		
		
		//When we reproduce these two salesman we need to make sure that we don't make them go to the same city twice.
		//Since our order should never have repeating values, we can use a hash set to check if we are trying to add the
		//same city twice. (when adding a repeat value to a hashset it will return false).
		HashSet<Integer> genePool = new HashSet<Integer>();	
		
		//Alternate between the two parents to produce the child
		for(int i = 1; i < numCities; i++){
			
			//What will we do if there is a repeat value?
			//We will go into the next parents genes and find the closest index to the current one
			//that doesn't have a repeating value. This will maintain some level of order between parent and child.
			
			//Add first parents genes
			if(i%2 == 0){
				
				//Encountered repeat value. See above for how we deal with this.
				if(genePool.add(parent1Genes[i]) == false){
					
					int left;	//Cursor that will go left from the current position
					int right;	//Cursor that will go right from the current position
					
					for(left = i,right = i; left>=1 || right<numCities; left--,right++){
						
						//If left cursor reaches the min, repurpose to search from the top
						if(left == 1)
							left = numCities - 1;
						
						//If right cursor reaches max, repurpose to search from bottom
						if(right == numCities)
							right = 1;
						
						//Add if right cursor finds a non repeat
						if(genePool.add(parent2Genes[right])){
							childGenes[i] = parent2Genes[right];
							break;
						}
						
						//Add if left cursor finds a non repeat
						if(genePool.add(parent2Genes[left])){
							childGenes[i] = parent2Genes[left];
							break;
						}
							
					}
				}
				//Not a repeat value, add normally
				else
					childGenes[i] = parent1Genes[i];
			}
			//Add second parents genes
			else{
				
				if(genePool.add(parent2Genes[i]) == false){
					
					int left;	//Cursor that will go left from the current position
					int right;	//Cursor that will go right from the current position
					
					for(left = i,right = i; left>=1 || right<numCities; left--,right++){
						
						if(left == 1)
							left = numCities - 1;
						
						if(right == numCities - 1)
							right = 1;
						
						if(genePool.add(parent1Genes[right])){
							childGenes[i] = parent1Genes[right];
							break;
						}
						
						if(genePool.add(parent1Genes[left])){
							childGenes[i] = parent1Genes[left];
							break;
						}
					}
				}
				else
					childGenes[i] = parent2Genes[i];		
			}
			
		}
		
		//Child is born!
		child = new Salesman(childGenes);
		
		//Chance a mutation on the child
		if(Math.random() < mutationRate)
			child.mutate();
		
		return child;
		
	}
	
	//Second mutation function
	//Mutation is done by reversing a random range of genes.
	//Example: [1,2,3,4,5,6] -> random range to reverse: (1,4) -> [1,5,4,3,2,6]
	public void mutate(){
		
		//Get 2 random indexes for our range
		int begin = r.nextInt(numCities - 1) + 1;
		int end = r.nextInt(numCities - 1) + 1;
		
		//Make sure the two indexes are not equal
		while(end == begin)
			end = r.nextInt(numCities - 1) + 1;
		
		//Make sure begin is the starting index and end is the ending index
		if(begin > end){
			int temp = begin;
			begin = end;
			end = temp;
		}
		
		//Get the midpoint between the indexes. This will help us do the reversing in O(n) time.
		int midpoint = (begin + end) / 2;
		
		//Start reversing
		for(int i = begin; i <= midpoint; i++, end--){
			
			int temp = order[i];
			order[i] = order[end];
			order[end] = temp;
		}
		
	}
	
	//Comparator based on fitness
	@Override
	public int compare(Salesman o1, Salesman o2) {
		
		if(o1.getFitness() > o2.getFitness())
			return -1;
		else if(o1.getFitness() < o2.getFitness())
			return 1;
		else
			return 0;	
	}
	
	//Equal based on order
	public static boolean equals(Salesman o1, Salesman o2){
		
		int[] salesman1Genes = o1.getGenes();
		int[] salesman2Genes = o2.getGenes();
		
		return Arrays.equals(salesman1Genes, salesman2Genes);
	}
	
	public String toString(){
		
		return Arrays.toString(order);
	}
	
}
