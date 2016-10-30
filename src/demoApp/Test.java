package demoApp;

import java.util.HashMap;
import java.util.Map;

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
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol.Style;

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
  

  public void start(Stage e) throws Exception {
    
    
    

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

    camera = new Camera(35.6, -79,1289, 295, 71, 0);
    
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
    Point location = new Point(-79.0, 35.6, 0,SpatialReference.create(4152));
    Point location2 = new Point(35.6, -100.0, 0, SpatialReference.create(4152));
    SimpleMarkerSymbol greenMarker = new SimpleMarkerSymbol(Style.CIRCLE, 0xFF00FF00, 10.0f);
    SimpleMarkerSymbol redMarker = new SimpleMarkerSymbol(Style.CIRCLE, 0xFFFF0000, 10);
    SimpleMarkerSymbol blueMarker = new SimpleMarkerSymbol(Style.CIRCLE, 0xFF0000FF, 10);

    // add the graphics in the different overlays
    Graphic drapedGraphic = new Graphic(location, redMarker);
    Graphic absoluteGraphic = new Graphic(location, blueMarker);
    Graphic relativeGraphic = new Graphic(location2, greenMarker);
    drapedGraphicsOverlay.getGraphics().add(drapedGraphic);
    absoluteGraphicsOverlay.getGraphics().add(absoluteGraphic);
    relativeGraphicsOverlay.getGraphics().add(relativeGraphic);
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

      public void rightThumbMagnitude(double m) {
        rightMagnitude = 5.0 * m;
      }

      public void leftThumbMagnitude(double m) {

        leftMagnitude = .01 / 1600.0 * camera.getLocation().getZ();
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
           	longitudeChange =  -  Math.cos( (90-camera.getHeading())*Math.PI / 180.0 )*leftMagnitude;
           }
        }
        
       
    	

        // System.out.println(latitudeChange);
        // System.out.println(longitudeChange);

        Point p = new Point(camera.getLocation().getX() + longitudeChange, camera.getLocation().getY() + latitudeChange, camera.getLocation().getZ(), SpatialReference.create(4152));
        // System.out.println(p.getX() + "," + p.getY() + ","+ p.getZ());

        camera = camera.moveTo(p);
        // Point l = camera.getLocation();
        // camera = new Camera(l.getX() + latitudeChange, l.getY() +
        // longitudeChange, l.getZ(), camera.getHeading(), camera.getPitch(),
        // camera.getRoll());
        sceneView.setViewpointCameraAsync(camera);
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
