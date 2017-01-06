import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import processing.core.PVector;

public class Population {
	
	private ArrayList<Salesman> population;
	private PriorityQueue<Salesman> heapPopulation;
	private ArrayList<PVector> cities;
	private double mutationRate;
	private int size;
	private int numCities;
	private static Random r = new Random();
	Comparator<Salesman> comparator = new Salesman(1);
	
	//Constructor to initialize a population of size n
	public Population(int n, double mutationRate, ArrayList<PVector> cities, int numCities){
		
		size = n;
		this.mutationRate = mutationRate;
		this.numCities = numCities;
		heapPopulation = new PriorityQueue<Salesman>(comparator);
		population = new ArrayList<Salesman>();
		this.cities = cities;
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
		
		parents[0] = heapPopulation.poll();
		parents[1] = heapPopulation.peek();
		
		if(!Salesman.equals(parents[0], parents[1]))
			parents[1] = heapPopulation.poll();
		else{
			while(Salesman.equals(parents[0], parents[1])){
				if(heapPopulation.size() == 1)
					break;
				else
					parents[1] = heapPopulation.poll();
			}
		}
		
		return parents;
	}
	
	public void createNewGeneration(){
		
		ArrayList<Salesman> newPop = new ArrayList<Salesman>();
		PriorityQueue<Salesman> newHeap = new PriorityQueue<Salesman>(comparator);

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
	public Salesman getFittestMember(){
		
		return heapPopulation.peek();
	}
	
	public String toString(){
		
		return population.toString();
	}
}
