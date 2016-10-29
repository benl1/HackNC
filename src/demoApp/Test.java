package demoApp;

import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Surface;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.SceneView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Test extends Application {

  public void start(Stage e) throws Exception{
    
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane);
    //create a scene
    ArcGISScene AGSscene = new ArcGISScene();

    //add a basemap to the scene
    Basemap basemap = Basemap.createImagery();
    AGSscene.setBasemap(basemap);

    //add a scene service to the scene for viewing buildings
    ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer("http://scene.arcgis.com/arcgis/rest/services/Hosted/Buildings_Brest/SceneServer/layers/0");
    AGSscene.getOperationalLayers().add(sceneLayer);
    
 // create a scene view control and define the scene it displays
    SceneView sceneView = new SceneView();
    sceneView.setArcGISScene(AGSscene);
    
    Camera snowdonCamera = new Camera(35.9049, -79.0469, 1289, 295, 71, 0);
    
 // add an elevation surface from an elevation source
    ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource(
         "http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer");
    Surface surface = new Surface();
    surface.getElevationSources().add(elevationSource);

    // apply the surface to the scene
    AGSscene.setBaseSurface(surface);
    
    sceneView.setViewpointCameraAsync(snowdonCamera);

    // add the scene view to the border pane
    
    borderPane.setCenter(sceneView);
    
    e.setScene(scene);
    e.show();
  }
  
  public static void main(String[] args){
    launch(args);
  }
 
}
