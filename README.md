# The project in action!
**100 city problem:**

The top graph represents the best route in the current generation

The bottom graph represents the best route we've found so far

IMAGE HERE

If you don't know what the TSP is, learn more below!
# The Travelling Salesman Problem Statement
Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city? 

# Brute Force Solution
To brute force a solution to the Travelling Salesman Problem (TSP) we could simply measure the distance of every possible city permutation and return the shortest distance. Unfortunately the running time for this approach is O(n!) which quickly scales out of control.
For example, the number of permutations in a:
* 5 city problem: 5! = 5 * 4 * 3 * 2 * 1 =  **150 permutations**
* 10 city problem: 10! = 10 * 9 * .. * 1 =  **3628800 permutations**
* 20 city problem: 20! = 20 * 19 * .. * 1 = **2432902008176640000 permutations**

Even a 20 city problem is not something we can brute-force in a reasonable time frame!

# Enter the Genetic Algorithm!
Uses Darwin’s Theory of Evolution to “evolve” towards **approximate** solutions in a programmatic way.
* **General idea:**
  * We have a population of members that start with random genes
  * Each member has “genes” that represent their genetic make-up
  * Each set of genes is given a fitness value that represents how effective those genes are
  * Each member can reproduce with another member to create a child member. The child member should have a genetic make-up that is a combination of the parents genes.
  * During reproduction there is a chance of a “mutation” that can randomly modify the child’s genes.
  * Members that have higher fitness values are more likely to reproduce than those with lower fitness values
  * Parents “die” and their children make up the next generation. The hope is only the strongest parents were able to reproduce so the child generation is “stronger” than the previous generation.
  * We continue this process for hundreds or potentially thousands of generations. The final generations should get close to the optimal solution!

# Applying a Genetic Algorithm to the Travelling Salesman problem
**Parameters:**
* **Genes:** A route of cities
* **Fitness value:** The distance of the route. The lower the distance, the stronger the set of genes.
* **Salesman:** Has genes and fitness value
* **Population:** A list of Salesman

**Algorithm idea:**

**1.** Initialize population of size n with random genes

**2.** Compute fitness value of each member of population

**3.** Select members that have strong fitness values to reproduce
    
    a. Take member 1 and member 2 and “cross” their genes together
    
    b. Give a chance that the child has a mutation and their genes are randomly modified

**4.** Perform reproduction n times to create an n sized child generation

**5.** Go back to step 2 where we now use the child generation
