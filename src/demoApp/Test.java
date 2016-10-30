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
import com.esri.arcgisruntime.mapping.view.SceneView;

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
  Map<String, Coordinate> places;

  public void start(Stage e) throws Exception {
    places = new HashMap<String, Coordinate>();
    places.put("Mt. Everest", new Coordinate(27.9878, 86.9250));
    places.put("Great Wall of China", new Coordinate(40.4319, 116.5704));
    places.put("Yosemite", new Coordinate(37.8651, -119.5383));
    showStartStage();
    shoMapStage(e);

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

    camera = new Camera(places.get(location).getLatitude(), places.get(location).getLongitude(), 1289, 295, 71, 0);

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
        if (d >= 0 && d <= 90) {
          latitudeChange = (0 + d) * (leftMagnitude / 90.0);
          longitudeChange = (90 - d) * (leftMagnitude / 90.0);
        } else if (d > 90 && d <= 180) {
          d -= 90;
          latitudeChange = (0 + d) * (leftMagnitude / 90.0);
          longitudeChange = (90 - d) * (-leftMagnitude / 90.0);
        } else if (d > 180 && d <= 270) {
          d -= 180;
          latitudeChange = (0 + d) * (-leftMagnitude / 90.0);
          longitudeChange = (90 - d) * (-leftMagnitude / 90.0);
        } else {
          d -= 270;
          latitudeChange = (0 + d) * (-leftMagnitude / 90.0);
          longitudeChange = (90 - d) * (leftMagnitude / 90.0);
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

  public void showStartStage() {
    Stage startStage = new Stage();
    Group g = new Group();
    Scene scene = new Scene(g);
    Button b = new Button();
    b.setText("Go!");
    b.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        startStage.close();
      }
  });
    });

  ObservableList<String> options = FXCollections.observableArrayList("Mt. Everest", "Great Wall of China",
      "Yosemite");comboBox=new ComboBox(options);comboBox.getSelectionModel().selectedItemProperty().addListener((o,old,n)->
  {
    location = (String) n;
  };g.getChildren().add(comboBox);g.getChildren().add(b);startStage.setScene(scene);startStage.show(););
}

}
