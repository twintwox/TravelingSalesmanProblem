package TSP.GUI;

import TSP.ApplicationManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author twintwox
 */
public class City extends JPanel{
    private static int imgSize = 30;
    private static int fontSize = 15;
    private Image img;
    
    private int number;
    public int x;
    public int y;
     
    public City(int xp,int yp){
        try{
        img = ImageIO.read(this.getClass().getResource("/city.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        x=xp;
        y=yp;
        setBounds(x-(imgSize/2), y-(imgSize/2),imgSize+10,imgSize+10);
        setLayout(null);
        setBackground(new Color(0,0,0,125));
        setOpaque(false);
        number= ApplicationManager.CITY_MAP.getCities().size();
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        g.setColor(Color.RED);
        Boolean success= g.drawImage(img,0,0,imgSize,imgSize, null);
        if(success)g.drawString(number+"",imgSize/2,imgSize/2);
    }
    
    public boolean paint(){
        Graphics2D g2= (Graphics2D) this.getGraphics();
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        g2.setColor(Color.RED);
        Boolean success= g2.drawImage(img,0,0,imgSize,imgSize, null);
        if(success)g2.drawString(number+"",0,0);
        return success;
    }
    public int getImgSize(){
        return imgSize;
    };
}
