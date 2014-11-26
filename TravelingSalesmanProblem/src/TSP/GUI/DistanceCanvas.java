package TSP.GUI;

import TSP.ApplicationManager;
import TSP.GUI.City;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author twintwox
 */
public class DistanceCanvas extends TSPCanvas{
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ArrayList<City> cities= ApplicationManager.CITY_MAP.getCities();
        for (int i = 0; i < cities.size(); i++) {
            City A= cities.get(i);
            for (int j = i+1; j < cities.size(); j++) {
                City B= cities.get(j);
                double distance=ApplicationManager.CITY_MAP.distanceBetween(A, B); 
                int xpos= (A.x+B.x)/2;
                int ypos= (A.y+B.y)/2;
                g.setColor(Color.BLACK);
                g.drawString(distance+"",xpos,ypos);
            }
        }
    }
}
