/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jte.ui;

import application.Main.JTEPropertyType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
        //initSelectPlayerScreen();
        

        // WE'LL START OUT WITH THE GAME SCREEN
        //changeScreen(JTEUIState.SELECT_PLAYER_STATE);
    }
    
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
    
    public void initSelectPlayerScreen() {
        
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
            case ABOUT_GAME_STATE:
                returnToState = currentState;
                mainPane.setCenter(aboutPane);
                break;
        }
        currentState = uiScreen;
    }
}
