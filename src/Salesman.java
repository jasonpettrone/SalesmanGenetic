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
	
	private double fitness;	//This set of genes fitness
	
	private static Random r  = new Random();
	
	//Constructor - Create a DNA object with a random order
	//All salesman, regardless of their order, must start at the same city
	public Salesman(int numCities){
		
		ArrayList<Integer> cities = new ArrayList<Integer>();
		order = new int[numCities];
		this.fitness = 0;
		order[0] = 0;
		
		for(int i = 1; i < numCities; i++)
			cities.add(i);
		
		for(int i = numCities-1; i >= 1; i--){
			
			if(i==0)
				order[i] = cities.remove(0);
			else
				order[i] = cities.remove(r.nextInt(i));
		}
	}
	
	//Constructor - create a DNA object with a specified order
	public Salesman(int[] order){
		
		this.fitness = 0;
		this.order = order;
	}
	
	//Get genes
	public int[] getGenes(){
		
		return order;
	}
	
	public double getFitness(){
		
		return this.fitness;
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
		
		fitness = 1.0/fitness;
		this.fitness = fitness;
		
		return fitness;
	}
	
	//Reproduction method - Mates two parents to create a child, returns child
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
		for(int i = 1; i < parent1Genes.length; i++){
			
			//What will we do if there is a repeat value?
			//We will go into the next parents genes and find the closest index to the current one
			//that doesn't have a repeating value. This will maintain some level of order between parent and child.
			
			//Add first parents genes
			if(i%2 == 0){
				
				//Encountered repeat value. See above for how we deal with this.
				if(genePool.add(parent1Genes[i]) == false){
					
					int left;	//Cursor that will go left from the current position
					int right;	//Cursor that will go right from the current position
					
					for(left = i,right = i; left>=1 || right<parent1Genes.length; left--,right++){
						
						//If left cursor reaches the min, repurpose to search from the top
						if(left == 1)
							left = parent1Genes.length - 1;
						
						//If right cursor reaches max, repurpose to search from bottom
						if(right == parent1Genes.length - 1)
							right = 1;
						
						//Add
						if(genePool.add(parent2Genes[right])){
							childGenes[i] = parent2Genes[right];
							break;
						}
						
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
					
					for(left = i,right = i; left>=1 || right<parent1Genes.length; left--,right++){
						
						if(left == 1)
							left = parent1Genes.length - 1;
						
						if(right == parent1Genes.length - 1)
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
		
		//Mutation strength will modify how many chances this child has at a mutation
		//Mutation strength will increase as the number of cities increase.
		int mutationStrength = (int)Math.round(childGenes.length * .05);
		
		//Chance a mutation on the child
		for(int i = 0; i < mutationStrength; i++){
			if(Math.random() < mutationRate)
				child.mutate();
		}
		
		return child;
		
	}
	
	//Mutation function
	//Mutation is done by swapping 2 random genes (indexes) with one another. The higher the mutation strength, the more swaps we do
	public void mutate(){
		
		//We add one to the random numbers because we don't want to swap the starting city
		int first = r.nextInt(order.length-1) + 1;
		int second = r.nextInt(order.length-1) + 1;
			
		int temp = order[first];
		order[first] = order[second];
		order[second] = temp;
		
	}
	
	@Override
	public int compare(Salesman o1, Salesman o2) {
		
		if(o1.getFitness() > o2.getFitness())
			return -1;
		else if(o1.getFitness() < o2.getFitness())
			return 1;
		else
			return 0;	
	}
	
	public static boolean equals(Salesman o1, Salesman o2){
		
		int[] salesman1Genes = o1.getGenes();
		int[] salesman2Genes = o2.getGenes();
		
		return Arrays.equals(salesman1Genes, salesman2Genes);
	}
	
	public String toString(){
		
		return Arrays.toString(order);
	}
	
}
