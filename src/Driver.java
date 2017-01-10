
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Driver extends PApplet{
	
	ArrayList<PVector> cities = new ArrayList<PVector>();	//Array of points which represent our cities
	int numCities = 100;		//Number of cities
	int populationSize = 1000;	//Population size
	double mutationRate = .5;	//Mutation rate
	int genCount = 0;			//Number of generations accumulated
	
	//Create the population
	Population pop = new Population(populationSize,mutationRate,cities,numCities);
	
	int offset = -300;	//Offset is used for draw functionality. It handles drawing of the best gen

	
	//MAIN
	public static void main(String[] args){
			
		PApplet.main("Driver");
	}
		
	//SETTINGS
	public void settings(){
			
		size(1920,600);	//Window size
	}
	
		
	//SETUP
	public void setup(){
	   	
		
	   	frameRate(99999999);	
		
	   	cities.add(new PVector(20,80));	//Starting point
	   	
		//Create random points between the width and height of the window
    	for(int i = 0; i < numCities-1; i++){
    		
    		float rx = random(width);
	    	float ry = 60 + random(height/3);
	    	cities.add(new PVector(rx,ry));
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
    	
    	float currentDistance = 0;	//Current tour distance for this generation
    	
    	//Draw the fittest member of the current generation
    	Salesman currentFittest = pop.getCurrentFittestMember();
    	int[] genes = currentFittest.getGenes();
    	for(int i = 0; i < genes.length - 1; i++){
    		
    		int currentCity = genes[i];
    		int nextCity = genes[i+1];
    		
    		line(cities.get(currentCity).x,cities.get(currentCity).y,cities.get(nextCity).x,cities.get(nextCity).y);
    		currentDistance += dist(cities.get(currentCity).x,cities.get(currentCity).y,cities.get(nextCity).x,cities.get(nextCity).y);
    	}
    	
    	
    	//Draw the points to the window for the fittest member of all generations
    	stroke(255,0,0);
    	fill(255,0,0);
    	ellipse(cities.get(0).x, cities.get(0).y - offset,15,15);
    	for(int i = 1; i < cities.size(); i++){
    		
    		stroke(100,100,100);
        	fill(100,100,100);
    		ellipse(cities.get(i).x, cities.get(i).y - offset,8,8);	
    	}
    	
    	//Draw the fittest member among all generations    	
    	int[] bestGenes = pop.getFittestMember().getGenes();
    	float bestDistance = 0;
    	for(int i = 0; i < bestGenes.length - 1; i++){
    		
    		int currentCity = bestGenes[i];
    		int nextCity = bestGenes[i+1];
    		
    		stroke(0,255,0);
    		line(cities.get(currentCity).x,cities.get(currentCity).y-offset,cities.get(nextCity).x,cities.get(nextCity).y-offset);
    		bestDistance += dist(cities.get(currentCity).x,cities.get(currentCity).y-offset,cities.get(nextCity).x,cities.get(nextCity).y-offset);
    	}
    	
    	//Code for drawing text to the window (genCount, genDist, ect...)
    	stroke(255,255,255);
    	fill(255,255,255);
    	textSize(20);
    	text("Gen #: " + genCount, 0,30);
    	text("Gen distance: " + currentDistance, 0, 60);
    	text("Best distance: " + bestDistance, 0, 290);
    	genCount++;
	}

}
