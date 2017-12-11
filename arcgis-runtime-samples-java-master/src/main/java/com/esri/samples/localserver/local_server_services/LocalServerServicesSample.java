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

package com.esri.samples.localserver.local_server_services;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LocalServerServicesSample extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    // set up the scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/local_server_services.fxml"));
    Parent root = loader.load();
    LocalServerServicesController controller = loader.getController();
    controller.setHostServices(getHostServices());
    Scene scene = new Scene(root);

    // set up the stage
    stage.setTitle("Local Server Services Sample");
    stage.setWidth(800);
    stage.setHeight(400);
    stage.setScene(scene);
    stage.show();
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
