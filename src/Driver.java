
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Driver extends PApplet{
	
	ArrayList<PVector> cities = new ArrayList<PVector>();	//Array of points which represent our cities
	int numCities = 50;
	int populationSize = 1000;
	double mutationRate = .75;
	int mutationStrength = 0;
	int genCount = 0;
	Population pop = new Population(populationSize,mutationRate,cities,numCities);
	Salesman fittest = new Salesman(numCities);
	int offset = -90;

	
	//MAIN
	public static void main(String[] args){
			
		PApplet.main("Driver");
	}
		
	//SETTINGS
	public void settings(){
			
		size(800,400);
	}
		
	//SETUP
	public void setup(){
	   	
		
	   	frameRate(99999999);	
		
		//Create random points between the width and height of the window
	   	float rx = 0;
    	for(int i = 0; i < numCities; i++){
    		
	    	
	    	float ry = (height/4) + random(20);
	    	cities.add(new PVector(rx,ry));
	    	rx+=width/numCities;
    	}
    	
    	pop.createInitialPop();
		
	}
	   
	//DRAW
	public void draw(){
	    
		background(0);
		
		//Draw the points to the window
    	stroke(255,0,0);
    	fill(255,0,0);
    	ellipse(cities.get(0).x, cities.get(0).y,15,15);
    	for(int i = 1; i < cities.size(); i++){
    		
    		stroke(100,100,100);
        	fill(100,100,100);
    		ellipse(cities.get(i).x, cities.get(i).y,8,8);	
    	}
    	
    	pop.createNewGeneration();
    	
    	//Draw the fittest member of the current generation
    	Salesman currentFittest = pop.getFittestMember();
    	int[] genes = currentFittest.getGenes();
    	for(int i = 0; i < genes.length - 1; i++){
    		
    		int currentCity = genes[i];
    		int nextCity = genes[i+1];
    		
    		line(cities.get(currentCity).x,cities.get(currentCity).y,cities.get(nextCity).x,cities.get(nextCity).y);
    	}
    	
    	
    	//Draw the points to the window
    	stroke(255,0,0);
    	fill(255,0,0);
    	ellipse(cities.get(0).x, cities.get(0).y - offset,15,15);
    	for(int i = 1; i < cities.size(); i++){
    		
    		stroke(100,100,100);
        	fill(100,100,100);
    		ellipse(cities.get(i).x, cities.get(i).y - offset,8,8);	
    	}
    	
    	//Draw the fittest member among all generations
    	if(fittest.compare(currentFittest, fittest) == -1){
    		fittest = currentFittest;
    	}
    	
    	
    	int[] bestGenes = fittest.getGenes();
    	for(int i = 0; i < bestGenes.length - 1; i++){
    		
    		int currentCity = bestGenes[i];
    		int nextCity = bestGenes[i+1];
    		
    		stroke(0,255,0);
    		line(cities.get(currentCity).x,cities.get(currentCity).y-offset,cities.get(nextCity).x,cities.get(nextCity).y-offset);
    	}
    	
    	
    	System.out.println(genCount);
    	genCount++;
    	
	}

}
