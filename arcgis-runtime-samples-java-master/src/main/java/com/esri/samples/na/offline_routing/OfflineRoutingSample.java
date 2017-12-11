/*
 * Copyright 2017 Esri. Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.esri.samples.na.offline_routing;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.esri.arcgisruntime.tasks.networkanalysis.TravelMode;

public class OfflineRoutingSample extends Application {

  private MapView mapView;
  private GraphicsOverlay stopsOverlay;
  private GraphicsOverlay routeOverlay;
  private RouteTask routeTask;
  private RouteParameters routeParameters;
  private LineSymbol lineSymbol;

  private EventHandler<MouseEvent> mouseMovedListener;

  @Override
  public void start(Stage stage) throws Exception {

    try {
      // create stack pane and application scene
      StackPane stackPane = new StackPane();
      Scene scene = new Scene(stackPane);
      scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

      // set title, size, and add scene to stage
      stage.setTitle("Offline Routing Sample");
      stage.setWidth(800);
      stage.setHeight(700);
      stage.setScene(scene);
      stage.show();

      // create the map's basemap from a local tile package
      TileCache tileCache = new TileCache("./samples-data/san_diego/streetmap_SD.tpk");
      ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
      Basemap basemap = new Basemap(tiledLayer);
      ArcGISMap map = new ArcGISMap(basemap);
      mapView = new MapView();
      mapView.setMap(map);

      // create graphics overlays for route and stops
      stopsOverlay = new GraphicsOverlay();
      routeOverlay = new GraphicsOverlay();
      mapView.getGraphicsOverlays().addAll(Arrays.asList(routeOverlay, stopsOverlay));

      ComboBox<TravelMode> travelModes = new ComboBox<>();
      travelModes.getSelectionModel().selectedItemProperty().addListener(o -> {
        routeParameters.setTravelMode(travelModes.getSelectionModel().getSelectedItem());
        updateRoute();
      });
      // display travel mode name within combobox
      travelModes.setConverter(new StringConverter<TravelMode>() {

        @Override
        public String toString(TravelMode travelMode) {

          return travelMode.getName();
        }

        @Override
        public TravelMode fromString(String fileName) {

          return null;
        }
      });

      // create an offline RouteTask
      routeTask = new RouteTask("./samples-data/san_diego/sandiego.geodatabase", "Streets_ND");
      routeTask.loadAsync();
      routeTask.addDoneLoadingListener(() -> {
        if (routeTask.getLoadStatus() == LoadStatus.LOADED) {
          try {
            // create route parameters
            routeParameters = routeTask.createDefaultParametersAsync().get();

            travelModes.getItems().addAll(routeTask.getRouteTaskInfo().getTravelModes());
            travelModes.getSelectionModel().select(0);
          } catch (InterruptedException | ExecutionException e) {
            displayMessage("Error getting default route parameters", e.getMessage());
          }
        } else {
          displayMessage("Error loading route task", routeTask.getLoadError().getMessage());
        }
      });

      // add a graphics overlay to show the boundary
      Envelope envelope = new Envelope(new Point(-13045352.223196, 3864910.900750, 0, SpatialReferences.getWebMercator()),
          new Point(-13024588.857198, 3838880.505604, 0, SpatialReferences.getWebMercator()));
      SimpleLineSymbol boundarySymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.DASH, 0xFF00FF00, 5);
      Graphic boundary = new Graphic(envelope, boundarySymbol);
      GraphicsOverlay boundaryOverlay = new GraphicsOverlay();
      boundaryOverlay.getGraphics().add(boundary);
      mapView.getGraphicsOverlays().add(boundaryOverlay);

      // create symbol for route
      lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF0000FF, 3);

      // create mouse moved event listener to update the route when moving stops
      mouseMovedListener = event -> {

        if (!stopsOverlay.getSelectedGraphics().isEmpty()) {

          Point2D hoverLocation = new Point2D(event.getX(), event.getY());
          Point hoverPoint = mapView.screenToLocation(hoverLocation);

          // update the moving stop and graphic
          Graphic stopGraphic = stopsOverlay.getSelectedGraphics().get(0);
          stopGraphic.setGeometry(hoverPoint);

          // update route
          if (stopsOverlay.getGraphics().size() > 1) {
            updateRoute();
          }
        }

      };

      // use mouse clicks to add and move stops
      mapView.setOnMouseClicked(event -> {

        if (routeTask.getLoadStatus() == LoadStatus.LOADED && event.isStillSincePress()) {

          // get mouse click location
          Point2D clickLocation = new Point2D(event.getX(), event.getY());
          Point point = mapView.screenToLocation(clickLocation);

          // left click adds a stop when not already moving a stop
          if (event.getButton() == MouseButton.PRIMARY && stopsOverlay.getSelectedGraphics().isEmpty()) {

            // create graphic for stop
            TextSymbol stopLabel = new TextSymbol(20, Integer.toString(stopsOverlay.getGraphics().size() + 1),
                0xFFFF0000, TextSymbol.HorizontalAlignment.RIGHT, TextSymbol.VerticalAlignment.TOP);

            // create and add the stop graphic to the graphics overlay and list
            Graphic stopGraphic = new Graphic(point, stopLabel);
            stopsOverlay.getGraphics().add(stopGraphic);

            // update the route
            updateRoute();
          }

          // right click to select/deselect a stop to move
          if (event.getButton() == MouseButton.SECONDARY) {

            // select a stop
            if (stopsOverlay.getSelectedGraphics().isEmpty()) {
              // identify the selected graphic
              ListenableFuture<IdentifyGraphicsOverlayResult> results = mapView.identifyGraphicsOverlayAsync(
                  stopsOverlay, clickLocation, 10, false);
              results.addDoneListener(() -> {
                try {
                  List<Graphic> graphics = results.get().getGraphics();
                  if (graphics.size() > 0) {
                    // set the graphic as selected
                    Graphic graphic = graphics.get(0);
                    graphic.setSelected(true);

                    // add the mouse move event listener to update the route
                    mapView.setOnMouseMoved(mouseMovedListener);
                  }

                } catch (InterruptedException | ExecutionException e) {
                  e.printStackTrace();
                }
              });
            } else {
              stopsOverlay.clearSelection();

              // remove the mouse moved event listener when not moving stops
              mapView.setOnMouseMoved(null);
            }
          }
        }
      });

      // add controls to stackpane
      stackPane.getChildren().addAll(mapView, travelModes);
      StackPane.setAlignment(travelModes, Pos.TOP_LEFT);
      StackPane.setMargin(travelModes, new Insets(10, 0, 0, 10));

    } catch (Exception e) {
      // on any error, display the stack trace.
      e.printStackTrace();
    }
  }

  /**
   * Update the route based on the set or moving stops.
   */
  private void updateRoute() {

    if (stopsOverlay.getGraphics().size() > 1) {
      // remove listener until route task is solved
      if (!stopsOverlay.getSelectedGraphics().isEmpty()) {
        mapView.setOnMouseMoved(null);
      }

      // update stops and solve route
      List<Stop> stops = stopsOverlay.getGraphics().stream()
          .map(g -> new Stop((Point) g.getGeometry()))
          .collect(Collectors.toList());
      routeParameters.setStops(stops);
      ListenableFuture<RouteResult> results = routeTask.solveRouteAsync(routeParameters);
      results.addDoneListener(() -> {
        try {
          RouteResult result = results.get();
          Route route = result.getRoutes().get(0);

          // create graphic for route
          Graphic graphic = new Graphic(route.getRouteGeometry(), lineSymbol);

          // replace route graphic
          routeOverlay.getGraphics().clear();
          routeOverlay.getGraphics().add(graphic);

        } catch (InterruptedException | ExecutionException e) {
          // ignore, no route solution

        } finally {
          // add mouse moved listener back
          if (!stopsOverlay.getSelectedGraphics().isEmpty()) {
            mapView.setOnMouseMoved(mouseMovedListener);
          }
        }
      });
    }
  }

  /**
   * Shows a message in an alert dialog.
   *
   * @param title title of alert
   * @param message message to display
   */
  private void displayMessage(String title, String message) {

    Platform.runLater(() -> {
      Alert dialog = new Alert(Alert.AlertType.INFORMATION);
      dialog.setHeaderText(title);
      dialog.setContentText(message);
      dialog.showAndWait();
    });
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
