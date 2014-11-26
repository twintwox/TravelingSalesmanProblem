package TSP;
import TSP.GUI.City;
import java.util.ArrayList;

/**
 *
 * @author twintwox
 */
public class CityMap {
    
    private ArrayList<City> cities = new ArrayList<City>();
    
    public void clearCityMap(){
        cities= new ArrayList<>();
    }
    
    public void addCity(City city){
        cities.add(city);
    }
    public ArrayList<City> getCities(){
        return cities;
    }
    
    public void setCities(ArrayList<City> c){
        cities=c;
    }
    
    public double[][] getDistances(){
        int N = cities.size();
        double[][] distances = new double[N][N];
        for (int i = 0; i < N; i++) {
            City A = cities.get(i);
            for (int j = i+1; j < N; j++) {
                City B = cities.get(j);
                distances[i][j]=Math.round(Math.sqrt((Math.pow(A.x-B.x,2)+Math.pow(A.y-B.y,2))));
                distances[j][i]=distances[i][j];
            }
        }
        return distances;
    }
    
    public double distanceBetween(City A, City B){
        return Math.round(Math.sqrt((Math.pow(A.x-B.x,2)+Math.pow(A.y-B.y,2))));  
    };
    
}
