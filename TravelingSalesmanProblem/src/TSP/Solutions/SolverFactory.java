package TSP.Solutions;

import TSP.Solutions.AntColonySystem.AntColonySystemSolver;
import TSP.Solutions.BruteForce.*;


/**
 *
 * @author twintwox
 */
public class SolverFactory {
    
    public static Solver getSolver(String str){
        if(str.compareTo("Brute Force")==0)
            return (new BruteForceSolver());
        if(str.compareTo("Ant System")==0)
            return (new AntColonySystemSolver());
        return null;
    };
    
}
