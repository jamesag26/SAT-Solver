import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class WalkSAT {
	
	int bestFitness;

	public WalkSAT() {
		bestFitness = 0;
	}
	
	Vector<Integer> walkSATsolver(ArrayList<Vector<Integer>> clauses, int lit, int maxFlips, double p) {
		// Model holds the assignment of true/false (1/0) for the literals
		Vector<Integer> model = new Vector<Integer>(lit);
		// Initial random assignment of model
		for (int i = 0; i < lit; i++) {
			int randLit = ThreadLocalRandom.current().nextInt(0, 2);
			model.add(randLit);
		}
		
		for (int i = 0; i < maxFlips; i++) {
			// Checks for fitness of the model
			int modelFitness = fitness(clauses, model);
			// If fitness is equal to number of clauses that mean every clause is true 
			if (modelFitness == clauses.size()) {
				System.out.println("Model is true " + i);
				bestFitness = modelFitness;
				return model;
			}
			// If model has new best fitness score then set bestFtiness
			if (modelFitness > bestFitness) {
				bestFitness = modelFitness;
			}
			
			// Picks random false clause
			int fClauseIndex = randomFalseClause(clauses, model);
			// Finds a random double value from 0 to 1 for probability p
			double randP = ThreadLocalRandom.current().nextDouble(0, 1);
			
			// If randP < p  flip a random literal assignment in model from clause with index fClauseIndex
			if (randP < p) {
				// Choose random literal from fClauseIndex in clauses and flip the model value for it
				int randLit = ThreadLocalRandom.current().nextInt(0, clauses.get(fClauseIndex).size());
				int litFlip = clauses.get(fClauseIndex).get(randLit);
				if (model.get(Math.abs(litFlip) - 1) == 0) {
					model.set(Math.abs(litFlip) - 1, 1);
				} else {
					model.set(Math.abs(litFlip) - 1, 0);
				}
			// Else flip the literal assignment in model for whichever literal in clause has the greatest fitness	
			} else {
				// Keeps track of best fitness score and index of the literal that was flipped so best fitness literal gets flipped
				int bestFit = 0;
				int flipLit = -1;
				
				// Finds the fitness for each literal assignment flip in model and chooses best one to flip
				for (int j = 0; j < clauses.get(fClauseIndex).size(); j++) {
					// A temp model which will be used to determine the best literal to flip
					Vector<Integer> tempModel = new Vector<Integer>(model.size());
					tempModel.addAll(model);
					// Index in model for literal is equal to |literal| - 1
					int flipLitIndex = Math.abs(clauses.get(fClauseIndex).get(j)) - 1;
					// Flips value for literal in model
					if (tempModel.get(flipLitIndex) == 1) {
						tempModel.set(flipLitIndex, 0);
					} else {
						tempModel.set(flipLitIndex, 1);
					}
					// Finds fitness and compares to best fitness score
					int tempFit = fitness(clauses, model);
					if (tempFit >= bestFit) {
						bestFit = tempFit;
						flipLit = flipLitIndex;
					}
				}
				
				// Uses best flipped literal fitness score and flips that literal for model
				if (model.get(flipLit) == 0) {
					model.set(flipLit, 1);
				} else {
					model.set(flipLit, 0);
				}
			}
			System.out.println(model);
		}
		return model;
	}
	
	int randomFalseClause(ArrayList<Vector<Integer>> clauses, Vector<Integer> model) {
		// Contains list of indexes of false clauses
		Vector<Integer> falseClauses = new Vector<Integer>();
		
		// Checks which clauses are negative and puts their index into falseClauses vector
		for (int i = 0; i < clauses.size(); i++) {
			boolean clauseTrue = false;
			// Checks each literal to see if any are true which makes the clause true
			int clauseCount = 0;
			
			while (clauseCount < clauses.get(i).size() && !clauseTrue) {
				// If literal is positive then model value needs to be 1 to be true
				if (clauses.get(i).get(clauseCount) > 0) {
					if (model.get(Math.abs(clauses.get(i).get(clauseCount)) - 1) == 1) {
						clauseTrue = true;
					}
				// If literal is negative then model value needs to be 0 to be true
				} else {
					if (model.get(Math.abs(clauses.get(i).get(clauseCount)) - 1) == 0) {
						clauseTrue = true;
					}
				}
				clauseCount++;
			}
			// If clause is false then add index to false clause vector
			if (!clauseTrue) {
				falseClauses.add(i);
			}
		}
		// Test number of false clauses
		//System.out.println(falseClauses.size());
		// Generates a random integer index from list of false clauses
		int randLit = ThreadLocalRandom.current().nextInt(0, falseClauses.size());
		// Returns index of random false clause for use flips
		return falseClauses.get(randLit);
	}
	
	int fitness(ArrayList<Vector<Integer>> clauses, Vector<Integer> model) {
		int fit = 0;
		
		// Checks each clause to see if it is true
		// If one clause is false returns false
		for (int i = 0; i < clauses.size(); i++) {
			boolean clauseTrue = false;
			// Checks each literal to see if any are true which makes the clause true
			int clauseCount = 0;
			
			while (clauseCount < clauses.get(i).size() && !clauseTrue) {
				// If literal is positive then model value needs to be 1 to be true
				if (clauses.get(i).get(clauseCount) > 0) {
					if (model.get(Math.abs(clauses.get(i).get(clauseCount)) - 1) == 1) {
						clauseTrue = true;
					}
				// If literal is negative then model value needs to be 0 to be true
				} else {
					if (model.get(Math.abs(clauses.get(i).get(clauseCount)) - 1) == 0) {
						clauseTrue = true;
					}
				}
				clauseCount++;
			}
			// If clause is false then return false
			if (clauseTrue) {
				fit++;
			}
		}
		
		// Returns fitness score of model
		return fit;
	}
}
