
package TSP.Solutions.AntColonySystem;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author twintwox
 */
public class AntCluster implements Runnable{

    private ArrayList<Ant> ants;
    private CyclicBarrier barrier;
    private Coordinator coord;
    private boolean finalize = false;
    
    public AntCluster(Coordinator c, CyclicBarrier b) {
        ants = new ArrayList<>();
        barrier=b;
        coord = c;
    }
    public void add(Ant a){
        ants.add(a);
    }
    private void block(){
        try{barrier.await();
        }catch(Exception e) {e.printStackTrace();};
    }
    @Override
    public void run() {
        
        block();
        while(!finalize){    
            for(Ant ant: ants)ant.execute();
            coord.threadFinish();
            block();
        }        
    }
    public void finalizeThread(){
        finalize=true;
    }
}
