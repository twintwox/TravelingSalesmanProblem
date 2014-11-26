package TSP.GUI;

import TSP.ApplicationManager;
import TSP.GUI.City;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author twintwox
 */ 
public class TSPCanvas extends JPanel {
    
    public TSPCanvas(){
        setLayout(null);
        setBackground(new Color(0,0,0,125));
        setOpaque(false);
    }    
    
    public void paintLine(City A, City B, Color color){
        Graphics2D g2= (Graphics2D) this.getGraphics();
        g2.setStroke(new BasicStroke(1));
        g2.setColor(color);     
        g2.drawLine(A.x, A.y, B.x, B.y);
    }
    
    public void paintPath(int A, int B, Color color){
        City cA =ApplicationManager.CITY_MAP.getCities().get(A);
        City cB =ApplicationManager.CITY_MAP.getCities().get(B);
        paintLine(cA, cB, color);
    }
    
    public void paintPath(ArrayList<Integer> path,Color color){
        if(path.size()==0)return;
        ArrayList<City> cts= ApplicationManager.CITY_MAP.getCities();
        int prev= path.get(0);
        for (int i = 1; i < path.size(); i++) {
            paintLine(cts.get(prev), cts.get(path.get(i)), color);
            prev=path.get(i);
        }
    }
    
    
}
