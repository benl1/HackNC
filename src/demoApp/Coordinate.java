package demoApp;

public class Coordinate {
  private double _latitude;
  private double _longitude;

  public Coordinate(double lat, double longit) {
    _latitude = lat;
    _longitude = longit;
  }

  public double getLatitude() {
    return _latitude;
  }

  public double getLongitude() {
    return _longitude;
  }

}
