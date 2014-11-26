package TSP.Solutions.BruteForce;

import TSP.ApplicationManager;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author twintwox
 */
public class BruteForceThread implements Runnable{
    
    private int bestTourLength = Integer.MAX_VALUE;
    private ArrayList<Integer> bestTour=new ArrayList<>();

    private double[][] dist;
    private BruteForceSolver solver;
    private int low_bound=0;
    private int top_bound=0;
    private ArrayList<Integer> currentTour;
    private boolean[] visited;
    
    public BruteForceThread(BruteForceSolver s) {
        solver=s;
        dist = solver.getDistances();
        currentTour= new ArrayList<>();
        visited = new boolean[dist.length];
    }
    
    public BruteForceThread distributeCharge(){
        BruteForceThread another = new BruteForceThread(solver);
        if(this.getWorklaod()== 0)return null;
        
        if(this.getWorklaod()==1){
            int node = this.getUniqueNodeInWorkload();
            this.addToInitialPath(node);
            low_bound=1;
            top_bound=dist.length;
        }
        for(Integer i: currentTour)another.addToInitialPath(i);
        
        int distribute = this.getWorklaod()/2;
        int split = low_bound;
        while(distribute>0){
            if(!visited[split])distribute--;
            split++;
        }
        another.setLowBound(split);
        another.setTopBound(top_bound);
        this.setTopBound(split);
        return another;
    };
    
    public void setLowBound(int b){
        low_bound=b;
    }
    public void setTopBound(int b){
        top_bound=b;
    }
    public void addToInitialPath(int node){
        currentTour.add(node);
        visited[node]=true;
    }
    public int getUniqueNodeInWorkload(){
        for(int i=low_bound;i<top_bound;i++)
            if(!visited[i])return i;
        return -1;
    }
    public int getInitLength(){
        return currentTour.size();
    }
    public int getWorklaod(){
        int load=0;
        for(int i=low_bound;i<top_bound;i++)
            if(!visited[i])load++;
        return load;
    }
    
    public void compareToMin(int count,ArrayList path){
        if(count < bestTourLength){
            bestTourLength=count;
            bestTour= new ArrayList<>(path);
        }     
    }  
    
    public double getSolutionLength() {
        return bestTourLength;
    }

    public ArrayList<Integer> getSolutionPath() {
        return bestTour;
    }    
    
    @Override
    public void run() {
        if((low_bound>top_bound)|(low_bound<0)|(low_bound>dist.length)|(top_bound>dist.length)){
            System.out.println("Thread cant run, bad bounds configuration");
            return;
        };
        
        /*Calcular cuenta inicial*/
        int count=0;
        for (int i = 1; i < currentTour.size(); i++) {
            count+=dist[currentTour.get(i-1)][currentTour.get(i)];
        }
        
        int size=currentTour.size();
        if(size==dist.length){
            calculateMin(count,currentTour.get(size-1));
        }else{
        //System.out.println(count);
        /* Cada thread calcula las permutaciones entre low_bound y top_bound.*/
            for (int i = low_bound; i < top_bound; i++) {
                if(!visited[i]){
                    visited[i]=true;
                    currentTour.add(i);
                    count+=dist[currentTour.get(size-1)][i];
                    calculateMin(count,i);
                    count-=dist[currentTour.get(size-1)][i];
                    currentTour.remove(currentTour.size()-1);
                    visited[i]=false;
                }
            }
        }
        if(ApplicationManager.executing())
        solver.updateBestSoFarTour(bestTourLength, bestTour);
    }
    
    public void calculateMin(int count, int prev){
        if(!ApplicationManager.executing())return;
        if(currentTour.size()==dist.length){
            currentTour.add(0);
            count+=dist[prev][0];
            compareToMin(count, currentTour);
            currentTour.remove(currentTour.size()-1);
            count-=dist[prev][0];
            return;
        }
        for (int i = 0; i < dist.length; i++) {
            if(!visited[i]){
                visited[i]=true;
                currentTour.add(i);
                count+=dist[prev][i];
                calculateMin(count,i);        
                count-=dist[prev][i];
                currentTour.remove(currentTour.size()-1);
                visited[i]=false;
            }
        }
    };

    public String initPath() {
        String str="";
        for(Integer i: currentTour){
            str+="-"+i;
        }
        return str;
    }

    public String bounds() {
        return low_bound+" - "+top_bound;
    }
}
