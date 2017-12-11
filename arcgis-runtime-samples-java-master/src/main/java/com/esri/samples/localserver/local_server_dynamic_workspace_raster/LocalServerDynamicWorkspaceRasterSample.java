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

package com.esri.samples.localserver.local_server_dynamic_workspace_raster;

import java.io.File;
import java.util.Arrays;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.RasterSublayerSource;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.localserver.DynamicWorkspace;
import com.esri.arcgisruntime.localserver.LocalMapService;
import com.esri.arcgisruntime.localserver.LocalServer;
import com.esri.arcgisruntime.localserver.LocalServerStatus;
import com.esri.arcgisruntime.localserver.RasterWorkspace;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

public class LocalServerDynamicWorkspaceRasterSample extends Application {

  private MapView mapView;
  private static LocalServer server;

  @Override
  public void start(Stage stage) throws Exception {

    try {
      // create stack pane and application scene
      StackPane stackPane = new StackPane();
      Scene scene = new Scene(stackPane);
      scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

      // set title, size, and add scene to stage
      stage.setTitle("Dynamic workspaces: raster");
      stage.setWidth(800);
      stage.setHeight(700);
      stage.setScene(scene);
      stage.show();

      // create Add Raster button
      Button addButton = new Button("Choose Raster");
      addButton.setMaxSize(150, 25);
      addButton.setDisable(true);

      // start local server if found
      if (LocalServer.INSTANCE.checkInstallValid()) {
        server = LocalServer.INSTANCE;
        server.addStatusChangedListener(status -> {
          if (server.getStatus() == LocalServerStatus.STARTED) {
            addButton.setDisable(false);
          }
        });
        // start local server
        server.startAsync();
      } else {
        Platform.runLater(() -> {
          Alert dialog = new Alert(AlertType.INFORMATION);
          dialog.setHeaderText("Local Server Load Error");
          dialog.setContentText("Local Server install path couldn't be located.");
          dialog.showAndWait();
          Platform.exit();
        });
      }

      // choose the file, then start the local map service
      addButton.setOnAction(e -> {
        // Browse to the raster file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.tif"));
        fileChooser.setInitialDirectory(new File("./samples-data/raster/"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
          String fileName = selectedFile.getName();
          String path = selectedFile.getParent();
          startLocalMapService(fileName, path);
        }
      });

      // create a map and add it to a map view
      ArcGISMap map = new ArcGISMap(Basemap.createTopographic());
      mapView = new MapView();
      mapView.setMap(map);

      // add the map view and control panel to stack pane
      stackPane.getChildren().addAll(mapView, addButton);
      StackPane.setAlignment(addButton, Pos.TOP_LEFT);
      StackPane.setMargin(addButton, new Insets(10, 0, 0, 10));
    } catch (Exception e) {
      // on any error, display the stack trace
      e.printStackTrace();
    }
  }

  /**
   * Start a LocalMapService and attaches a dynamic workspace raster.
   * 
   * @param fileName
   * @param path
   */
  private void startLocalMapService(String fileName, String path) {

    // start a service from the blank MPK
    String mapServiceURL = "./samples-data/local_server/mpk_blank.mpk";
    LocalMapService localMapService = new LocalMapService(mapServiceURL);

    // Can't add a dynamic workspace to a running service, so do that first
    RasterWorkspace rasterWorkspace = new RasterWorkspace("raster_wkspc", path);
    RasterSublayerSource source = new RasterSublayerSource(rasterWorkspace.getId(), fileName);
    ArcGISMapImageSublayer imageSublayer = new ArcGISMapImageSublayer(0, source);
    Iterable<DynamicWorkspace> dynamicWorkspaces = Arrays.asList(rasterWorkspace);
    localMapService.setDynamicWorkspaces(dynamicWorkspaces);
    localMapService.addStatusChangedListener(event -> {
      if (event.getNewStatus() == LocalServerStatus.STARTED) {
        // Now, we're ready to add the raster layer. Create a map image layer using url
        ArcGISMapImageLayer imageLayer = new ArcGISMapImageLayer(localMapService.getUrl());

        // Add the sub layer to the image layer
        imageLayer.addDoneLoadingListener(() -> {
          if (imageLayer.getLoadStatus() == LoadStatus.LOADED) {
            imageLayer.getSublayers().add(imageSublayer);
            imageSublayer.addDoneLoadingListener(() -> {
              if (imageSublayer.getLoadStatus() == LoadStatus.LOADED) {
                mapView.setViewpoint(new Viewpoint(imageSublayer.getMapServiceSublayerInfo().getExtent()));
              } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Map Image Sublayer Failed to Load!");
                alert.show();
              }
            });
            imageSublayer.loadAsync();
          } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Map Image Layer Failed to Load!");
            alert.show();
          }
        });
        imageLayer.loadAsync();

        // add the image layer to map. Clear any previous layers
        mapView.getMap().getOperationalLayers().clear();
        mapView.getMap().getOperationalLayers().add(imageLayer);
      }
    });
    localMapService.startAsync();
  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() throws Exception {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}
