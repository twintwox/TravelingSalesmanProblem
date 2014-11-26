package TSP.GUI;

import TSP.ApplicationManager;
import TSP.GUI.City;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 *
 * @author twintwox
 */
public class CityCanvas extends TSPCanvas implements MouseListener{
    
    public CityCanvas(){
        super();
        addMouseListener(this);
    }
    
    public  void addCity(int x,int y){
       City city = new City(x,y);
       add(city);
       ApplicationManager.CITY_MAP.addCity(city);
       ApplicationManager.WINDOW.setCities(ApplicationManager.CITY_MAP.getCities().size());
       repaint();
       System.out.println("Added city in x:"+x+" y:"+y);
    }
     
     public  void addCity(City city){
       add(city);
       ApplicationManager.CITY_MAP.addCity(city);
       ApplicationManager.WINDOW.setCities(ApplicationManager.CITY_MAP.getCities().size());
       repaint();
       System.out.println("Added city in x:"+city.x+" y:"+city.y);
    }
     
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Component c: this.getComponents()){
            c.repaint();
        }
    }
    @Override
    public void mousePressed(MouseEvent me) {
        addCity(me.getX(),me.getY());
    }
    @Override
    public void mouseClicked(MouseEvent me) {}
    @Override
    public void mouseReleased(MouseEvent me) {}
    @Override
    public void mouseEntered(MouseEvent me) {}
    @Override
    public void mouseExited(MouseEvent me) {}
}
