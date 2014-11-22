/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.ui;

import application.Main.JTEPropertyType;
import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import jte.data.City;
import jte.game.JTEGameData;
import jte.game.JTEGameStateManager;
import properties_manager.PropertiesManager;
import xmlparser.XMLParser;

/**
 *
 * @author Michael
 */
public class JTEUI extends Pane{
    
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
    private Stage primaryStage;

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
    private ArrayList<City> cities = new ArrayList<City>();
    private int currenti;
    private ImageView grid1;
    private ImageView grid2;
    private ImageView grid3;
    private ImageView grid4;
    private AnchorPane gridPane;
    private ImageView diceView;
    private BorderPane gamePane;
    private GridPane gridSelector;
    private BorderPane leftGamePane;
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
    
    public Stage getStage()
    {
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
        mainPane.setStyle("-fx-background: #4FC3E3;");
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
                eventHandler.respondToSetPlayerNumber(Integer.parseInt((String)numberPlayerList.getValue()));
            }

        });
        
        //Make the go button
        goButton = new Button();
        goButton.setText("GO");
        goButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToNewGameRequest(JTEUIState.PLAY_GAME_STATE);
            }

        });
        
        //Make setPlayerPane
        setPlayerPane = new GridPane();
        renderPlayerSelection(2);
        
        //Add to numberPlayerPane
        numberPlayerPane.getChildren().addAll(numberPlayerPrompt, numberPlayerList, goButton);
        
        //Add to selectPlayerPane
        selectPlayerPane.getChildren().addAll(numberPlayerPane, setPlayerPane);
    }
    
    public void renderPlayerSelection(int numPlayers)
    {
        setPlayerPane.getChildren().clear(); //clean the previously rendered boxes
        //make the set player boxes
        int row = 0, col = 0; int added = 0;
        
        //draw six boxes
        for(int i = 0; i < 6; i++) {
            
            //playerSelectionBox
            BorderPane playerSelectionBox = new BorderPane();
            playerSelectionBox.setPrefSize(400, 380);
            playerSelectionBox.setStyle("-fx-border-color: black;");
            
            //new row when 3 are in one row already
            if(added >= 3) {
                    row++;
                    col = 0;
                    added = 0;
                }
            
            //display set player options in number of boxes equal to number of players
            if(i < numPlayers) {
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
                RadioButton playerIsPlayer = new RadioButton("Player");
                RadioButton playerIsComputer = new RadioButton("Computer");
                playerIsPlayer.setToggleGroup(group);
                playerIsComputer.setToggleGroup(group);
                setPlayerTypeBox.getChildren().addAll(playerIsPlayer, playerIsComputer);
                
                //setPlayerNameBox
                VBox setPlayerNameBox = new VBox();
                Label namePrompt = new Label("Name");
                TextField playerName = new TextField();
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
        BorderPane gridFrame = new BorderPane();
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
        gridFrame.setCenter(grid1);
        gridPane.getChildren().add(gridFrame);
        gamePane.setCenter(gridPane);
        
        //gamePane left
        leftGamePane = new BorderPane();
        leftGamePane.setPrefSize(300, 800);
        gamePane.setLeft(leftGamePane);
        
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
        buttons.getChildren().addAll(home, about, history, exit);
        rightGamePane.setTop(buttons);
        
        //Dice
        Image dice = loadImage("die_1.jpg");
        diceView = new ImageView(dice);
        diceView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToRollDieRequest();
            }

        });
        
        //GridSelector
        gridSelector = new GridPane();
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
        gridSelector.setStyle("-fx-border-color: black; -fx-border-width: 1px");
        
        //Add the die and gridselector
        VBox dieGridV = new VBox();
        HBox dieGridH = new HBox();
        dieGridV.getChildren().addAll(diceView, gridSelector);
        dieGridV.setAlignment(Pos.CENTER);
        dieGridV.setSpacing(20);
        dieGridH.getChildren().add(dieGridV);
        dieGridH.setAlignment(Pos.CENTER);
        rightGamePane.setCenter(dieGridH);
        
        //Load the city info
        ArrayList<String> cityNames = props.getPropertyOptionsList(JTEPropertyType.CITY_NAMES);
        ArrayList<String> cityX = props.getPropertyOptionsList(JTEPropertyType.CITY_X);
        ArrayList<String> cityY = props.getPropertyOptionsList(JTEPropertyType.CITY_Y);
        ArrayList<String> cityGrid = props.getPropertyOptionsList(JTEPropertyType.CITY_GRID);
        XMLParser.parsexml();
        Iterator it = City.cities.entrySet().iterator();
        for (City value : City.cities.values()) {
            System.out.print(value.getName() + ": ");
            if(value.getLandNeighbors().size() != 0) {
                for(String city: value.getSeaNeighbors()) {
                    System.out.print(city + ", ");
                }
            }
            System.out.print("\n");
        }
        for(int i = 0; i < cityNames.size(); i++) {
            currenti = i;
            //Make the city and add it to list
            City city = city = new City(cityNames.get(i), Float.parseFloat(cityX.get(i)), Float.parseFloat(cityY.get(i)), Integer.parseInt(cityGrid.get(i)));
            cities.add(city);
            
            //Make the city button
            Button cityButton = new Button();
            Tooltip cityInfo = new Tooltip(city.getName() + ": (X, Y) = (" + city.getX() + ", " + city.getY() + ")");
            cityButton.setTooltip(cityInfo);
            Circle circle = new Circle();
            circle.setRadius(10);
            cityButton.setShape(circle);
            cityButton.setLayoutX(city.getX()-10);
            cityButton.setLayoutY(city.getY()-10);
            gridPane.getChildren().add(cityButton);
            
            //When button is clicked, respond accordingly
            cityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    // TODO Auto-generated method stub
                }
            

            });
            
            //When player hovers over city button, change cursor so they know they are in range
            cityButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    // TODO Auto-generated method stub
                    cityButton.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
                }
            

            });
            
            //Button should be transparent
            cityButton.setStyle("-fx-background-color: transparent;");
        }
    }
    
    /**
     * Change Die Picture
     */
    public void changeDie() {
        Integer num = (int)(Math.random()*6+1);
        String dieImagePath = "die_"+num.toString()+".jpg";
        diceView.setImage(loadImage(dieImagePath));
    }
    
    /**
     * Change the grid
     */
    public void changeGrid(JTEGridState gridState) {
        switch(gridState) {
            case ONE_STATE:
                gridPane.getChildren().clear();
                gridPane.getChildren().add(grid1);
                break;
            case TWO_STATE:
                gridPane.getChildren().clear();
                gridPane.getChildren().add(grid2);
                break;
            case THREE_STATE:
                gridPane.getChildren().clear();
                gridPane.getChildren().add(grid3);
                break;
            case FOUR_STATE:
                gridPane.getChildren().clear();
                gridPane.getChildren().add(grid4);
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
        historyEngine.loadContent("<h1> HISTORY GOES HERE </h1>");
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
                returnToState = currentState;
                mainPane.setCenter(historyPane);
                break;
            case ABOUT_GAME_STATE:
                returnToState = currentState;
                mainPane.setCenter(aboutPane);
                break;
        }
        currentState = uiScreen;
    }
}
