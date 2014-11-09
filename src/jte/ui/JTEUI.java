/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.ui;

import application.Main.JTEPropertyType;
import java.util.ArrayList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import jte.game.JTEGameData;
import jte.game.JTEGameStateManager;
import properties_manager.PropertiesManager;

/**
 *
 * @author Michael
 */
public class JTEUI extends Pane{
    
    public enum JTEUIState {
        SPLASH_SCREEN_STATE, SELECT_PLAYER_STATE, ABOUT_GAME_STATE
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
    
    //aboutScreen
    private VBox aboutPane;
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
        mainPane.setPadding(marginlessInsets);
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

        // GET THE UPDATED TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(JTEPropertyType.GAME_TITLE_TEXT);
        primaryStage.setTitle(title);

        // THEN ADD ALL THE STUFF WE MIGHT NOW USE
        

        // WE'LL START OUT WITH THE GAME SCREEN
        changeScreen(JTEUIState.SELECT_PLAYER_STATE);
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
        
        //Make setPlayerPane
        setPlayerPane = new GridPane();
        renderPlayerSelection(0);
        
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
     * Initialize the about screen
     */
    public void initAboutScreen() {
        
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
        
        //Now add the components
        aboutPane = new VBox();
        aboutPane.getChildren().addAll(returnFromAboutButton, aboutWebView);
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
            case SELECT_PLAYER_STATE:
                mainPane.setCenter(selectPlayerPane);
                break;
            case ABOUT_GAME_STATE:
                returnToState = currentState;
                mainPane.setCenter(aboutPane);
                break;
        }
        currentState = uiScreen;
    }
}
