import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import processing.core.PVector;

public class Population {
	
	private ArrayList<Salesman> population;			//Population of salesman
	private PriorityQueue<Salesman> heapPopulation;	//Heap of salesman (used for sorting)
	private ArrayList<PVector> cities;				//Cities
	private Salesman fittest;						//Fittest member of all generations
	private static Random r = new Random();			//Random number generator
	private double mutationRate;					//Mutation rate
	private int size;								//Size of this population
	private int numCities;							//Number of cities
	
	Comparator<Salesman> comparator = new Salesman(1);
	
	//Constructor to initialize a population of size n
	public Population(int n, double mutationRate, ArrayList<PVector> cities, int numCities){
		
		size = n;
		this.mutationRate = mutationRate;
		this.numCities = numCities;
		heapPopulation = new PriorityQueue<Salesman>(comparator);
		population = new ArrayList<Salesman>();
		this.cities = cities;
		fittest = new Salesman(numCities);
	}
	
	//Creates our initial random population
	public void createInitialPop(){
		
		for(int i = 0; i < size; i++){
			population.add(new Salesman(numCities));
			population.get(i).fitness(cities);
			heapPopulation.add(population.get(i));
		}
	}
	
	//Survival of the fittest. Chooses what parents will create the next generation.
	//The parents are chosen off the top of a heap, so the two strongest parents will create the next generation
	public Salesman[] selection(ArrayList<Salesman> genePool){
		
		Salesman[] parents = new Salesman[2];
		
		parents[0] = genePool.get(r.nextInt(genePool.size()));
		parents[1] = genePool.get(r.nextInt(genePool.size()));
		
		while(Salesman.equals(parents[0], parents[1])){
			parents[1] = genePool.get(r.nextInt(genePool.size()));
		}
			
		
		//Update our fittest salesman if applicable
		if(genePool.get(0).getFitness() > fittest.getFitness())
			fittest = genePool.get(0);
		
		/*
		 * 		parents[0] = heapPopulation.poll();
		parents[1] = heapPopulation.poll();
		 * 
		//Check if the top two parents are equal. If they are, we search for another parent not equal to the first one
		while(Salesman.equals(parents[0], parents[1])){
			if(heapPopulation.size() == 1)
				break;
			else
				parents[1] = heapPopulation.poll();
		}
		*/
		
		return parents;
	}
	
	//Survival of the fittest. Chooses what parents will create the next generation.
	//The parents are chosen off the top of a heap, so the two strongest parents will create the next generation
	public Salesman[] selection(){
			
		Salesman[] parents = new Salesman[2];
			
		parents[0] = heapPopulation.poll();
		parents[1] = heapPopulation.poll();
			
		//Update our fittest salesman if applicable
		if(parents[0].getFitness() > fittest.getFitness())
			fittest = parents[0];
		 
		//Check if the top two parents are equal. If they are, we search for another parent not equal to the first one
		while(Salesman.equals(parents[0], parents[1])){
			if(heapPopulation.size() == 1)
				break;
			else
				parents[1] = heapPopulation.poll();
		}
		
		return parents;
	}
	
	//Creates a gene pool for selection
	//The gene pool represents the top 33% of the population
	public ArrayList<Salesman> createGenePool(){
		
		ArrayList<Salesman> genePool = new ArrayList<Salesman>();
		
		for(int i = 0; i < size/2; i++)
			genePool.add(heapPopulation.poll());
		
		return genePool;
		
	}
	
	//Creates the next generation
	public void createNewGeneration(){
		
		ArrayList<Salesman> newPop = new ArrayList<Salesman>();
		PriorityQueue<Salesman> newHeap = new PriorityQueue<Salesman>(comparator);
		//ArrayList<Salesman> genePool = createGenePool();

		Salesman[] parents = selection();
		for(int i = 0; i < population.size(); i++){
			
			Salesman child = Salesman.reproduce(parents[0], parents[1], mutationRate);
			newPop.add(child);
			newPop.get(i).fitness(cities);
			newHeap.add(child);
		}	
		
		heapPopulation = newHeap;
		population = newPop;
		
	}
	
	//Gets the fittest member of the current population
	public Salesman getCurrentFittestMember(){
		
		return heapPopulation.peek();
	}
	
	//Gets the fittest member of all generations
	public Salesman getFittestMember(){
		
		return fittest;
	}
	
	//Gives an average fitness of two parents reproducing for a set size
	public double sample(Salesman parent1, Salesman parent2, int size){
		
		double average = 0;
		
		for(int i = 0; i < size; i++){
			
			Salesman child;
			child = Salesman.reproduce(parent1, parent2, mutationRate);
			average += (1+child.fitness(cities));
		}
		
		return average/(double)size;
	}
			
	public String toString(){
		
		return population.toString();
	}
}
