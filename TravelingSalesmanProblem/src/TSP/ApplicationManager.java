
package TSP;

import TSP.Solutions.SolverFactory;
import TSP.Solutions.Solver;
import TSP.GUI.GUI;


/**
 *
 * @author twintwox
 */
public class ApplicationManager {
    
    private static int      STATE=0;
    public static final int ST_INIT =0;
    public static final int ST_RUNNING=1;
    public static final int ST_STOPPING=2;
    
    public static       Solver  SOLVER;
    public static final GUI     WINDOW   = new GUI();
    public static final CityMap    CITY_MAP = new CityMap();
    
    public static void perform(){ 
        Runnable task = new Runnable() {
            @Override 
            public void run() { 
                 switch(STATE){
                     case ST_INIT:clear();      break;
                     case ST_RUNNING: execute();break;
                     default:                   break;
                 }
            } 
        }; 
        new Thread(task, "ApplicationManager").start();
    }
     
    public static void setState(int st){
        if(st<0||st>2)return;
        STATE=st;
    };
    public static int getState(){
        return STATE;
    }
    
    private static void execute(){ 
        SOLVER = SolverFactory.getSolver(WINDOW.getMethod());
        SOLVER.setConfiguration(WINDOW.getThreads(),WINDOW.getAnts(),WINDOW.getAlpha(),WINDOW.getBeta(),WINDOW.getGlobalEvaporation(),WINDOW.getIterations(),WINDOW.getRepetitions(), WINDOW.getOpt2(),WINDOW.getOpt3());
        SOLVER.setDistances(CITY_MAP.getDistances());
        try{
            double start=System.nanoTime();
            SOLVER.execute();
            double elapsedTime = System.nanoTime() - start;
            elapsedTime= Math.floor(elapsedTime*0.00001)/10000;
            if(STATE!=ST_STOPPING)
                WINDOW.addToHistory(WINDOW.getMethod(),WINDOW.getThreads(),elapsedTime, SOLVER.getBestTourLength(),CITY_MAP.getCities());
        }catch(Exception e){
            System.err.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }
        WINDOW.resetButtons();
        WINDOW.CANVAS[1].repaint();
        STATE=ST_STOPPING;
    }
    
    private static void clear(){
        SOLVER=null;
        CITY_MAP.clearCityMap();
    }
    
    public static boolean executing() {
        return (STATE!=ST_STOPPING);
    }
    
    public static void main(String[] args){
        Thread t = Thread.currentThread();
        t.setName("TSP-Main");
    }
}