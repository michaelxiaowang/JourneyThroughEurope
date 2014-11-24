/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jte.ui;

import application.Main.JTEPropertyType;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jte.data.City;
import jte.data.Player;
import properties_manager.PropertiesManager;

/**
 *
 * @author Michael
 */
public class JTEEventHandler {

    private JTEUI ui;

    public JTEEventHandler(JTEUI initUI) {
        ui = initUI;
    }

    /**
     * Respond to a switch screen request
     *
     * @param uiState the screen user wants to switch to.
     */
    public void respondToSwitchScreenRequest(JTEUI.JTEUIState uiState) {
        ui.changeScreen(uiState);
    }

    public void respondToSetPlayerNumber(int numPlayers) {
        ui.renderPlayerSelection(numPlayers);
    }

    public void respondToNewGameRequest(JTEUI.JTEUIState uiState, int numPlayers) {
        Player.players.clear();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> colors = props.getPropertyOptionsList(JTEPropertyType.PLAYER_COLOR);
        Player.players.clear();
        String name;
        Boolean computer = false;
        Boolean startGame = false;
        for (int i = 0; i < numPlayers; i++) {
            for(Toggle r: ui.playerTypes.get(i).getToggles()) {
                if(((RadioButton)r).isSelected() == true) {
                    computer = true;
                    startGame = true;
                    break;
                }
            }
            if(startGame == false)
                break;
            if (!ui.playerNames.get(i).getText().replaceAll("\\s+", "").equalsIgnoreCase("")) {
                name = ui.playerNames.get(i).getText();
            } else {
                name = ("Player " + ((Integer)(i + 1)).toString());
            }
            Player.players.add(new Player(name, computer));
            Player.players.get(i).setColor(colors.get(i));
        }
        System.out.println(Player.players.size());
        if (startGame == true) {
            ui.initJTEUI();
            ui.changeScreen(uiState);
            ui.dealCards();
            ui.animateBeginning(0);
            for(Player p: Player.players) {
                City start = City.cities.get(p.getCards().get(0));
                p.setStartCity(start);
                p.setCurrentCity(start);
            }
            //Start game with player 1's cards
            ui.placePlayers();
            ui.placeFlags();
            ui.currentPlayer = 0;
            ui.drawLines(Player.players.get(ui.currentPlayer).getCurrentCity());
        }
        else {
            //Inform the user that they did not select the required options
            Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(ui.primaryStage);
                BorderPane okPane = new BorderPane();
                Label text = new Label("You have not selected \"Player\" or \"Computer\" for each player");
                okPane.setCenter(text);
                Button okButton = new Button("OK");
                HBox align = new HBox();
                align.setAlignment(Pos.CENTER);
                align.getChildren().add(okButton);
                okPane.setBottom(align);
                Scene scene = new Scene(okPane, 400, 100);
                dialogStage.setScene(scene);
                dialogStage.show();
                okButton.setOnAction(e -> {
                    // Close screen
                    dialogStage.close();
                });
        }
    }

    public void respondToCityClickedRequest(City city) {
        City currentCity = Player.players.get(ui.currentPlayer).getCurrentCity();
        String cityName = city.getName();
        ArrayList<String> cityNeighbors = new ArrayList();
        cityNeighbors.addAll(currentCity.getSeaNeighbors());
        cityNeighbors.addAll(currentCity.getLandNeighbors());
        if(cityNeighbors.contains(city.getName())) {
            ui.movePlayer(currentCity, city);
        }
    }

    public void respondToRollDieRequest() {
        ui.changeDie();
    }

    public void respondToChangeGridRequest(JTEUI.JTEGridState gridState) {
        ui.changeGrid(gridState);
    }

    //Exit Button
    public void respondToExitRequest(Stage primaryStage) {
        /*// ENGLIS IS THE DEFAULT
         String options[] = new String[]{"Yes", "No"};
         PropertiesManager props = PropertiesManager.getPropertiesManager();
         options[0] = props.getProperty(JTEPropertyType.DEFAULT_YES_TEXT);
         options[1] = props.getProperty(JTEPropertyType.DEFAULT_NO_TEXT);
         String verifyExit = props.getProperty(JTEPropertyType.DEFAULT_EXIT_TEXT);

         // NOW WE'LL CHECK TO SEE IF LANGUAGE SPECIFIC VALUES HAVE BEEN SET
         if (props.getProperty(JTEPropertyType.YES_TEXT) != null) {
         options[0] = props.getProperty(JTEPropertyType.YES_TEXT);
         options[1] = props.getProperty(JTEPropertyType.NO_TEXT);
         verifyExit = props.getProperty(JTEPropertyType.EXIT_REQUEST_TEXT);
         }

         // FIRST MAKE SURE THE USER REALLY WANTS TO EXIT
         Stage dialogStage = new Stage();
         dialogStage.initModality(Modality.WINDOW_MODAL);
         dialogStage.initOwner(primaryStage);
         BorderPane exitPane = new BorderPane();
         HBox optionPane = new HBox();
         optionPane.setAlignment(Pos.CENTER);
         optionPane.setPadding(new Insets(10, 10, 10, 10));
         Button yesButton = new Button(options[0]);
         Button noButton = new Button(options[1]);
         optionPane.setSpacing(10.0);
         optionPane.getChildren().addAll(yesButton, noButton);
         Label exitLabel = new Label(verifyExit);
         exitPane.setCenter(exitLabel);
         exitPane.setBottom(optionPane);
         Scene scene = new Scene(exitPane, 200, 150);
         dialogStage.setScene(scene);
         dialogStage.show();
         // WHAT'S THE USER'S DECISION?
         yesButton.setOnAction(e -> {
         // YES, LET'S EXIT
         System.exit(0);
         });
         noButton.setOnAction(e -> {
         // NO, LET'S NOT
         dialogStage.close();
         });*/

        System.exit(0);

    }
}
