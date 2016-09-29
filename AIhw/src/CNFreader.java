import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

// This will read the CNF file and output an array of clauses
public class CNFreader {

	// CNF file path
	private String filePath;
	// List of clauses
	private ArrayList<Vector<Integer>> clauseList = new ArrayList<Vector<Integer>>();
	
	public CNFreader(String path) {
		filePath = path;
	}
	
	public static void readLines(CNFreader cnf) throws IOException{
		// Open the file for reading
		FileInputStream fstream = new FileInputStream(cnf.filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Will hold the clause for the given line
			Vector<Integer> tempClause = new Vector<Integer>(3);
			// Print the content on the console
			//System.out.println (strLine);
			// Splits the string to separate the literals in each clause
			String[] temp = strLine.split("\\s+");
			// Only puts literals into vector if line is not a comment line or initial line
			if (!temp[0].equals("c") && !temp[0].equals("p")) {
				// Places literals into vector clause with negative numbers representing !literal 
				for (int i = 0; i < temp.length; i++) {
					int num = Integer.parseInt(temp[i]);
					if (num != 0){
						tempClause.addElement(num);
					}
				}
			}
			if (tempClause.size() > 0){
				//Adds clause to list of clauses
				cnf.clauseList.add(tempClause);
			}
		}
		//Close the input stream
		br.close();
	}
	
	public static void main(String[] args) throws IOException{
		//String fileFolder = "/Users/jasonsands/Desktop/satProgram/SATinstances/easy/";
		String fileFolder = "../SATinstances/easy/";
		fileFolder += "3.cnf";
		CNFreader read1 = new CNFreader(fileFolder);
		readLines(read1);
		//System.out.println(read1.clauseList);
		//System.out.println(read1.lit);
		
		DPLL dpll = new DPLL();
		System.out.println(dpll.dpllSolver(read1.clauseList));
		//System.out.println(dpll.solution);
	}

}
