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
	public Salesman[] selection(){
			
		Salesman[] parents = new Salesman[2];
			
		parents[0] = heapPopulation.poll();	//Parent 1
		parents[1] = heapPopulation.poll();	//Parent 2
			
		//Check if the top two parents are equal. If they are, we search for another parent not equal to the first one
		while(Salesman.equals(parents[0], parents[1])){
			if(heapPopulation.size() == 1){
				break;
			}
			else
				parents[1] = heapPopulation.poll();
		}
		
		return parents;	
	}
	
	//Tournament selection
	public Salesman selection(int tournamentSize){
		
		Salesman winner;
		
		//Create the tournament pool
		//The pool is a heap, so the top of the heap will be the winner of the tournament
		PriorityQueue<Salesman> tournamentPool = new PriorityQueue<Salesman>(comparator);
		for(int i = 0; i < tournamentSize; i++){
			int candidate = r.nextInt(size);
			tournamentPool.add(population.get(candidate));
		}
		
		winner = tournamentPool.peek();
		
		return winner;
	}
	
	
	//Creates the next generation
	public void createNewGeneration(){
		
		//New population and heap for next generation
		ArrayList<Salesman> newPop = new ArrayList<Salesman>();
		PriorityQueue<Salesman> newHeap = new PriorityQueue<Salesman>(comparator);
		
		//Parents for the selection process
		Salesman[] parents = new Salesman[2];
		
		for(int i = 0; i < population.size(); i++){
			
			//Select parents through tournament selection
			parents[0] = selection(size/4);
			parents[1] = selection(size/4);
			
			//Use those parents to create a child
			Salesman child = Salesman.reproduce(parents[0], parents[1], mutationRate);
			
			//Add the child to the new population
			newPop.add(child);
			newPop.get(i).fitness(cities);
			newHeap.add(child);
		}
		
		//Update fittest parents if applicable
		if(newHeap.peek().getFitness() > fittest.getFitness()){
			fittestParents = parents;
		}
		
		//Update fittest salesman if applicable
		if(heapPopulation.peek().getFitness() > fittest.getFitness())
			fittest = heapPopulation.peek();
		
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
