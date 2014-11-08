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
import javafx.stage.Stage;
import jte.game.JTEGameData;
import jte.game.JTEGameStateManager;
import properties_manager.PropertiesManager;

/**
 *
 * @author Michael
 */
public class JTEUI extends Pane{
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
    private Label splashImageLabel;
    
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
        menuBox = new HBox();
        splashPane = new VBox();
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setSpacing(10.0);
        splashImage = loadImage(props.getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        splashImageView = new ImageView(splashImage);
        splashImageLabel = new Label();
        splashImageLabel.setGraphic(splashImageView);
        splashPane.getChildren().add(splashImageLabel);
        
        //Make the play button
        Image playButtonImage = loadImage(props.getProperty(JTEPropertyType.PLAY_IMG_NAME));
        ImageView playButtonView = new ImageView(playButtonImage);
        Button playButton = new Button();
        playButton.setGraphic(playButtonView);
        
        //Make the load button
        Image loadButtonImage = loadImage(props.getProperty(JTEPropertyType.LOAD_IMG_NAME));
        ImageView loadButtonView = new ImageView(loadButtonImage);
        Button loadButton = new Button();
        loadButton.setGraphic(loadButtonView);
        loadButton.setDisable(true);
        
        //Make the about button
        Image aboutButtonImage = loadImage(props.getProperty(JTEPropertyType.ABOUT_IMG_NAME));
        ImageView aboutButtonView = new ImageView(aboutButtonImage);
        Button aboutButton = new Button();
        aboutButton.setGraphic(aboutButtonView);
        
        //Make the exit button
        Image exitButtonImage = loadImage(props.getProperty(JTEPropertyType.EXIT_IMG_NAME));
        ImageView exitButtonView = new ImageView(exitButtonImage);
        Button exitButton = new Button();
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
        
        //Set the mainPane to splashScreen components
        mainPane.setCenter(splashImageView);
        mainPane.setBottom(menuBox);
    }
    
    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
}
