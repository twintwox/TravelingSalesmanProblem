package TSP.Solutions.AntColonySystem;

import TSP.ApplicationManager;
import TSP.Solutions.Solver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 *
 * @author twintwox
 */
public class AntColonySystemSolver implements Solver, Coordinator {
    
    private double[][] dist;
    private int noThreads;
    private int noAnts;
    private int noNodes;
    private double alpha;
    private double beta;
    private double ro; //global Evaporation Rate
    private int iterations;
    private int repetitions;
    private boolean doOpt2;
    private boolean doOpt3;
    ArrayList<AntCluster> clusters = new ArrayList<>();
    ArrayList<Thread> threads = new ArrayList<>();
    
    int threadFinished;
    CyclicBarrier coordBarrier;
    CyclicBarrier threadsBarrier;
    
    private double[][]  pheromone;
    private double[][]  choiceInfo;
    private double[][]  ni_value;
    private int[]   bestTour;
    private double  bestTourLength = Double.MAX_VALUE;
    
    private double computeInitialPheromone(){
        if(bestTourLength==Double.MAX_VALUE)bestTourLength = greedyTour();
        return  ((double)noAnts)/bestTourLength;
    }
    public void initData(){
        pheromone = new double[noNodes][noNodes];
        choiceInfo = new double[noNodes][noNodes];
        double tau0 = computeInitialPheromone();
        
        /*Inicializar feromonas*/
        for(int i = 0; i < noNodes; i++)
            for(int j = i+1; j < noNodes; j++){
                pheromone[i][j] = tau0;
                pheromone[j][i] = tau0;
            }  
        /*Inicializar valor de funcion ni*/
        ni_value = new double[noNodes][noNodes];
        for(int i = 0; i < noNodes; i++)
            for(int j = i+1; j < noNodes; j++){
                if(dist[i][j] > 0){
                    ni_value[i][j] = 1.0/dist[i][j];
                    ni_value[j][i] = ni_value[i][j]; 
                }else {
                    ni_value[i][j] = 1.0/0.0001;
                    ni_value[j][i] = ni_value[i][j];
                }
        }
        this.computeHeuristic();
    }
    
    private double doHeuristic(double pheromone,double ni_value){
        return Math.pow(pheromone,alpha)*Math.pow(ni_value,beta);
    }
    public void computeHeuristic(){
        for(int i = 0; i < noNodes; i++)
            for(int j = i+1; j < noNodes; j++){
                choiceInfo[i][j] = doHeuristic(pheromone[i][j],ni_value[i][j]);
                choiceInfo[j][i] = choiceInfo[i][j];
        }
    }
    
    @Override
    public void execute() throws Exception {
        System.out.println("Ant System Algorithm started...");
        if(dist==null || dist.length<2) throw new Exception("No cities configured");
        
        coordBarrier   = new CyclicBarrier(2);
        threadsBarrier = new CyclicBarrier(noThreads+1);
        /*Distribución de carga*/
        for (int j = 0; j < noThreads; j++)clusters.add(new AntCluster(this,threadsBarrier));
        int cl=0;
        for (int i = 0; i < noAnts; i++) {
            Ant ant = new Ant(this,dist);
            ant.setConfiguration(doOpt2, doOpt3);
            clusters.get(cl).add(ant);
            cl++;
            if(cl>=noThreads)cl=0;
        }

        /*Inicio de algoritmo*/
        int rep=repetitions;
        
        initializeThreads();
        while((rep-->0) && (ApplicationManager.executing())){
            int it=iterations;
            initData();
            while((it-->0) && (ApplicationManager.executing())){         
                globalEvaporation();
                runThreads();
                computeHeuristic();        
            }
        }
        finalizeThreads();
        System.out.println("END");
    }

    private void initializeThreads(){
        threads = new ArrayList<>();
        for(AntCluster ac: clusters){
            Thread t = new Thread(ac);
            t.setName("TSP-Thread_"+t.getId());
            threads.add(t);
            t.start();
        };
    }
    
    private void block(){
        try{coordBarrier.await();
        }catch(Exception e) {e.printStackTrace();};
    }
    private void releaseThreads(){
        try{threadsBarrier.await();
        }catch(Exception e) {e.printStackTrace();};
    }
    private void runThreads(){         
        threadFinished=0;
        releaseThreads();
        block();
    }
    
    @Override
    public synchronized void threadFinish() {
        threadFinished++;
        if(threadFinished==noThreads) block(); //Para liberar al Coord.   
    }
   
    private void finalizeThreads()throws Exception{
        for(AntCluster ac: clusters)ac.finalizeThread();
        releaseThreads();
        for(Thread t: threads)t.join();
    }
    
    public synchronized void updateBestSoFarTour(int tourLength,int[] tour){
        if(tourLength < bestTourLength){
            bestTour= Arrays.copyOf(tour,noNodes+1);
            bestTourLength=tourLength;
        }
    }
    
    public void globalEvaporation(){
        int i,j;
        for(i = 0; i < noNodes; i++)
            for(j = 0; j < noNodes; j++)
                pheromone[i][j] = (1-ro)*pheromone[i][j];       
    } 
    
    
    private double greedyTour(){
        boolean visited[] = new boolean[noNodes];
        bestTour= new int[noNodes+1];
        double min,len=0.0;
        int node, i, j;
        bestTour[0] = 0;
        visited[0] = true;
        
        for(i = 1; i < noNodes; i++){
            min = Double.MAX_VALUE;
            node = -1;
            for(j = 0; j < noNodes; j++)
                if((!visited[j])&&(j!=bestTour[i-1]))
                    if(min > dist[bestTour[i-1]][j]){
                        min = dist[bestTour[i-1]][j];
                        node = j;
                    }
            bestTour[i] = node;
            visited[node] = true;
        }
        bestTour[noNodes] = bestTour[0];
        for(i = 0; i < noNodes; i++) len+=dist[bestTour[i]][bestTour[i+1]];
        return len;
    }
    
    @Override
    public void setDistances(double[][] cities) {
        this.dist=cities;
        noNodes = cities.length;
    }

    @Override
    public void printResult(){
        System.out.print("-Solution Length: "+bestTourLength+" Path: ");
        for (Integer i: bestTour) System.out.print(i+" ");
        System.out.println();
    }

    @Override
    public double getBestTourLength() {
        return bestTourLength;
    }

    @Override
    public ArrayList<Integer> getBestTour() {
        if(bestTour==null)return null;
        ArrayList<Integer> res= new ArrayList<>();
        for(Integer i: bestTour) res.add(i);
        return res;
    }
    
    @Override
    public void setConfiguration(int threads,int ants, double alpha, double beta, double globalEvaporation, int iterations, int repetitions, boolean opt2, boolean opt3) {
       this.noThreads = threads;
       this.noAnts=ants;
       this.alpha = alpha;
       this.beta = beta;
       this.ro = globalEvaporation;//global Evaporation Rate
       this.iterations = iterations;
       this.repetitions = repetitions;
       this.doOpt2=opt2;
       this.doOpt3=opt3;
    }

    public double[][] getPheromone() {
        return pheromone;
    }

    public double[][] getChoiceInfo() {
        return choiceInfo;
    }


}
