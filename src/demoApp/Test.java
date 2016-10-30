package demoApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
import com.esri.arcgisruntime.symbology.SceneSymbol.AnchorPosition;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSceneSymbol.Style;
//import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol.Style;

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
  private Camera          camera;
  private double          rightMagnitude;
  private double          leftMagnitude;
  private SceneView       sceneView;
  private String          location;
  ComboBox                comboBox;
  Tour                    tour;

  public void start(Stage e) throws Exception {
    
    Tour china = new Tour("Great Wall of China");
    
    

    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane);
    // create a scene
    ArcGISScene AGSscene = new ArcGISScene();

    // add a basemap to the scene
    Basemap basemap = Basemap.createImagery();
    AGSscene.setBasemap(basemap);

    // add a scene service to the scene for viewing buildings
    ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer("http://scene.arcgis.com/arcgis/rest/services/Hosted/Buildings_Brest/SceneServer/layers/0");
    AGSscene.getOperationalLayers().add(sceneLayer);

    // create a scene view control and define the scene it displays
    sceneView = new SceneView();
    sceneView.setArcGISScene(AGSscene);

    camera = new Camera(40.4319, 116.5704, 1289, 45, 71, 0);
    
    //
    GraphicsOverlay drapedGraphicsOverlay = new GraphicsOverlay();
    drapedGraphicsOverlay.getSceneProperties().setSurfacePlacement(SurfacePlacement.DRAPED);
    sceneView.getGraphicsOverlays().add(drapedGraphicsOverlay);

    // create an absolute graphics overlay
    GraphicsOverlay absoluteGraphicsOverlay = new GraphicsOverlay();
    absoluteGraphicsOverlay.getSceneProperties().setSurfacePlacement(SurfacePlacement.ABSOLUTE);
    sceneView.getGraphicsOverlays().add(absoluteGraphicsOverlay);

    // create a relative graphics overlay
    GraphicsOverlay relativeGraphicsOverlay = new GraphicsOverlay();
    relativeGraphicsOverlay.getSceneProperties().setSurfacePlacement(SurfacePlacement.RELATIVE);
    sceneView.getGraphicsOverlays().add(relativeGraphicsOverlay);

    //create a point and markers
    Point location = new Point(116.5704, 40.4319, 0,SpatialReference.create(4152));
    Point location2 = new Point(35.6, -100.0, 0, SpatialReference.create(4152));
    SimpleMarkerSceneSymbol greenMarker = new SimpleMarkerSceneSymbol(SimpleMarkerSceneSymbol.Style.SPHERE, 0xCC880000, 3000,
            3000, 3000, AnchorPosition.CENTER);
    SimpleMarkerSceneSymbol redMarker = new SimpleMarkerSceneSymbol(SimpleMarkerSceneSymbol.Style.SPHERE, 0xCC880000, 3000,
            3000, 3000, AnchorPosition.CENTER);
    SimpleMarkerSceneSymbol blueMarker = new SimpleMarkerSceneSymbol(SimpleMarkerSceneSymbol.Style.SPHERE, 0xCC880000, 3000,
            3000, 3000, AnchorPosition.CENTER);

    // add the graphics in the different overlays
    Graphic drapedGraphic = new Graphic(location, redMarker);
//    Graphic absoluteGraphic = new Graphic(location, blueMarker);
//    Graphic relativeGraphic = new Graphic(location2, greenMarker);
    drapedGraphicsOverlay.getGraphics().add(drapedGraphic);
//    absoluteGraphicsOverlay.getGraphics().add(absoluteGraphic);
//    relativeGraphicsOverlay.getGraphics().add(relativeGraphic);
    //

    // add an elevation surface from an elevation source
    ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource("http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer");
    Surface surface = new Surface();
    surface.getElevationSources().add(elevationSource);

    // apply the surface to the scene
    AGSscene.setBaseSurface(surface);

    sceneView.setViewpointCameraAsync(camera);

    // add the scene view to the border pane

    borderPane.setCenter(sceneView);

    e.setScene(scene);
    e.show();

    XboxController xc = new XboxController();
  
    xc.addXboxControllerListener(new XboxControllerAdapter() {

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

        camera = camera.rotateTo(camera.getHeading() + headingChange, camera.getPitch() + pitchChange, camera.getRoll());

        sceneView.setViewpointCameraAsync(camera);

      }
      
      public void buttonA(boolean p){
        if (p){
          camera = camera.moveForward(500);
          sceneView.setViewpointCameraAsync(camera);
        }
      }

      public void rightThumbMagnitude(double m) {
        rightMagnitude = 5.0 * m;
      }

      public void leftThumbMagnitude(double m) {

        leftMagnitude = .01 / 1600.0 * camera.getLocation().getZ() * m;
      }

      public void leftThumbDirection(double d) {
        double latitudeChange = 0;
        double longitudeChange = 0;
        if (camera.getHeading() < 90 && camera.getHeading() > 0) {
        	if ( d > 180 && d < 360 ) {
           	 // if you are going left, and your heading is in between 0 and 90:
           	latitudeChange =  - Math.sin( (90-camera.getHeading())*Math.PI / 180.0 )*leftMagnitude;
           	longitudeChange =  Math.cos( (90-camera.getHeading())*Math.PI / 180.0 )*leftMagnitude;
           } else  {
           	// if you are going right, and your heading is in between 0 and 90:
           	latitudeChange =   Math.sin( (90-camera.getHeading())*Math.PI / 180.0 )*leftMagnitude;
           	longitudeChange = -   Math.cos( (90-camera.getHeading())*Math.PI / 180.0 )*leftMagnitude;
           }
        }
        
       
//    	System.out.println("Direction: " + d);
//        System.out.println("Heading: " + camera.getHeading());
//         System.out.println("Lat change:" + latitudeChange);
//         System.out.println("Long change: " + longitudeChange);

        Point p = new Point(camera.getLocation().getX() + longitudeChange, camera.getLocation().getY() + latitudeChange, camera.getLocation().getZ(), SpatialReference.create(4152));
        // System.out.println(p.getX() + "," + p.getY() + ","+ p.getZ());

        camera = camera.moveTo(p);
        // Point l = camera.getLocation();
        // camera = new Camera(l.getX() + latitudeChange, l.getY() +
        // longitudeChange, l.getZ(), camera.getHeading(), camera.getPitch(),
        // camera.getRoll());
        sceneView.setViewpointCameraAsync(camera);
      }
      
      public void buttonB(boolean bool) {
    	  if (bool) {
    		  System.out.println("======\nLat: " + camera.getLocation().getX());
    		  System.out.println("Long: " + camera.getLocation().getY());
    		  //https://maps.googleapis.com/maps/api/place/textsearch/xml?location="+camera.getLocation().getY()+","+camera.getLocation().getY()+"&query=closest+restaurant&key=AIzaSyBji1ewhxgDN6jUJyYEYZyQjG3Ws0dpkAY
    		  URL url = null;
    		  
    		  try {
            url = new URL("https://maps.googleapis.com/maps/api/place/textsearch/xml?location="+camera.getLocation().getY()+","+camera.getLocation().getY()+"&query=closest+restaurant&key=AIzaSyBji1ewhxgDN6jUJyYEYZyQjG3Ws0dpkAY");
          } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          Scanner s = null;
          try {
            s = new Scanner(url.openStream());
            while(s.hasNext()){
             // System.out.println(s.next());
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
    		  
    		  
    		  
    		  
          try {
            url = new URL("https://maps.googleapis.com/maps/api/place/radarsearch/json?location="+camera.getLocation().getY()+","+camera.getLocation().getY()+"&radius=5000&type=food&key=AIzaSyBji1ewhxgDN6jUJyYEYZyQjG3Ws0dpkAY");
          } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
    		  Scanner s = null;
          try {
            s = new Scanner(url.openStream());
            while(s.hasNext()){
             // System.out.println(s.next());
            }
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
    		  
    	  }
      }
      
      public void buttonX(boolean bool){
        if(bool) {
          System.out.println("added CheckPoint");
          System.out.println("======\nLat: " + camera.getLocation().getY());
          System.out.println("Long: " + camera.getLocation().getX());
          china.addCheckpoint(camera.getLocation().getY(), camera.getLocation().getX());
        }
        
      }
      public void buttonY(boolean bool){
        ArrayList<Point> p = china.getCheckPoints();
        if(bool){
          if(!p.isEmpty()){
            
            camera = camera.moveTo(p.remove(0));
            sceneView.setViewpointCameraAsync(camera);
          }
         
        }
      }

    });
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
