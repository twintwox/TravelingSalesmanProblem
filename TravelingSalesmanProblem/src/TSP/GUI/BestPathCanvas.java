package TSP.GUI;

import TSP.ApplicationManager;
import TSP.GUI.City;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author twintwox
 */
public class BestPathCanvas extends TSPCanvas{
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ArrayList<City> cities= ApplicationManager.CITY_MAP.getCities();
        
        ArrayList<Integer> bestPath= new ArrayList<>();
        if(ApplicationManager.SOLVER!=null)
            bestPath=ApplicationManager.SOLVER.getBestTour();

        if(bestPath==null || bestPath.isEmpty())return;
        if(cities==null || cities.isEmpty())return;
        
        City A= cities.get(bestPath.get(0));
        Graphics2D g2= (Graphics2D)g;
        for (int i = 1; i < bestPath.size(); i++) {
            City B= cities.get(bestPath.get(i));
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(A.x, A.y, B.x, B.y);
            double distance=ApplicationManager.CITY_MAP.distanceBetween(A, B); 
            int xpos= (A.x+B.x)/2;
            int ypos= (A.y+B.y)/2;
            g.setColor(Color.RED);
            g.drawString(distance+"",xpos,ypos);
            A=B;
        }
    }
}
