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
    private boolean diceRolled = false;
    private City noBackTrack;

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
        String name;
        Boolean computer = false;
        Boolean startGame = false;
        for (int i = 0; i < numPlayers; i++) {
            for (Toggle r : ui.playerTypes.get(i).getToggles()) {
                if (((RadioButton) r).isSelected() == true) {
                    computer = true;
                    startGame = true;
                    break;
                }
            }
            if (startGame == false) {
                break;
            }
            if (!ui.playerNames.get(i).getText().replaceAll("\\s+", "").equalsIgnoreCase("")) {
                name = ui.playerNames.get(i).getText();
            } else {
                name = ("Player " + ((Integer) (i + 1)).toString());
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
            for (Player p : Player.players) {
                City start = City.cities.get(p.getCards().get(0));
                p.setStartCity(start);
                p.setCurrentCity(start);
            }
            //Start game with player 1's cards
            ui.placePlayers();
            ui.placeFlags();
            ui.currentPlayer = 0;
            goToPlayerGrid();
            ui.drawLines(Player.players.get(ui.currentPlayer).getCurrentCity());
        } else {
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
        if(diceRolled == false) {
            ui.updateStatusInfoLabel("You must roll the dice before moving.");
        }
        if(noBackTrack != null && city == noBackTrack) {
            if(currentCity.getLandNeighbors().size() + currentCity.getSeaNeighbors().size() > 1) {
                ui.updateStatusInfoLabel("Cannot move to a city you just came from.");
            }
            else {
                noBackTrack = null;
                respondToCityClickedRequest(city);
            }
        }
        else if (Player.players.get(ui.currentPlayer).getTurns() > 0 || Player.players.get(ui.currentPlayer).isWaitingForShip()) {
            noBackTrack = currentCity;
            if (currentCity.getLandNeighbors().contains(city.getName())) {
                if (currentCity.getLandNeighbors().contains(city.getName())) {
                    ui.updateStatusInfoLabel("Moved from " + currentCity.getName() + " to " + city.getName());
                    ui.movePlayer(currentCity, city);
                    Player.players.get(ui.currentPlayer).setTurns(Player.players.get(ui.currentPlayer).getTurns() - 1);
                    ui.updateTurnInfoLabel("Moves left: " + Player.players.get(ui.currentPlayer).getTurns());
                    if(Player.players.get(ui.currentPlayer).getTurns() == 0) {
                        ui.updateStatusInfoLabel("No moves left. Click \"End Turn\" to end your turn.");
                    }
                }
                if (Player.players.get(ui.currentPlayer).getCards().contains(city.getName())) {
                    if (!Player.players.get(ui.currentPlayer).getCards().get(0).equalsIgnoreCase(city.getName())) {
                        Player.players.get(ui.currentPlayer).getCards().remove(city.getName());
                    } else {
                        if (Player.players.get(ui.currentPlayer).getCards().size() == 1) {
                            Player.players.get(ui.currentPlayer).getCards().remove(city.getName());
                        }
                    }
                    ui.changeCardPane(ui.currentPlayer);
                }
            }
            if (currentCity.getSeaNeighbors().contains(city.getName())) {
                if (Player.players.get(ui.currentPlayer).isWaitingForShip()) {
                    
                    
                } else {
                    Player.players.get(ui.currentPlayer).setTurns(0);
                    ui.updateStatusInfoLabel("Waiting for ship... It will come next turn.");
                    ui.updateTurnInfoLabel("Moves Left: " + Player.players.get(ui.currentPlayer).getTurns());
                    Player.players.get(ui.currentPlayer).setWaitingForShip(true);
                    Player.players.get(ui.currentPlayer).setWaitingCity(city);
                }
            }
        }
    }
    
    public void respondToEndTurnRequest() {
        if (Player.players.get(ui.currentPlayer).getTurns() == 0 && diceRolled) {
            ui.currentPlayer = (ui.currentPlayer + 1) % Player.players.size();
            ui.changeCardPane(ui.currentPlayer);
            diceRolled = false;
            if (Player.players.get(ui.currentPlayer).isWaitingForShip()) {
                respondToCityClickedRequest(Player.players.get(ui.currentPlayer).getWaitingCity());
            }
            ui.updateTurnInfoLabel("Roll Dice!");
            ui.updateStatusInfoLabel(Player.players.get(ui.currentPlayer).getName() + "'s turn begins.");
            goToPlayerGrid();
            if (Player.players.get(ui.currentPlayer).isWaitingForShip()) {
                ui.movePlayer(Player.players.get(ui.currentPlayer).getCurrentCity(), Player.players.get(ui.currentPlayer).getWaitingCity());
                Player.players.get(ui.currentPlayer).setWaitingForShip(false);
            }
        }
    }
    
    public void goToPlayerGrid() {
        JTEUI.JTEGridState gridState;
        switch(Player.players.get(ui.currentPlayer).getCurrentCity().getGrid()) {
            case 1:
                gridState = JTEUI.JTEGridState.ONE_STATE;
                break;
            case 2:
                gridState = JTEUI.JTEGridState.TWO_STATE;
                break;
            case 3:
                gridState = JTEUI.JTEGridState.THREE_STATE;
                break;
            case 4:
                gridState = JTEUI.JTEGridState.FOUR_STATE;
                break;
            default:
                gridState = JTEUI.JTEGridState.ONE_STATE;
                break;
        }
        ui.changeGrid(gridState);
    }

    public void respondToRollDieRequest() {
        if (!diceRolled) {
            int rollResult = (int) (Math.random() * 6) + 1;
            Player.players.get(ui.currentPlayer).setTurns(rollResult);
            ui.changeDie(rollResult);
            diceRolled = true;
            ui.updateTurnInfoLabel("Moves Left: " + rollResult);
        }
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
