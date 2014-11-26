package TSP.Solutions.BruteForce;

import TSP.Solutions.Solver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author twintwox
 */
public class BruteForceSolver implements Solver{
    
    private int        noThreads=1;
    private int        bestTourLength = Integer.MAX_VALUE;
    private double[][] dist = null;
    private ArrayList<Integer> bestTour = new ArrayList<>();
    
    private PriorityQueue<BruteForceThread> bfs;
    private ArrayList<Thread>  threads  = new ArrayList<>();
        
    
    public synchronized void updateBestSoFarTour(int count,ArrayList path){
        if(count < bestTourLength){
            bestTourLength=count;
            bestTour= new ArrayList<>(path);
        }     
    }
    
    public double[][] getDistances(){
        return dist;
    }
    
    @Override
    public void execute() throws Exception{
        System.out.println("Brute force Algorithm started...");
        if(dist==null || dist.length<2){
            throw new Exception("No dist configured");
        }
        if(dist.length==1)return;
        if(noThreads<1)return;
        
        bfs = new PriorityQueue(noThreads, new Comparator<BruteForceThread>(){
            @Override
            public int compare(BruteForceThread t, BruteForceThread t1) {
                if(t.getInitLength() > t1.getInitLength())return  1;
                if(t.getInitLength() < t1.getInitLength())return -1;       
                if( t.getWorklaod() < t1.getWorklaod()) return 1;
                return -1;
            }
        });
        
        /*Distribucion de carga*/
        chargeDistribution();
        
        for(Thread t:threads){
            t.setName("TSP-Thread_"+t.getId());
            t.start();
        };
        for(Thread t: threads)t.join();
        
    }
    
    public void chargeDistribution(){
        BruteForceThread bf = new BruteForceThread(this);
        bf.addToInitialPath(0);
        bf.setLowBound(1);
        bf.setTopBound(dist.length);
        bfs.add(bf);
        threads.add(new Thread(bf));
        for (int i = 1; i < noThreads; i++) {
            bf = bfs.poll();
            BruteForceThread bf2 = bf.distributeCharge();
            bfs.add(bf);
            if(bf2!=null){
                bfs.add(bf2);
                threads.add(new Thread(bf2));
            }
        }
        
        int i=0;
        while(!bfs.isEmpty()){
            bf = bfs.poll();
            System.out.println("Nodo "+i+"int("+bf.initPath()+")-"+bf.bounds()+": "+bf.getWorklaod());
            i++;
        }
    }
    
    @Override
    public void printResult(){
        System.out.print("-Solution Length: "+bestTourLength+" Path: ");
        for (Integer i: bestTour)
            System.out.print(i+" ");
        System.out.println();
    }

    @Override
    public double getBestTourLength() {
        return bestTourLength;
    }

    @Override
    public ArrayList<Integer> getBestTour() {
        return bestTour;
    }
    
    @Override
    public void setDistances(double[][] c) {
        dist=c;
    }

    @Override
    public void setConfiguration(int threads,int ants, double alpha, double beta, double globalEvaporation, int iterations, int repetitions, boolean opt2, boolean opt3) {
        noThreads = threads;
    }
}
