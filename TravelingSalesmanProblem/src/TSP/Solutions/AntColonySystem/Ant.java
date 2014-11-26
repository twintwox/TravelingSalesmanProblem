package TSP.Solutions.AntColonySystem;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author twintwox
 */
public class Ant{
    
    private AntColonySystemSolver solver;
    private int noNodes;
    private int tourLength;
    private int tour[];
    private boolean visited[];
    
    
    private double[][]  pheromone;
    private double[][]  choiceInfo;
    private double[][] dist;
    private OptimizationTSP opt;
    
    private boolean doOpt2;
    private boolean doOpt3;
    
    
    public Ant(AntColonySystemSolver solver, double [][] c){
        this.solver = solver;
        dist = new double[c.length][];
        for(int i = 0; i < c.length; i++)
            dist[i] = c[i].clone();
        opt = new OptimizationTSP(dist);
    }
    
    public void execute() {
        constructSolution();
        localSearch();
        recalculateTourLength();
        depositPheromone();
        solver.updateBestSoFarTour(tourLength, tour);
    }
    
    private void init(){
      noNodes   = dist.length;
      tourLength= 0;
      visited   = new boolean [noNodes];
      tour      = new int [noNodes+1];
      pheromone = solver.getPheromone();
      choiceInfo= solver.getChoiceInfo();
    }
    
    public void constructSolution(){
        int k,rand;
        int step = 0;
        init();
        Random random = new Random();
        /* Posicion inicial de la horamiga */
        rand = Math.abs(random.nextInt())%noNodes;
        tour[step]=rand;
        visited[rand]=true;
        
        /* Construir solucion */
        while(step < noNodes-1){
            step++;
            decisionRule(step);
        }
        /* Completar recorrido */
        tour[noNodes] = tour[0];
    }
    
    public void decisionRule(int step){
        
        int c = tour[step-1]; // ciudad anterior a este paso.
        double sumProb = 0.0;
        double selectionProbability[] = new double[noNodes];

        int j;
        for(j = 0; j < noNodes; j++){
            if((visited[j]) || (j == c))
                selectionProbability[j] = 0.0;
            else{
                selectionProbability[j] = choiceInfo[c][j];
                sumProb+=selectionProbability[j];
            }

        }
        double prob = Math.random()*sumProb;
        j = 0;
        double p = selectionProbability[j];
        while(p < prob){
            j++;
            p += selectionProbability[j];
        }
        tour[step] = j;
        visited[j] = true;
    }
    
    public void localSearch(){
        /* Procedimientos de búsqueda local */
        doOpt2=true;
        doOpt3=true;
        if(doOpt2) opt.opt2(tour);
        if(doOpt3) opt.opt3(tour);
    }
    
    public void depositPheromone(){
        double delta_tau = 1.0/tourLength;
        int left,right;
        
        synchronized(pheromone){
            for(int i = 0; i < noNodes; i++){
                left = tour[i];
                right = tour[i+1];
                pheromone[left][right]+=delta_tau;
                pheromone[right][left]+=delta_tau;
            }
        }
    }
    
    public int computeTourLength(int tour[]){
        int len = 0;
        for(int i = 0; i < noNodes; i++)
            len+=dist[tour[i]][tour[i+1]];
        return len;
    }
    
    public int getTourLength(){
        return tourLength;
    }
    private void recalculateTourLength(){
        tourLength = computeTourLength(this.getTour());
    }
    public void setNoNodes(int n){
        noNodes = n;
    }
    public int[] getTour(){
        return tour;
    }
    
    public void setCities(double[][] c){
        dist = Arrays.copyOf(c, c.length);
        opt = new OptimizationTSP(dist);
    }
    
    public void setConfiguration(boolean opt2,boolean opt3){
        this.doOpt2=opt2;
        this.doOpt3=opt3;
    }
}
