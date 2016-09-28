import java.util.ArrayList;
import java.util.Vector;

public class DPLL {
	Vector<Integer> solution;
	
	// Sets solution vector size to number of literals
	public DPLL(int lit) {
		solution = new Vector<Integer>(lit);
		for(int i = 0; i < lit; i++){
			solution.add(0);
		}
	}
	
	// Checks satisfiability, returns true if clauses are satisfiable
	boolean dpllSolver(ArrayList<Vector<Integer>> clauseList){
		boolean sat = false;
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
				sat = true;
				return true;
			}
			else {
				sat = false;
			}
		}
		
		// For every unit clause do unitPropogate
		//for (int i = 0; i < clauseList.size(); i++) {
		//	if (clauseList.get(i).size() == 1){
		//		
		//	}
		//}
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
		
		return false;
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
		// Error check
		if (solution.get(Math.abs(lit) -1) != 0) {
			System.out.println("Error literal value has already been set");
		}
		
		// Sets literal in solution to be true or false depending on value of literal
		if (lit > 0) {
			solution.set(Math.abs(lit) - 1, 1);
		} else {
			solution.set(Math.abs(lit - 1), -1);
		}
		
		for (int i = 0; i < clauses.size(); i++){
			// Checks if clause contains literal lit
			if (clauses.get(i).contains(lit)) {
				// Removes clause if it contains literal lit since the clause is now true
				clauses.remove(i);
			}
			// Checks if clause has opposite of literal lit
			if (clauses.get(i).contains(lit * -1)) {
				// Finds index of opposite of lit in clause and removes it from clause
				int index = clauses.get(i).indexOf(lit * -1);
				clauses.get(i).remove(index);
			}
		}
		
		return clauses;
	}
}
