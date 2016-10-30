package demoApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Surface;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties.SurfacePlacement;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.symbology.SceneSymbol.AnchorPosition;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol.Style;
// import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol.Style;

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application {
  private Camera    camera;
  private double    rightMagnitude;
  private double    leftMagnitude;
  private SceneView sceneView;
  private String    location;
  ComboBox          comboBox;
  Tour              tour;
  ArrayList<Camera> tourLocations;
  int               nextTourIndex;
  Graphic           goalSphere;
  GraphicsOverlay   drapedGraphicsOverlay;
  Point             sphereLocation;

  public void start(Stage e) throws Exception {

    Tour china = new Tour("Great Wall of China");
    nextTourIndex = 0;
    tourLocations = new ArrayList<>();

    // Yosemite
    tourLocations.add(new Camera(37.74169172721636, -119.58455716838428,
        2451.021166132763, 245.51413277748176, 75.47528817260508, 0));

    // Chilean Fjords
    tourLocations.add(new Camera(-48.902825847092, -74.23081887123965, 2610.716689112596,
        86.96069772472258, 78.6124019890956, 0));

    // Blue Ridge Parkway
    tourLocations.add(new Camera(36.364376, -81.30551700000001, 1451.0211661318317,
        199.14251186577462, 77.2356961105611, 0));

    // Mount Everest
    tourLocations
        .add(new Camera(27.487289572976692, 86.88540486257297, 11703, 12.10, 71.38, 0));
    // Iceland
    tourLocations.add(new Camera(66.0358131056401, -22.92559929182217, 500.0,
        237.2227790559418, 79.9795604661635, 0));

    // Alaska Glacier
    tourLocations.add(new Camera(59.081784282331206, -136.83352998205436,
        6828.150498895906, 255.83535027285143, 69.95918086930413, 0));

    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane);
    // create a scene
    ArcGISScene AGSscene = new ArcGISScene();

    // add a basemap to the scene
    Basemap basemap = Basemap.createImagery();
    AGSscene.setBasemap(basemap);

    // add a scene service to the scene for viewing buildings
    ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer(
        "http://scene.arcgis.com/arcgis/rest/services/Hosted/Buildings_Brest/SceneServer/layers/0");
    AGSscene.getOperationalLayers().add(sceneLayer);

    // create a scene view control and define the scene it displays
    sceneView = new SceneView();
    sceneView.setArcGISScene(AGSscene);

    // camera = new Camera(40.4319, 116.5704, 1289, 45, 71, 0);
    // camera = new Camera(36.1128, -113.9961, 1289, 45, 71, 0);
    // Stone mountatain state park
    // camera = new Camera(36.3873, -81.0273, 1289, 45, 71, 0);
    // Mount Everest
    camera = tourLocations.get(nextTourIndex++);

    //
    drapedGraphicsOverlay = new GraphicsOverlay();
    drapedGraphicsOverlay.getSceneProperties()
        .setSurfacePlacement(SurfacePlacement.DRAPED);
    sceneView.getGraphicsOverlays().add(drapedGraphicsOverlay);

    // create an absolute graphics overlay
    GraphicsOverlay absoluteGraphicsOverlay = new GraphicsOverlay();
    absoluteGraphicsOverlay.getSceneProperties()
        .setSurfacePlacement(SurfacePlacement.ABSOLUTE);
    sceneView.getGraphicsOverlays().add(absoluteGraphicsOverlay);

    // create a relative graphics overlay
    GraphicsOverlay relativeGraphicsOverlay = new GraphicsOverlay();
    relativeGraphicsOverlay.getSceneProperties()
        .setSurfacePlacement(SurfacePlacement.RELATIVE);
    sceneView.getGraphicsOverlays().add(relativeGraphicsOverlay);

    SimpleMarkerSceneSymbol blueMarker = new SimpleMarkerSceneSymbol(
        SimpleMarkerSceneSymbol.Style.SPHERE, 0xAA0000FF, 3000, 3000, 3000,
        AnchorPosition.CENTER);

    sphereLocation = camera.moveForward(50000).getLocation();

    // add the graphics in the different overlays
    goalSphere = new Graphic(sphereLocation, blueMarker);
    // Graphic absoluteGraphic = new Graphic(location, blueMarker);
    // Graphic relativeGraphic = new Graphic(location2, greenMarker);
    drapedGraphicsOverlay.getGraphics().add(goalSphere);
    // absoluteGraphicsOverlay.getGraphics().add(absoluteGraphic);
    // relativeGraphicsOverlay.getGraphics().add(relativeGraphic);

    // add an elevation surface from an elevation source
    ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource(
        "http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer");
    Surface surface = new Surface();
    surface.getElevationSources().add(elevationSource);

    // apply the surface to the scene
    AGSscene.setBaseSurface(surface);

    sceneView.setViewpointCameraAsync(camera);

    // add the scene view to the border pane

    borderPane.setCenter(sceneView);

    e.setScene(scene);
    e.show();
    e.setFullScreen(true);

    XboxController xc = new XboxController();

    xc.addXboxControllerListener(new XboxControllerAdapter() {

      public void start(boolean pressed) {
        if (pressed) {
          if (nextTourIndex < tourLocations.size()) {
            camera = tourLocations.get(nextTourIndex);
            nextTourIndex += 1;
            sceneView.setViewpointCameraAsync(camera);
          } else {
            camera = tourLocations.get(0);
            nextTourIndex = 0;
            sceneView.setViewpointCameraAsync(camera);
          }

          Random javaRandom = new Random();
          // create a relative graphics overlay
          GraphicsOverlay relativeGraphicsOverlay = new GraphicsOverlay();
          relativeGraphicsOverlay.getSceneProperties()
              .setSurfacePlacement(SurfacePlacement.RELATIVE);
          sceneView.getGraphicsOverlays().add(relativeGraphicsOverlay);

          SimpleMarkerSceneSymbol blueMarker = new SimpleMarkerSceneSymbol(
              SimpleMarkerSceneSymbol.Style.SPHERE, 0xAA0000FF, 3000, 3000, 3000,
              AnchorPosition.CENTER);

          Point sphereInitial = camera.moveForward(40000 + javaRandom.nextInt(15000))
              .getLocation();

          sphereLocation = new Point(sphereInitial.getX() + javaRandom.nextDouble() - .5,
              sphereInitial.getY() + javaRandom.nextDouble() - .5, sphereInitial.getZ(),
              sphereInitial.getSpatialReference());

          // add the graphics in the different overlays
          goalSphere = new Graphic(sphereLocation, blueMarker);
          // Graphic absoluteGraphic = new Graphic(location, blueMarker);
          // Graphic relativeGraphic = new Graphic(location2, greenMarker);
          drapedGraphicsOverlay.getGraphics().add(goalSphere);
        }
      }

      public void rightThumbDirection(double d) {
        double headingChange = 0, pitchChange = 0;
        // if (rightMagnitude <= .0001)
        // sceneView.setViewpointCameraAsync(snowdonCamera).cancel(true);
        // TODO: change hard coded 5.0 to some value extrapolated from magnitude
        if (d >= 0 && d <= 90) {
          headingChange = (0 + d) * (rightMagnitude / 90.0);
          pitchChange = (90 - d) * (rightMagnitude / 90.0);
        } else if (d > 90 && d <= 180) {
          d -= 90;
          headingChange = (0 + d) * (rightMagnitude / 90.0);
          pitchChange = (90 - d) * (-rightMagnitude / 90.0);
        } else if (d > 180 && d <= 270) {
          d -= 180;
          headingChange = (0 + d) * (-rightMagnitude / 90.0);
          pitchChange = (90 - d) * (-rightMagnitude / 90.0);
        } else {
          d -= 270;
          headingChange = (0 + d) * (-rightMagnitude / 90.0);
          pitchChange = (90 - d) * (rightMagnitude / 90.0);
        }

        camera = camera.rotateTo(camera.getHeading() + headingChange,
            camera.getPitch() + pitchChange, camera.getRoll());

        sceneView.setViewpointCameraAsync(camera);

      }

      public void rightTrigger(double v) {
        if (v > 0) {
          System.out.println(v);
          double currentAlt = v * camera.getLocation().getZ();
          Point potentialLocation = camera.moveForward(currentAlt).getLocation();
          if (potentialLocation.getZ() < 1000) {
            potentialLocation = new Point(potentialLocation.getX(),
                potentialLocation.getY(), 500, SpatialReference.create(4152));
          }
          // if potentialAltitude
          camera = camera.moveTo(potentialLocation);
          // System.out.println(camera.getLocation().getZ());
          double distanceToSphere = distance(camera.getLocation(), sphereLocation);
          System.out.println("Distance: " + distanceToSphere);
          if (distanceToSphere < 0.023) {
            if (nextTourIndex < tourLocations.size()) {
              camera = tourLocations.get(nextTourIndex);
              nextTourIndex += 1;
              sceneView.setViewpointCameraAsync(camera);
            } else {
              camera = tourLocations.get(0);
              nextTourIndex = 0;
              sceneView.setViewpointCameraAsync(camera);
            }

            Random javaRandom = new Random();
            // create a relative graphics overlay
            GraphicsOverlay relativeGraphicsOverlay = new GraphicsOverlay();
            relativeGraphicsOverlay.getSceneProperties()
                .setSurfacePlacement(SurfacePlacement.RELATIVE);
            sceneView.getGraphicsOverlays().add(relativeGraphicsOverlay);

            SimpleMarkerSceneSymbol blueMarker = new SimpleMarkerSceneSymbol(
                SimpleMarkerSceneSymbol.Style.SPHERE, 0xAA0000FF, 3000, 3000, 3000,
                AnchorPosition.CENTER);

            Point sphereInitial = camera.moveForward(40000 + javaRandom.nextInt(15000))
                .getLocation();

            sphereLocation = new Point(
                sphereInitial.getX() + javaRandom.nextDouble() - .5,
                sphereInitial.getY() + javaRandom.nextDouble() - .5, sphereInitial.getZ(),
                sphereInitial.getSpatialReference());

            // add the graphics in the different overlays
            goalSphere = new Graphic(sphereLocation, blueMarker);
            // Graphic absoluteGraphic = new Graphic(location, blueMarker);
            // Graphic relativeGraphic = new Graphic(location2, greenMarker);
            drapedGraphicsOverlay.getGraphics().add(goalSphere);
          }
          sceneView.setViewpointCameraAsync(camera);
        }
      }

      public void leftTrigger(double v) {
        if (v > 0) {
          System.out.println(v);
          double currentAlt = v * camera.getLocation().getZ();
          Point potentialLocation = camera.moveForward(-currentAlt).getLocation();
          if (potentialLocation.getZ() < 1000) {
            potentialLocation = new Point(potentialLocation.getX(),
                potentialLocation.getY(), 500, SpatialReference.create(4152));
          }
          // if potentialAltitude
          camera = camera.moveTo(potentialLocation);
          System.out.println(camera.getLocation().getZ());
          sceneView.setViewpointCameraAsync(camera);
        }
      }

      public void leftShoulder(boolean pressed) {
        if (pressed) {
          Point location = new Point(camera.getLocation().getX(),
              camera.getLocation().getY(), camera.getLocation().getZ() + 500,
              SpatialReference.create(4152));
          camera = camera.moveTo(location);
          sceneView.setViewpointCameraAsync(camera);
        }
      }

      public void rightShoulder(boolean pressed) {
        if (pressed && camera.getLocation().getZ() > 1500) {
          Point location = new Point(camera.getLocation().getX(),
              camera.getLocation().getY(), camera.getLocation().getZ() - 500,
              SpatialReference.create(4152));
          camera = camera.moveTo(location);
          sceneView.setViewpointCameraAsync(camera);
        }
      }

      public void rightThumbMagnitude(double m) {
        rightMagnitude = 5.0 * m;
      }

      public void leftThumbMagnitude(double m) {

        leftMagnitude = .01 / 1600.0 * camera.getLocation().getZ() * m;
      }

      // public void leftThumbDirection(double d) {
      // double latitudeChange = 0;
      // double longitudeChange = 0;

      // System.out.println("Direction: " + d);
      // System.out.println("Heading: " + camera.getHeading());
      // System.out.println("Lat change:" + latitudeChange);
      // System.out.println("Long change: " + longitudeChange);

      // Point p = new Point(camera.getLocation().getX() + longitudeChange,
      // camera.getLocation().getY() + latitudeChange,
      // camera.getLocation().getZ(), SpatialReference.create(4152));
      // // System.out.println(p.getX() + "," + p.getY() + ","+ p.getZ());
      //
      // camera = camera.moveTo(p);
      // // Point l = camera.getLocation();
      // // camera = new Camera(l.getX() + latitudeChange, l.getY() +
      // // longitudeChange, l.getZ(), camera.getHeading(), camera.getPitch(),
      // // camera.getRoll());
      // sceneView.setViewpointCameraAsync(camera);
      // }
      public void buttonB(boolean bool) {
        if (bool) {
          double latitude = camera.getLocation().getY();
          double longitude = camera.getLocation().getX();

          URL url = null;

          try {
            url = new URL(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + latitude + "," + longitude
                    + "&radius=50000&key=AIzaSyBji1ewhxgDN6jUJyYEYZyQjG3Ws0dpkAY");
          } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          Scanner s = null;

          String temp = "";
          HashMap<String, Point> places = new HashMap();

          try {
            s = new Scanner(url.openStream());

            while (s.hasNext()) {
              temp = s.next();

              if (temp.contains("location") && s.next().equals(":")) {
                s.next();
                s.next();
                s.next();
                String lat = s.next();
                lat = lat.substring(0, lat.length() - 1);
                System.out.println(lat);
                s.next();
                s.next();
                String longit = s.next();

                System.out.println(longit);
                String name = "";
                if (temp.contains("name")) {
                  name = s.next();
                  while (!name.contains(",")) {
                    name += s.next() + " ";
                  }
                }

                // System.out.println(name.substring(2,name.length()-3));
                System.out.println("======Nearest Places======");
                double latitPlace = Double.parseDouble(lat);
                double longitPlace = Double.parseDouble(longit);
                
                //THIS LINE PRINTS OUT THE PLACES TO THE CONSOLE
                //CONSOLEPRINTPLACE IS THE PLACE FROM THE QUERY
                String consolePrintPlace = name.substring(2, name.length() - 3);
                System.out.println(consolePrintPlace);

                places.put(name.substring(2, name.length() - 3),
                    new Point(latitPlace, longitPlace));
                if (places.size() == 5) {
                  break;
                }

              }

            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
      // public void buttonB(boolean bool) {
      // if (bool) {
      // double latitude = camera.getLocation().getY();
      // double longitude = camera.getLocation().getX();
      //
      // URL url = null;
      //
      // try {
      // url = new
      // URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=50000&key=AIzaSyBji1ewhxgDN6jUJyYEYZyQjG3Ws0dpkAY");
      // } catch (MalformedURLException e1) {
      // // TODO Auto-generated catch block
      // e1.printStackTrace();
      // }
      // Scanner s = null;
      //
      // String temp = "";
      // ArrayList<String> restaurants = new ArrayList();
      // try {
      // s = new Scanner(url.openStream());
      // while(s.hasNext()){
      // temp = s.next();
      // if(temp.contains("name")){
      // String name = s.next();
      // while(!name.contains(",")){
      // name+=s.next()+" ";
      // }
      //
      //
      // restaurants.add(name.substring(2,name.length()-3));
      // System.out.println("======Nearest Restaurants======");
      // System.out.println(name.substring(2,name.length()-3));
      // if (restaurants.size()==5){
      //
      // break;
      // }
      //
      //
      // }
      //
      // }
      // } catch (IOException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }
      // }
      // }

      // tourLocations.add(new Camera(
      // 40.38722459849128,
      // 116.57624628608481,
      // 10232.386429083534,
      // 29.106149428982615,
      // 62.231688208195884, 0));

      public void buttonX(boolean bool) {
        if (bool) {
          System.out.println("=========\ntourLocations.add(new Camera(");
          System.out.println(camera.getLocation().getY() + ",");
          System.out.println(camera.getLocation().getX() + ",");
          System.out.println(camera.getLocation().getZ() + ",");
          System.out.println(camera.getHeading() + ",");
          System.out.println(camera.getPitch() + ", 0));");
          // china.addCheckpoint(camera.getLocation().getY(),
          // camera.getLocation().getX());
        }

      }

      public void buttonY(boolean bool) {
        ArrayList<Point> p = china.getCheckPoints();
        if (bool) {
          if (!p.isEmpty()) {

            camera = camera.moveTo(p.remove(0));
            sceneView.setViewpointCameraAsync(camera);
          }

        }
      }

    });
  }

  public static double distance(Point a, Point b) {

    double termX = Math.pow(a.getX() - b.getX(), 2);
    double termY = Math.pow(a.getY() - b.getY(), 2);

    return Math.sqrt(termX + termY);
  }

  @Override
  public void stop() throws Exception {
    sceneView.dispose();
    System.exit(0);
  }

  public static void main(String[] args) {
    launch(args);
  }

}
