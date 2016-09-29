import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class DPLL {
	// DPLL Constructor
	public DPLL() { 
	}
	
	// Checks satisfiability, returns true if clauses are satisfiable
	boolean dpllSolver(ArrayList<Vector<Integer>> clauseList){
		System.out.println("The beginning clause list is ");
		System.out.println(clauseList);
		boolean unitClause = false;
		
		// Checks if clauses have been found as satisfiable or unsatisfiable
		// Checks if clauseList is empty which means every clause has been satisfied
		if (clauseList.isEmpty()) {
			return true;
		} else {
			// Check if each clause is a unit clause
			int clauseCount = 0;
			do {
				// If clause size is greater than 1 it is not a unit clause
				if (clauseList.get(clauseCount).size() > 1) {
					unitClause = false;
				} else if (clauseList.get(clauseCount).size() == 0) {
					// An empty clause has been found and the solution is not satisfiable
					return false;
				} else {
					unitClause = true;
				}
				clauseCount++;
				// Ends if every unit clause is a unit clause or a non unit clause is found
			} while(clauseCount < clauseList.size() && unitClause);
			
			// If all unit clauses found check for consistent literals
			if (consistentLiterals(clauseList)) {
				// Solution has been found return
				return true;
			}
		}
		
		// For every unit clause do unitPropogate
		int clauseCount = 0;
		do {
			// if unitPropogate has been called then reset clauseCount since the removal of literals may have created new unit clauses
			if (clauseList.get(clauseCount).size() == 1) {
				clauseList = unitPropogate(clauseList,clauseList.get(clauseCount).firstElement());
				clauseCount = 0;
			} else {
				clauseCount++;
			}
		} while(clauseCount < clauseList.size());
		
		// For every pure literal do pureLiteralAssign
		
		// Contains lists of literals to compare and find pure literals
		Vector<Integer> tempLits = new Vector<Integer>();
		Vector<Integer> pureLits = new Vector<Integer>();
		
		for (int i = 0; i < clauseList.size(); i++){
			for (int j = 0; j < clauseList.get(i).size(); j++) {
				// If literal is not in tempLits then add it to the list
				if (!tempLits.contains(clauseList.get(i).get(j))) {
					tempLits.add(clauseList.get(i).get(j));
					// If opposite of literal is not in pure literals then add to pure literals if so then remove from pure literals
					if (pureLits.contains(-1 * clauseList.get(i).get(j))) {
						// Finds index of opposite of lit in clause and removes it from puteLits
						int index = pureLits.indexOf(clauseList.get(i).get(j) * -1);
						pureLits.remove(index);
					} else {
						pureLits.add(clauseList.get(i).get(j));
					}
				}
			}
		}
		
		// Performs pureLiteralAssign for each pure literal
		for (int i = 0; i < pureLits.size(); i++) {
			clauseList = pureLiteralAssign(clauseList,pureLits.get(i));
		}
		
		// Chooses a random number from the list of literals
		// Temporary clauses to hold the unit clause for the literal and the opposite of the literal
		Vector<Integer> randUnitClause1 = new Vector<Integer>(1);
		Vector<Integer> randUnitClause2 = new Vector<Integer>(1);
		System.out.println("Number of literals is " + tempLits.size());
		System.out.println("The changed clause list is ");
		System.out.println(clauseList);

		int randLit = ThreadLocalRandom.current().nextInt(0, tempLits.size());
		randUnitClause1.add(tempLits.get(randLit));
		randUnitClause2.add(-1 * tempLits.get(randLit));
		System.out.println("Chosen literal is " + randUnitClause1);
		
		// A temporary clause list to hold the opposite value for the randomly chosen literal
		ArrayList<Vector<Integer>> clauseList2 = new ArrayList<Vector<Integer>>();
		clauseList2.addAll(clauseList);
		clauseList.add(0,randUnitClause1);
		clauseList2.add(0,randUnitClause2);
		return (dpllSolver(clauseList) || dpllSolver(clauseList2));
	}
	
	// Checks for consistent literals with assumption each clause contains one literal
	boolean consistentLiterals(ArrayList<Vector<Integer>> clauses) {
		// If return false then literals are not consistent
		// Contains seen literals to check for consistent literals
		Vector<Integer> literals = new Vector<Integer>();
		// Checks for each literal 
		for(int i = 0; i < clauses.size(); i++){
			// If no literals have been added then add the literal 
			if (!literals.isEmpty()){
				// Will compare each literal already checked versus current literal
				for (int j = 0; j < literals.size(); j++) {
					// Checks to see if they literals have same absolute value
					if (Math.abs(literals.get(j)) == Math.abs(clauses.get(i).firstElement())) {
						// If literals have same absolute value then check if they have same value if not
						// literals are not consistent
						if (literals.get(j) != clauses.get(i).firstElement()) {
							return false;
						}
					}
				}
			}
			// Adds literals until false is returned or every literal has been checked for consistency
			literals.add(clauses.get(i).firstElement());
		}
		// If no inconsistent literals found then return true
		return true;
	}
	
	ArrayList<Vector<Integer>> unitPropogate(ArrayList<Vector<Integer>> clauses, int lit) {
		// Error check
		if (lit == 0) {
			System.out.println("Error literal value is 0");
		}

		for (int i = 0; i < clauses.size(); i++){
			
			// Checks if clause has opposite of literal lit
			if (clauses.get(i).contains(lit * -1)) {
				// Finds index of opposite of lit in clause and removes it from clause
				int index = clauses.get(i).indexOf(lit * -1);
				clauses.get(i).remove(index);
			}
			
			// Checks if clause contains literal lit
			if (clauses.get(i).contains(lit)) {
				// Removes clause if it contains literal lit since the clause is now true
				clauses.remove(i);
			}
		}
		
		return clauses;
	}
	
	ArrayList<Vector<Integer>> pureLiteralAssign(ArrayList<Vector<Integer>> clauses, int lit) {
		// Searches through all causes for pure literal
		for (int i = 0; i < clauses.size(); i++){
			// Checks if clause contains literal lit
			if (clauses.get(i).contains(lit)) {
				// Removes clause if it contains literal lit since the clause is now true
				clauses.remove(i);
			}
		}
		
		return clauses;
	}
}
