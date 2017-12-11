/*
 * Copyright 2017 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.esri.samples.imagelayers.map_image_layer_sublayer_visibility;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.SublayerList;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MapImageLayerSublayerVisibilitySample extends Application {

  private MapView mapView;

  // World Topo Map Service URL
  private static final String WORLD_CITIES_SERVICE =
      "http://sampleserver6.arcgisonline.com/arcgis/rest/services/SampleWorldCities/MapServer";

  @Override
  public void start(Stage stage) throws Exception {

    try {
      // create a border pane and application scene
      StackPane stackPane = new StackPane();
      Scene scene = new Scene(stackPane);
      scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

      // size the stage and add a title
      stage.setTitle("Map Image Layer Sublayer Visibility Sample");
      stage.setWidth(800);
      stage.setHeight(700);
      stage.setScene(scene);
      stage.show();

      // create a control panel
      VBox vBoxControl = new VBox(6);
      vBoxControl.setMaxSize(180, 130);
      vBoxControl.getStyleClass().add("panel-region");

      // create checkboxes for each sublayer
      CheckBox citiesBox = new CheckBox("Cities");
      CheckBox continentsBox = new CheckBox("Continents");
      CheckBox worldBox = new CheckBox("World");

      vBoxControl.getChildren().addAll(citiesBox, continentsBox, worldBox);
      vBoxControl.getChildren().forEach(c -> ((CheckBox) c).setSelected(true));

      // create a ArcGISMap with the a BasemapTyppe Topographic
      ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 48.354406, -99.998267, 2);

      // create a Image Layer with dynamically generated ArcGISMap images
      ArcGISMapImageLayer imageLayer = new ArcGISMapImageLayer(WORLD_CITIES_SERVICE);
      imageLayer.setOpacity(0.7f);

      // add world cities layers as ArcGISMap operational layer
      map.getOperationalLayers().add(imageLayer);

      // set the ArcGISMap to be displayed in this view
      mapView = new MapView();
      mapView.setMap(map);

      // get the layers from the ArcGISMap image layer
      SublayerList layers = imageLayer.getSublayers();

      // handle sub layer selection
      citiesBox.selectedProperty().addListener(e -> layers.get(0).setVisible(citiesBox.isSelected()));
      continentsBox.selectedProperty().addListener(e -> layers.get(1).setVisible(continentsBox.isSelected()));
      worldBox.selectedProperty().addListener(e -> layers.get(2).setVisible(worldBox.isSelected()));

      // add the MapView and checkboxes
      stackPane.getChildren().addAll(mapView, vBoxControl);
      StackPane.setAlignment(vBoxControl, Pos.TOP_LEFT);
      StackPane.setMargin(vBoxControl, new Insets(10, 0, 0, 10));

    } catch (Exception e) {
      // on any error, display the stack trace.
      e.printStackTrace();
    }
  }

  @Override
  public void stop() throws Exception {

    // releases resources when the application closes
    if (mapView != null) {
      mapView.dispose();
    }
  }

  /**
   * Starting point of this application.
   * 
   * @param args arguments to this application.
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}
