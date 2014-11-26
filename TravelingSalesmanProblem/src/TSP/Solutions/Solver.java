package TSP.Solutions;

import java.util.ArrayList;

/**
 *
 * @author twintwox
 */
public interface Solver {
    public void execute() throws Exception;
    public void setDistances(double[][] cities);
    public void printResult();
    public double getBestTourLength();
    public ArrayList<Integer> getBestTour();
    public void setConfiguration(int threads,int ants, double alpha, double beta, double globalEvaporation, int iterations, int repetitions, boolean opt2, boolean opt3);
}
