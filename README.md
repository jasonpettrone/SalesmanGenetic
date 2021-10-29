# The Travelling Salesman Problem
Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city?

Trying to brute force this problem results in a running time of O(n!), which is really bad. We need to do better...

Shown below are examples of my genetic algorithm running to solve the TSP significantly faster than the brute force method. Additionally, below that is a more in-depth explanation of genetic algorithms as well as the Travelling Salesman Problem. Enjoy!

# The project in action!

The top graph represents the average route in the current generation.

The bottom graph represents the best route we've found so far among all generations.

**50 city problem:**
![50 City](https://user-images.githubusercontent.com/16530058/138945033-cf0a140b-8dac-41b2-b3bf-efc1b8806740.gif)

**100 city problem:**
![100 City](https://user-images.githubusercontent.com/16530058/138945077-5130541b-89f5-494b-b08e-1af30391a7c2.gif)

In case you're wondering how many permutations we'd have to check with a brute force solution for this 100 city case, here is the number:

100! = 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000

**Some quick math on this number:**

In 2023 we will debut the worlds fastest supercomputer, El Capitan, which can peform up to 2 quintillion calculations per second (https://www.cnet.com/tech/computing/el-capitan-supercomputer-blow-past-rivals-with-2-quintillion-calculations-per-second/).

Lets assume this computer could check 2 quintillion permutations of the TSP every second in the 100 city problem above. Lets try to figure out how long it would take for this computer to finish if we ran the brute force algorithm of the TSP.

2 quintillion = 2000000000000000000

 * 100! permutations / 2 quintillion permutations processed per second = **46663107721972076340849619428133350245357984132190810734296481947608799996614957804470731988078259143126848960413611879125592605458432000000 seconds to completion**

Lets convert the amount of seconds into years:

* seconds / 60 seconds per minute / 60 minutes per hour / 24 hours per day / 365 days a year = **1479677439179733521716438972226450730763507868220155084167189305796829020694284557473069888003496294492860507369787286882470592512000 years to completion**

For fun, lets see if we could complete the alorgithm before the heat death of the universe:

The heat death of the universe is expected to take around 10^100 years (https://phys.org/news/2015-09-fate-universeheat-death-big-rip.html)

* years to completion / 10^100 = **147967743917973352171643897222645 heat death timelines**

Darn.

Math done with the help of https://www.calculator.net/big-number-calculator.html

If you don't know what the TSP is or want to learn more about genetic algorithms, look below!

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
