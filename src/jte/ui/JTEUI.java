/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jte.ui;

import application.Main.JTEPropertyType;
import java.util.ArrayList;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import jte.data.City;
import jte.data.Player;
import jte.game.JTEGameStateManager;
import properties_manager.PropertiesManager;
import xmlparser.XMLParser;

/**
 *
 * @author Michael
 */
public class JTEUI extends Pane {

    public enum JTEUIState {

        SPLASH_SCREEN_STATE, SELECT_PLAYER_STATE, PLAY_GAME_STATE, VIEW_HISTORY_STATE, ABOUT_GAME_STATE,
    }

    public enum JTEDiceState {

        ONE_STATE, TWO_STATE, THREE_STATE, FOUR_STATE, FIVE_STATE, SIX_STATE
    }

    public enum JTEGridState {

        ONE_STATE, TWO_STATE, THREE_STATE, FOUR_STATE
    }

    //PropertiesManager
    PropertiesManager props = PropertiesManager.getPropertiesManager();

    // Image path
    private String ImgPath = "file:images/";

    // mainStage
    public Stage primaryStage;

    // mainPane
    private BorderPane mainPane;
    private int paneWidth;
    private int paneHeigth;

    //splashScreen
    private VBox splashPane;
    private HBox menuBox;
    private Image splashImage;
    private ImageView splashImageView;
    private HBox splashImageAlign;
    private Button playButton;
    private Button loadButton;
    private Button aboutButton;
    private Button exitButton;

    //selectPlayerScreen
    private VBox selectPlayerPane;
    private HBox numberPlayerPane;
    private Label numberPlayerPrompt;
    private ComboBox numberPlayerList;
    private Button goButton;
    private GridPane setPlayerPane;

    //gamePane
    public ArrayList<TextField> playerNames;
    public ArrayList<ToggleGroup> playerTypes;
    public int currentPlayer;
    private int currentGrid;
    private Label turnInfo;
    private Label statusInfo;
    private ImageView grid1;
    private ImageView grid2;
    private ImageView grid3;
    private ImageView grid4;
    private StackPane nameBox;
    private AnchorPane gridPane;
    private ImageView diceView;
    private GridPane gridSelector;
    private BorderPane gamePane;
    private BorderPane gridFrame;
    private VBox leftGamePane;
    public AnchorPane cardPane;
    private BorderPane rightGamePane;

    //historyScreen
    private VBox historyPane;
    private StackPane historyFrame;
    private WebView historyView;
    private WebEngine historyEngine;
    private Button returnFromHistoryButton;

    //aboutScreen
    private VBox aboutPane;
    private StackPane webFrame;
    private WebView aboutWebView;
    private WebEngine aboutWebEngine;
    private Button returnFromAboutButton;
    private JTEUIState currentState;
    private JTEUIState returnToState;

    // Padding
    private Insets marginlessInsets;

    //EventHandler
    private JTEEventHandler eventHandler;

    //GameStateManager
    JTEGameStateManager gsm;

    /**
     * Constructor creates JTEEventHandler, and GameStateManager
     */
    public JTEUI() {
        gsm = new JTEGameStateManager(this);
        eventHandler = new JTEEventHandler(this);
        initMainPane();
        initSplashScreen();
        initAboutScreen();
        initSelectPlayerScreen();
        currentState = JTEUIState.SPLASH_SCREEN_STATE;
    }

    public void SetStage(Stage stage) {
        primaryStage = stage;
    }

    public Stage getStage() {
        return primaryStage;
    }

    public BorderPane GetMainPane() {
        return this.mainPane;
    }

    public JTEGameStateManager getGSM() {
        return gsm;
    }

    public void initMainPane() {
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();

        paneWidth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_WIDTH));
        paneHeigth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeigth);
        //mainPane.setPadding(marginlessInsets);
        mainPane.setStyle("-fx-background: #4682B4;");
    }

    public void initSplashScreen() {

        splashPane = new VBox();
        splashPane.setAlignment(Pos.CENTER);
        splashPane.setSpacing(10.0);
        menuBox = new HBox();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setSpacing(10.0);

        //Make the splash image
        splashImage = loadImage(props.getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        splashImageView = new ImageView(splashImage);
        splashImageView.fitHeightProperty().bind(mainPane.heightProperty().divide(8).multiply(7));
        splashImageView.fitWidthProperty().bind(mainPane.widthProperty());
        splashImageAlign = new HBox();
        splashImageAlign.getChildren().add(splashImageView);
        splashImageAlign.setAlignment(Pos.CENTER);

        //Make the play button
        Image playButtonImage = loadImage(props.getProperty(JTEPropertyType.PLAY_IMG_NAME));
        ImageView playButtonView = new ImageView(playButtonImage);
        playButton = new Button();
        playButton.setGraphic(playButtonView);
        playButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JTEUIState.SELECT_PLAYER_STATE);
            }

        });

        //Make the load button
        Image loadButtonImage = loadImage(props.getProperty(JTEPropertyType.LOAD_IMG_NAME));
        ImageView loadButtonView = new ImageView(loadButtonImage);
        loadButton = new Button();
        loadButton.setGraphic(loadButtonView);
        loadButton.setDisable(true);

        //Make the about button
        Image aboutButtonImage = loadImage(props.getProperty(JTEPropertyType.ABOUT_IMG_NAME));
        ImageView aboutButtonView = new ImageView(aboutButtonImage);
        aboutButton = new Button();
        aboutButton.setGraphic(aboutButtonView);
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JTEUIState.ABOUT_GAME_STATE);
            }

        });

        //Make the exit button
        Image exitButtonImage = loadImage(props.getProperty(JTEPropertyType.EXIT_IMG_NAME));
        ImageView exitButtonView = new ImageView(exitButtonImage);
        exitButton = new Button();
        exitButton.setGraphic(exitButtonView);
        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToExitRequest(primaryStage);
            }

        });

        //Add the buttons
        menuBox.getChildren().addAll(playButton, loadButton, aboutButton, exitButton);

        //Add menu box to splashPane
        splashPane.getChildren().addAll(splashImageAlign, menuBox);

        //Set the mainPane to splashScreen components
        mainPane.setCenter(splashPane);
    }

    public void initJTEUI() {
        // FIRST REMOVE THE SPLASH SCREEN
        mainPane.getChildren().clear();

        // INITIALIZE GAME SCREEN
        initGameScreen();
        initHistoryScreen();
    }

    /**
     * Initialize the select player screen
     */
    public void initSelectPlayerScreen() {
        selectPlayerPane = new VBox();
        numberPlayerPane = new HBox();
        playerNames = new ArrayList();
        playerTypes = new ArrayList();

        //Make the label
        numberPlayerPrompt = new Label();
        numberPlayerPrompt.setText("Number of Players:");
        numberPlayerPrompt.setPadding(marginlessInsets);

        //Make the ComboBox
        numberPlayerList = new ComboBox();
        numberPlayerList.getItems().addAll("2", "3", "4", "5", "6");
        numberPlayerList.setValue("2");
        numberPlayerList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSetPlayerNumber(Integer.parseInt((String) numberPlayerList.getValue()));
            }

        });

        //Make the go button
        goButton = new Button();
        goButton.setText("GO");
        goButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToNewGameRequest(JTEUIState.PLAY_GAME_STATE, Integer.parseInt((String) (numberPlayerList.getValue())));
            }

        });

        //Make setPlayerPane
        setPlayerPane = new GridPane();
        setPlayerPane.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        renderPlayerSelection(2);

        //Add to numberPlayerPane
        numberPlayerPane.getChildren().addAll(numberPlayerPrompt, numberPlayerList, goButton);

        //Add to selectPlayerPane
        selectPlayerPane.getChildren().addAll(numberPlayerPane, setPlayerPane);
    }

    public void renderPlayerSelection(int numPlayers) {
        setPlayerPane.getChildren().clear(); //clean the previously rendered boxes
        playerNames.clear();
        playerTypes.clear();
        //make the set player boxes
        int row = 0, col = 0;
        int added = 0;

        //draw six boxes
        for (int i = 0; i < 6; i++) {

            //playerSelectionBox
            BorderPane playerSelectionBox = new BorderPane();
            playerSelectionBox.setPrefSize(400, 380);
            playerSelectionBox.setStyle("-fx-border-color: black;");

            //new row when 3 are in one row already
            if (added >= 3) {
                row++;
                col = 0;
                added = 0;
            }

            //display set player options in number of boxes equal to number of players
            if (i < numPlayers) {
                HBox playerOptions = new HBox();
                VBox playerOptionsAlign = new VBox();
                playerOptions.setAlignment(Pos.CENTER);
                playerOptionsAlign.setAlignment(Pos.CENTER);
                playerOptions.setSpacing(10);

                //PlayerFlagImage
                ArrayList<String> flagImages = props.getPropertyOptionsList(JTEPropertyType.PLAYER_FLAG);
                Image flag = loadImage(flagImages.get(i));
                ImageView flagView = new ImageView(flag);
                flagView.setFitHeight(50);
                flagView.setFitWidth(50);

                //SetPlayerTypeBox
                VBox setPlayerTypeBox = new VBox();
                setPlayerTypeBox.setSpacing(5);
                final ToggleGroup group = new ToggleGroup();
                playerTypes.add(group);
                RadioButton playerIsPlayer = new RadioButton("Player");
                RadioButton playerIsComputer = new RadioButton("Computer");
                playerIsPlayer.setToggleGroup(group);
                playerIsComputer.setToggleGroup(group);
                setPlayerTypeBox.getChildren().addAll(playerIsPlayer, playerIsComputer);

                //setPlayerNameBox
                VBox setPlayerNameBox = new VBox();
                Label namePrompt = new Label("Name");
                TextField playerName = new TextField();
                playerNames.add(playerName);
                setPlayerNameBox.getChildren().addAll(namePrompt, playerName);

                playerOptions.getChildren().addAll(flagView, setPlayerTypeBox, setPlayerNameBox);

                playerOptionsAlign.getChildren().add(playerOptions);
                playerSelectionBox.setCenter(playerOptionsAlign);
            }

            //add the box, increment column and number added
            setPlayerPane.add(playerSelectionBox, col, row);
            col++;
            added++;
        }
    }

    /**
     * Initialize the game screen
     */
    public void initGameScreen() {
        gamePane = new BorderPane();
        gridPane = new AnchorPane();
        gridFrame = new BorderPane();
        gridFrame.setStyle("-fx-border-color:black; -fx-border-width: 3px");

        //Load the grid images
        Image gridA = loadImage(props.getProperty(JTEPropertyType.GRID_1_IMG_NAME));
        grid1 = new ImageView(gridA);
        Image gridB = loadImage(props.getProperty(JTEPropertyType.GRID_2_IMG_NAME));
        grid2 = new ImageView(gridB);
        Image gridC = loadImage(props.getProperty(JTEPropertyType.GRID_3_IMG_NAME));
        grid3 = new ImageView(gridC);
        Image gridD = loadImage(props.getProperty(JTEPropertyType.GRID_4_IMG_NAME));
        grid4 = new ImageView(gridD);

        //Begin with grid1
        currentGrid = 1;
        gridFrame.setCenter(grid1);
        gridPane.getChildren().add(gridFrame);
        gamePane.setCenter(gridPane);

        //gamePane left
        leftGamePane = new VBox();
        leftGamePane.setPrefSize(300, 800);
        gamePane.setLeft(leftGamePane);
        nameBox = new StackPane();
        nameBox.setPrefSize(300, 50);
        nameBox.setStyle("-fx-border-color: #002966; -fx-border-width: 2px; -fx-background-color: #127696");
        cardPane = new AnchorPane();
        statusInfo = new Label("Dealing Cards...");
        HBox statusAlign = new HBox();
        statusAlign.setAlignment(Pos.CENTER);
        statusAlign.getChildren().add(statusInfo);
        leftGamePane.getChildren().addAll(nameBox, statusAlign, cardPane);
        leftGamePane.setSpacing(20);

        //gamePane right
        rightGamePane = new BorderPane();
        rightGamePane.setPrefSize(300, 800);
        gamePane.setRight(rightGamePane);

        //Toolbar buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        Button about = new Button("About");
        about.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JTEUIState.ABOUT_GAME_STATE);
            }

        });
        Button history = new Button("History");
        history.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_HISTORY_STATE);
            }

        });
        Button home = new Button("Home");
        home.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(JTEUIState.SPLASH_SCREEN_STATE);
            }

        });
        Button exit = new Button("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToExitRequest(primaryStage);
            }

        });
        Button end = new Button("End Turn");
        end.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToEndTurnRequest();
            }

        });
        buttons.getChildren().addAll(home, about, history, exit, end);
        rightGamePane.setTop(buttons);
        
        //Turn Info Text
        turnInfo = new Label("Roll Dice!");

        //Dice
        Image dice = loadImage("die_1.jpg");
        diceView = new ImageView(dice);
        diceView.setStyle("-fx-cursor: hand;");
        diceView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToRollDieRequest();
            }

        });

        //GridSelector
        gridSelector = new GridPane();
        gridSelector.setStyle("-fx-cursor: hand; -fx-border-color: black; -fx-border-width: 1px");
        ImageView gridImage1 = new ImageView(loadImage(props.getProperty(JTEPropertyType.GRID_1_IMG_NAME)));
        ImageView gridImage2 = new ImageView(loadImage(props.getProperty(JTEPropertyType.GRID_2_IMG_NAME)));
        ImageView gridImage3 = new ImageView(loadImage(props.getProperty(JTEPropertyType.GRID_3_IMG_NAME)));
        ImageView gridImage4 = new ImageView(loadImage(props.getProperty(JTEPropertyType.GRID_4_IMG_NAME)));
        gridImage1.setFitWidth(75);
        gridImage1.setFitHeight(100);
        gridImage2.setFitWidth(75);
        gridImage2.setFitHeight(100);
        gridImage3.setFitWidth(75);
        gridImage3.setFitHeight(100);
        gridImage4.setFitWidth(75);
        gridImage4.setFitHeight(100);

        //Add border to each gridSelector element
        BorderPane grid1Stack = new BorderPane();
        grid1Stack.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                eventHandler.respondToChangeGridRequest(JTEGridState.ONE_STATE);
            }

        });
        BorderPane grid2Stack = new BorderPane();
        grid2Stack.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                eventHandler.respondToChangeGridRequest(JTEGridState.TWO_STATE);
            }

        });
        BorderPane grid3Stack = new BorderPane();
        grid3Stack.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                eventHandler.respondToChangeGridRequest(JTEGridState.THREE_STATE);
            }

        });
        BorderPane grid4Stack = new BorderPane();
        grid4Stack.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                eventHandler.respondToChangeGridRequest(JTEGridState.FOUR_STATE);
            }

        });
        grid1Stack.setCenter(gridImage1);
        grid1Stack.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        grid2Stack.setCenter(gridImage2);
        grid2Stack.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        grid3Stack.setCenter(gridImage3);
        grid3Stack.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        grid4Stack.setCenter(gridImage4);
        grid4Stack.setStyle("-fx-border-color: black; -fx-border-width: 1px");

        //Now add the elements to the gridSelector
        gridSelector.add(grid1Stack, 0, 0);
        gridSelector.add(grid2Stack, 1, 0);
        gridSelector.add(grid3Stack, 0, 1);
        gridSelector.add(grid4Stack, 1, 1);

        //Add the die and gridselector
        VBox dieGridV = new VBox();
        HBox dieGridH = new HBox();
        dieGridV.getChildren().addAll(turnInfo, diceView, gridSelector);
        dieGridV.setAlignment(Pos.CENTER);
        dieGridV.setSpacing(20);
        dieGridH.getChildren().add(dieGridV);
        dieGridH.setAlignment(Pos.CENTER);
        rightGamePane.setCenter(dieGridH);

        //Load the city info
        XMLParser.parsexml();
        setCityButtons();
    }

    /**
     * Deal the cards
     */
    public void dealCards() {
        ArrayList<String> playerPieces = props.getPropertyOptionsList(JTEPropertyType.PLAYER_PIECE);
        ArrayList<String> playerFlags = props.getPropertyOptionsList(JTEPropertyType.PLAYER_FLAG);
        for (int i = 0; i < Player.players.size(); i++) {

            //Set the player flags and pieces
            Player player = Player.players.get(i);
            ImageView piece = new ImageView(loadImage(playerPieces.get(i)));
            ImageView flag = new ImageView(loadImage(playerFlags.get(i)));
            piece.setFitWidth(30);
            piece.setFitHeight(30);
            flag.setFitWidth(30);
            flag.setFitHeight(30);
            player.setPiece(piece);
            player.setFlag(flag);

            //Deal the cards
            int card;
            switch ((i) % 3) {
                case 0:
                    card = (int) (Math.random() * City.redcities.size());
                    player.getCards().add(City.redcities.remove(card));
                    card = (int) (Math.random() * City.greencities.size());
                    player.getCards().add(City.greencities.remove(card));
                    card = (int) (Math.random() * City.yellowcities.size());
                    player.getCards().add(City.yellowcities.remove(card));
                    break;
                case 1:
                    card = (int) (Math.random() * City.greencities.size());
                    player.getCards().add(City.greencities.remove(card));
                    card = (int) (Math.random() * City.yellowcities.size());
                    player.getCards().add(City.yellowcities.remove(card));
                    card = (int) (Math.random() * City.redcities.size());
                    player.getCards().add(City.redcities.remove(card));
                    break;
                case 2:
                    card = (int) (Math.random() * City.yellowcities.size());
                    player.getCards().add(City.yellowcities.remove(card));
                    card = (int) (Math.random() * City.redcities.size());
                    player.getCards().add(City.redcities.remove(card));
                    card = (int) (Math.random() * City.greencities.size());
                    player.getCards().add(City.greencities.remove(card));
                    break;
            }
        }
        for (Player p : Player.players) {
            System.out.println(p.getName() + p.getCards());
        }
    }

    //Animate it
    public void animateBeginning(int p) {
        Label playerName = new Label(Player.players.get(p).getName());
        nameBox.getChildren().add(playerName);
        if (Player.players.get(p).getColor().equalsIgnoreCase("black")) {
            playerName.setTextFill(Color.web("white"));
        } else {
            playerName.setTextFill(Color.web("black"));
        }
        nameBox.setStyle("-fx-background-color: " + Player.players.get(p).getColor() + ";-fx-border-color: #002966; -fx-border-width: 2px;");
        SequentialTransition timeline = new SequentialTransition();
        timeline.getChildren().clear();
        double layoutX = 25;
        double layoutY = 0;
        for (int j = 0; j < Player.players.get(p).getCards().size(); j++) {
            ImageView c = new ImageView(loadImage(Player.players.get(p).getCards().get(j) + ".JPG"));
            c.setFitWidth(250);
            c.setFitHeight(400);
            HBox cardBorder = new HBox();
            cardBorder.setLayoutX(layoutX);
            cardBorder.setLayoutY(layoutY);
            cardBorder.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            cardBorder.getChildren().add(c);
            layoutY += 75;
            TranslateTransition animation = new TranslateTransition(Duration.millis(1000), cardBorder);
            animation.setFromX(600);
            animation.setFromY(400);
            animation.setToX(0);
            animation.setToY(0);
            timeline.getChildren().add(animation);
            cardPane.getChildren().add(cardBorder);
        }
        timeline.play();
        //Necessary to make the next animation wait until done
        timeline.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                cardPane.getChildren().clear();
                nameBox.getChildren().clear();
                if (p < (Player.players.size() - 1)) {
                    animateBeginning(p + 1);
                } else {
                    changeCardPane(0);
                    updateStatusInfoLabel(Player.players.get(0).getName() + "'s turn begins.");
                }
            }
        });
    }

    /**
     * Place the pieces
     */
    public void placePlayers() {
        ImageView playerPiece = new ImageView();
        for (Player p : Player.players) {
            if (p.getCurrentCity().getGrid() == currentGrid) {
                playerPiece = p.getPiece();
                playerPiece.setLayoutX(((float) p.getCurrentCity().getX() - 5));
                playerPiece.setLayoutY(((float) p.getCurrentCity().getY() - 15));
                gridPane.getChildren().add(playerPiece);
            }
        }
    }

    /**
     * Place the flags
     */
    public void placeFlags() {
        ImageView playerFlag = new ImageView();
        for (Player p : Player.players) {
            if (City.cities.get(p.getCards().get(0)).getGrid() == currentGrid) {
                playerFlag = p.getFlag();
                playerFlag.setLayoutX(((float) p.getStartCity().getX() - 15));
                playerFlag.setLayoutY(((float) p.getStartCity().getY() - 25));
                gridPane.getChildren().add(playerFlag);
            }
        }
    }

    /**
     * Draw the lines
     */
    public void drawLines(City c) {
        if (c.getGrid() != currentGrid) {
            return;
        }
        ArrayList<String> n = new ArrayList();
        for (String l : c.getLandNeighbors()) {
            n.add(l);
        }
        for (String s : c.getSeaNeighbors()) {
            n.add(s);
        }
        for (String s : n) {
            City neighbor = City.cities.get(s);
            Line line = new Line(c.getX(), c.getY(), neighbor.getX(), neighbor.getY());
            line.setStyle("-fx-stroke: red");
            if (c.getGrid() == neighbor.getGrid()) {
                gridPane.getChildren().add(line);
            }
        }
    }

    /**
     * Set the card pane
     */
    public void changeCardPane(int n) {
        cardPane.getChildren().clear();
        Label playerName = new Label(Player.players.get(n).getName());
        nameBox.getChildren().add(playerName);
        if (Player.players.get(n).getColor().equalsIgnoreCase("black")) {
            playerName.setTextFill(Color.web("white"));
        } else {
            playerName.setTextFill(Color.web("black"));
        }
        nameBox.setStyle("-fx-background-color: " + Player.players.get(n).getColor() + ";-fx-border-color: #002966; -fx-border-width: 2px;");
        double layoutX = 25;
        double layoutY = 0;
        for (int j = 0; j < Player.players.get(n).getCards().size(); j++) {
            ImageView c = new ImageView(loadImage(Player.players.get(n).getCards().get(j) + ".JPG"));
            c.setFitWidth(250);
            c.setFitHeight(400);
            HBox cardBorder = new HBox();
            cardBorder.setLayoutX(layoutX);
            cardBorder.setLayoutY(layoutY);
            cardBorder.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
            cardBorder.getChildren().add(c);
            layoutY += 75;
            cardPane.getChildren().add(cardBorder);
        }
    }
    
    /**
     * Update turn info label
     */
    public void updateTurnInfoLabel(String s) {
        turnInfo.setText(s);
    }
    
    /**
     * Update status info label
     */
    public void updateStatusInfoLabel(String s) {
        statusInfo.setText(s);
    }

    /**
     * Change Die Picture
     */
    public void changeDie(int num) {
        String dieImagePath = "die_" + ((Integer)num).toString() + ".jpg";
        diceView.setImage(loadImage(dieImagePath));
    }

    /**
     * Change the grid
     */
    public void changeGrid(JTEGridState gridState) {
        gridPane.getChildren().clear();
        switch (gridState) {
            case ONE_STATE:
                currentGrid = 1;
                gridFrame.setCenter(grid1);
                break;
            case TWO_STATE:
                currentGrid = 2;
                gridFrame.setCenter(grid2);
                break;
            case THREE_STATE:
                currentGrid = 3;
                gridFrame.setCenter(grid3);
                break;
            case FOUR_STATE:
                currentGrid = 4;
                gridFrame.setCenter(grid4);
                break;
        }
        gridPane.getChildren().add(gridFrame);
        placeFlags();
        placePlayers();
        setCityButtons();
        if (Player.players.get(currentPlayer).getCurrentCity().getGrid() == currentGrid) {
            drawLines(Player.players.get(currentPlayer).getCurrentCity());
        }
    }
    
    /**
     * Set the city buttons in the currentGrid
     */
    public void setCityButtons() {
        for (City city : City.cities.values()) {
            //Make the city button
            if (city.getGrid() == currentGrid) {
                Button cityButton = new Button();
                Tooltip cityInfo = new Tooltip(city.getName() + "\n (" + city.getX() + ", " + city.getY() + ")");
                cityButton.setTooltip(cityInfo);
                Circle circle = new Circle();
                circle.setRadius(10);
                cityButton.setShape(circle);
                cityButton.setLayoutX(city.getX() - 10);
                cityButton.setLayoutY(city.getY() - 10);
                cityButton.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                gridPane.getChildren().add(cityButton);

                //When button is clicked, respond accordingly
                cityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        eventHandler.respondToCityClickedRequest(city);
                    }

                });
                
                
                cityButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        cityButton.setMouseTransparent(false);
                    }

                });

                cityButton.setOnDragDetected(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        // TODO Auto-generated method stub
                        if(city.getX() == Player.players.get(currentPlayer).getCurrentCity().getX()) {
                            cityButton.startFullDrag();
                            cityButton.setMouseTransparent(true);
                        }
                        else {
                            event.consume();
                        }
                    }

                });

                cityButton.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
                @Override
                public void handle(MouseDragEvent event) {
                    eventHandler.respondToCityClickedRequest(city);
                }
            });

            }
        }
    }

    /**
     * Move the player piece
     */
    public void movePlayer(City city1, City city2) {
        Player p = Player.players.get(currentPlayer);
        ImageView playerPiece = p.getPiece();
        p.setCurrentCity(city2);
        if (city1.getGrid() == city2.getGrid()) {
            TranslateTransition animation = new TranslateTransition(Duration.millis(1500), p.getPiece());
            animation.setFromX(city1.getX() - city2.getX());
            animation.setFromY(city1.getY() - city2.getY());
            animation.setToX(0);
            animation.setToY(0);
            animation.play();
        }
        playerPiece.setLayoutX(((float) city2.getX() - 5));
        playerPiece.setLayoutY(((float) city2.getY() - 15));
        drawLines(city2);
        switch (city2.getGrid()) {
            case 1:
                changeGrid(JTEGridState.ONE_STATE);
                break;
            case 2:
                changeGrid(JTEGridState.TWO_STATE);
                break;
            case 3:
                changeGrid(JTEGridState.THREE_STATE);
                break;
            case 4:
                changeGrid(JTEGridState.FOUR_STATE);
                break;
        }
    }

    /**
     * Initialize the history screen
     */
    public void initHistoryScreen() {
        //Make the frame
        historyFrame = new StackPane();
        historyFrame.setPrefHeight(775);

        //Make the return button
        returnFromHistoryButton = new Button();
        returnFromHistoryButton.setText("Return");
        returnFromHistoryButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(returnToState);
            }

        });

        //Load the history page
        historyView = new WebView();
        historyEngine = historyView.getEngine();
        historyEngine.loadContent("<h1> Game History </h1>");
        historyFrame.getChildren().add(historyView);

        //Now add the components
        historyPane = new VBox();
        historyPane.getChildren().addAll(returnFromHistoryButton, historyFrame);
    }

    /**
     * Initialize the about screen
     */
    public void initAboutScreen() {

        //Make the frame
        webFrame = new StackPane();
        webFrame.setPrefHeight(775);

        //Make the return button
        returnFromAboutButton = new Button();
        returnFromAboutButton.setText("Return");
        returnFromAboutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(returnToState);
            }

        });

        //Load the web page
        aboutWebView = new WebView();
        aboutWebEngine = aboutWebView.getEngine();
        aboutWebEngine.load("http://en.wikipedia.org/wiki/Journey_Through_Europe");
        webFrame.getChildren().add(aboutWebView);

        //Now add the components
        aboutPane = new VBox();
        aboutPane.getChildren().addAll(returnFromAboutButton, webFrame);
    }

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    public void changeScreen(JTEUIState uiScreen) {

        mainPane.getChildren().clear();
        mainPane.setStyle("-fx-background: #4682B4;");
        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                mainPane.setCenter(splashPane);
                break;
            case PLAY_GAME_STATE:
                mainPane.setCenter(gamePane);
                break;
            case SELECT_PLAYER_STATE:
                mainPane.setCenter(selectPlayerPane);
                break;
            case VIEW_HISTORY_STATE:
                mainPane.setStyle("-fx-background: white;");
                returnToState = currentState;
                mainPane.setCenter(historyPane);
                break;
            case ABOUT_GAME_STATE:
                mainPane.setStyle("-fx-background: white;");
                returnToState = currentState;
                mainPane.setCenter(aboutPane);
                break;
        }
        currentState = uiScreen;
    }
}
