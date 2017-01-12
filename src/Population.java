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
	private Salesman[] fittestParents;				//Parents that created the fittest member
	private static Random r = new Random();			//Random number generator
	private double mutationRate;					//Mutation rate
	private int size;								//Size of this population
	private int numCities;							//Number of cities
	private int genCount;							//Number of generations
	private int bestGen;							//Generation the best salesman was found in
	
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
		genCount = 0;
	}
	
	//Creates our initial random population
	public void createInitialPop(){
		
		for(int i = 0; i < size; i++){
			population.add(new Salesman(numCities));
			population.get(i).fitness(cities);
			heapPopulation.add(population.get(i));
		}
	}
	
	
	//Tournament selection
	//Adjusting the tournament size will allow for stronger members to be selected
	public Salesman selection(int tournamentSize){
		
		Salesman winner = fittest;
		
		//Create the tournament pool
		//The pool is a heap, so the top of the heap will have the winner of the tournament
		PriorityQueue<Salesman> tournamentPool = new PriorityQueue<Salesman>(comparator);
		
		//Pick random candidates from the current population and add them to the heap
		for(int i = 0; i < tournamentSize; i++){
			int candidate = r.nextInt(size);
			tournamentPool.add(population.get(candidate));
		}
		
		//Select a winner
		//The winner will always be on top of the heap, but to promote a little variation we won't always select
		//the top member.
		for(int i = 0; i < tournamentSize; i++){
			
			if(Math.random() < .5){
				winner = tournamentPool.poll();
				break;
			}
		}
		
		return winner;
	}
	
	//Creates the next generation
	//In this method we perform selection on the current population and create 'n' number of children
	//(n being the size of the population) from the parents selected. The new population then overrides
	//the current one.
	public void createNewGeneration(){
		
		//New population and heap for next generation
		ArrayList<Salesman> newPop = new ArrayList<Salesman>();
		PriorityQueue<Salesman> newHeap = new PriorityQueue<Salesman>(comparator);
		
		//Parents for the selection process
		Salesman[] parents = new Salesman[2];
		
		for(int i = 0; i < population.size(); i++){
			
			//Select parents through tournament selection
			parents[0] = selection(size/10);
			parents[1] = selection(size/10);
			
			//Use those parents to create a child
			Salesman child = Salesman.reproduce(parents[0], parents[1], mutationRate);
			
			//Add the child to the new population
			newPop.add(child);
			
			//Calculate its fitness
			newPop.get(i).fitness(cities);
			
			//Add it to the heap aswell
			newHeap.add(child);
		}
		
		//Update fittest parents if applicable
		if(newHeap.peek().getFitness() > fittest.getFitness()){
			fittestParents = parents;
		}
		
		//Update fittest salesman if applicable
		if(newHeap.peek().getFitness() > fittest.getFitness()){
			bestGen = genCount;
			fittest = newHeap.peek();
		}
		
		//Start the new gen
		heapPopulation = newHeap;
		population = newPop;
		genCount++;
	}
	
	//Gets the fittest member of the current population
	public Salesman getCurrentFittestMember(){
		
		return heapPopulation.peek();
	}
	
	//Gets the fittest member of all generations
	public Salesman getFittestMember(){
		
		return fittest;
	}
	
	//Gets the generation number the best salesman was found in
	public int getBestGenNum(){
		
		return bestGen;
	}
	
	//Get generation count
	public int getGenCount(){
		
		return genCount;
	}
			
	public String toString(){
		
		return population.toString();
	}
}
