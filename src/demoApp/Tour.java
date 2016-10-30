package demoApp;

import java.util.ArrayList;

import com.esri.arcgisruntime.geometry.Point;

public class Tour {

  private ArrayList<Point> _checkpoints;
  private String           _name;

  public Tour(String name) {
    _checkpoints = new ArrayList<Point>();
    _name = name;
  }

  public void addCheckpoint(double latitude, double longitude) {
    _checkpoints.add(new Point(longitude, latitude));
  }

  public String getName() {
    return _name;
  }

  public ArrayList<Point> getCheckPoints() {
    return _checkpoints;
  }

}
