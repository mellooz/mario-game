package javafxapplication5;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class MarioGame extends Application {

    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 800;
    private MediaPlayer mediaPlayer;
    // mario intial position
    private double marioX = 0;
    private double marioY = 0;
    private double marioVelocityX = 0;
    private double marioVelocityY = 0;
    private boolean isJumping = false;
    private boolean isOnGround = false;
    private boolean win = false;
    // obstacles intial position
    private double obstacle1X = 300;
    private double obstacle2X = 600;
    private double obstacle3X = 950;
    private double obstacle4X = 1150;
    private double obstacle5X = 1400;
    private final double obstacleVelocity = 0.2; // The velocity of the obstacles
    private final double blockVelocity = 0.2;    // The velocity of the blockes
    private final double groundY = CANVAS_HEIGHT - 10; // The y-coordinate of the ground
    private boolean gameOver = false; // flag to indicate if game is over
    // blocks coordinates
    private final double[][] blocks = {{Math.random()*600, 650, 250, 20}, {700, 550, 180, 20}, {1000, 350,350, 20},
                                       {Math.random()*600, 250, 200, 20}, {Math.random()*600,450, 350, 20}};
   
    private StackPane root;
    private Scene scene,scene2;
    private Canvas canvas;
    private AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Cloud cloud = new Cloud();
        root = new StackPane(canvas,cloud);
        Button btn1 = new Button("START GAME");       
        btn1.setOnAction (e -> primaryStage.setScene(scene));
        btn1.setMaxSize(100,50);
        Image i =new Image("javafxapplication5/intro.jpg");
        ImageView iv = new ImageView(i);
        iv.setFitHeight(800);
        iv.setFitWidth(1200);
        StackPane s = new StackPane(iv ,btn1);
        s.setMinSize(1200, 800);
        root.setMaxWidth(1200);
        root.setMaxHeight(800);
        scene = new Scene(root);
        scene2 = new Scene(s);
        
       //key control pressed
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT && marioX > 0) {
                marioVelocityX = -5;
            } else if (event.getCode() == KeyCode.RIGHT && marioX < CANVAS_WIDTH - 50) {
                marioVelocityX = 5;
            } else if (event.getCode() == KeyCode.SPACE && isOnGround) {
                isJumping = true;
                isOnGround = false;
                marioVelocityY = -20;
            }
        });
       // key control release
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                marioVelocityX = 0;
            }
        });

        gameLoop = new AnimationTimer() {
            @Override //update func
            public void handle(long now) {
                try {
                    update();
                    draw(gc);
            }   catch (Exception ex) {
                    Logger.getLogger(MarioGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        initMediaPlayer(); // initialize the MediaPlayer object
        primaryStage.setTitle("Mario Game");
        primaryStage.setScene(scene);
        primaryStage.setScene(scene2);
        primaryStage.show();

        gameLoop.start();
        mediaPlayer.play(); // start playing the background music
    }

    private void update() {
        
        // Update Mario's position based on velocity
             marioX += 1.3*marioVelocityX;
             marioY += 1.3*marioVelocityY;
  
        // Apply gravity to Mario's vertical velocity
             marioVelocityY += 1.2 ;
        for (double[] block : blocks) {
        block[0] -= blockVelocity;
}

        // Check if Mario collides with any of the blocks
       for (double[] block : blocks) {
       if    (marioX + 40 >= block[0] && marioX <= block[0] + block[2]
         &&   marioY + 40 >= block[1] && marioY <= block[1] + block[3]) {
           
        if    (marioVelocityY > 0 && marioY < block[1]) {
            // Mario is moving downwards and below the top of the block
               marioY = block[1] - 40; // Move Mario just above the block
               marioVelocityY = 0;
               isJumping = false;
               isOnGround= true;
        } else {
            // Mario collides with the block but not from below, move him just above the block
                marioY = block[1] + block[3];
            if (marioVelocityY >= 0) { // Check if Mario is moving downwards or not
                marioVelocityY = 0.1;
                isOnGround = true;
                isJumping = false;
            }
                isJumping = false;
        }
    }
}
        // Check if Mario is on the ground
        if (marioY >= groundY - 40) {
            marioY = groundY - 40;
            marioVelocityY = 0;
            isOnGround = true;
            isJumping = false;
        } 
         // Update obstacle positions
            obstacle1X -= obstacleVelocity;
            obstacle2X -= obstacleVelocity;
            obstacle3X -= obstacleVelocity;
            obstacle4X -= obstacleVelocity;
            obstacle5X -= obstacleVelocity;
    
    // Check if an obstacle has crossed the screen bounds and reposition it accordingly
        if (obstacle1X < -30) {
            obstacle1X = CANVAS_WIDTH + 30;
    }
        if (obstacle2X < -30) {
            obstacle2X = CANVAS_WIDTH + 30;
    }
        if (obstacle3X < -30) {
            obstacle3X = CANVAS_WIDTH + 30;
    }
        if (obstacle4X < -30) {
           obstacle4X = CANVAS_WIDTH + 30;
    }
        if (obstacle5X < -30) {
            obstacle5X = CANVAS_WIDTH + 30;
    }
        for (double[] block : blocks) {
        if (block[0] + block[2] < 0) {
        block[0] = CANVAS_WIDTH;      
    }     
}
        // Check for collision with obstacles
        if ((marioX + 40 > obstacle1X && marioX < obstacle1X + 30 && marioY + 40 > groundY - 80)
         || (marioX + 40 > obstacle2X && marioX < obstacle2X + 30 && marioY + 40 > groundY - 80)
         || (marioX + 40 > obstacle3X && marioX < obstacle3X + 30 && marioY + 40 > groundY - 80)
         || (marioX + 40 > obstacle4X && marioX < obstacle4X + 30 && marioY + 40 > groundY - 80))
        {
            gameOver = true; // set gameOver flag to true upon collision
        }
       else if ((marioX + 40 > obstacle5X && marioX < obstacle5X + 30 && marioY + 100 > groundY - 100))
       {win = true ;}
        
        // Keep Mario within the screen bounds
        if (marioX < 0) {
            marioX = 0;
        }    else if (marioX > CANVAS_WIDTH - 40) {
            marioX = CANVAS_WIDTH - 40;
        }
    }

   private void draw(GraphicsContext gc) throws Exception {
    // Clear the canvas and draw black borders
       if (!gameOver) {
        gc.setFill(Color.BROWN);
     }
       else {
        gc.setFill(Color.WHITE); // set fill color to white when game is over
     }
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill(Color.CORNFLOWERBLUE);
        gc.fillRect(10, 10, CANVAS_WIDTH - 20, CANVAS_HEIGHT - 20);

    // Draw black blocks
       drawBlocks(gc);
      if (!win) {          
     }
      else
     { 
    // Draw background after winning    
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.setFill(Color.WHITE);
        gc.fillRect(10, 10, CANVAS_WIDTH - 20, CANVAS_HEIGHT - 20);
    // Draw "WINNER" message
        gc.setFill(Color.BROWN);
        gc.setFont(javafx.scene.text.Font.font("Arial", 48));
        gc.fillText(" WINNER",  CANVAS_WIDTH / 2 - 110, CANVAS_HEIGHT / 2-90);
    // Draw "AGAIN" button    
        gc.setFill(Color.BLACK);
        gc.fillRect(CANVAS_WIDTH / 2 - 50, CANVAS_HEIGHT / 2 + 60, 100, 40);
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 20));
        gc.fillText("AGAIN", CANVAS_WIDTH / 2 - 32, CANVAS_HEIGHT / 2 + 85);
        
    // Again button mouse action
                 canvas.setOnMouseClicked(event -> {
            if (event.getX() > CANVAS_WIDTH / 2 - 50 && event.getX() < CANVAS_WIDTH / 2 + 50 &&
                event.getY() > CANVAS_HEIGHT / 2 + 60 && event.getY() < CANVAS_HEIGHT / 2 + 100) {
                // Reset game variables and restart game loop
                marioX = 0;
                marioY = 0;
                marioVelocityX = 0;
                marioVelocityY = 0;
                isJumping = false;
                isOnGround = false;
                obstacle1X = 300;
                obstacle2X = 600;
                obstacle3X = 950;
                obstacle4X = 1150;
                obstacle5X = 1400;
                gameOver = false;
                gameLoop.start();
                win= false ;
                // Remove restart button handler
                canvas.setOnMouseClicked(null);
                  
            }
        });
            gameLoop.stop();
                   
        }
            
    if (!gameOver) { // Draw obstacles and Mario if game is not over
        // Draw the obstacle
        gc.setFill(Color.GREEN);
        gc.fillRect(obstacle1X, groundY - 50, 30, 80);
        gc.fillRect(obstacle2X, groundY - 50, 30, 80);
        gc.fillRect(obstacle3X, groundY - 50, 30, 80);
        gc.fillRect(obstacle4X, groundY - 50, 30, 80);
        gc.setFill(Color.YELLOW);
        gc.fillOval(obstacle5X, groundY - 80, 80,80);

        // Draw Mario
        gc.setFill(Color.RED);
        gc.fillRect(marioX, marioY, 40, 40);
        
        // Draw Mario's eyes
        gc.setFill(Color.WHITE);
        gc.fillOval(marioX + 10, marioY + 15, 7, 7);
        gc.fillOval(marioX + 23, marioY + 15, 7, 7);

        // Draw Mario's pupils
        gc.setFill(Color.BLACK);
        gc.fillOval(marioX + 13, marioY + 17, 3, 3);
        gc.fillOval(marioX + 26, marioY + 17, 3, 3);
        
    } else { // Draw "Game Over" message if game is over
        // Draw "Game Over" message
        gc.setFill(Color.RED);
        gc.setFont(javafx.scene.text.Font.font("Arial", 48));
        gc.fillText(" Game Over", CANVAS_WIDTH / 2 - 130, CANVAS_HEIGHT / 2-90);

        // Draw restart button
        gc.setFill(Color.BLACK);
        gc.fillRect(CANVAS_WIDTH / 2 - 50, CANVAS_HEIGHT / 2 + 60, 100, 40);
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 20));
        gc.fillText("Restart", CANVAS_WIDTH / 2 - 32, CANVAS_HEIGHT / 2 + 85);

        // Handle restart button click
        canvas.setOnMouseClicked(event -> {
            if (event.getX() > CANVAS_WIDTH / 2 - 50 && event.getX() < CANVAS_WIDTH / 2 + 50 &&
                event.getY() > CANVAS_HEIGHT / 2 + 60 && event.getY() < CANVAS_HEIGHT / 2 + 100) {
                // Reset game variables and restart game loop
                marioX = 0;
                marioY = 0;
                marioVelocityX = 0;
                marioVelocityY = 0;
                isJumping = false;
                isOnGround = false;
                obstacle1X = 300;
                obstacle2X = 600;
                obstacle3X = 950;
                obstacle4X = 1150;
                obstacle5X = 1400;
                gameOver = false;
                gameLoop.start();

                // Remove restart button handler
                canvas.setOnMouseClicked(null);
            }
        });
    }
}

private void drawBlocks(GraphicsContext gc) { // Draw blocks mario can stand into to avoide obstacles
    if (!gameOver) {
        gc.setFill(Color.BROWN);
        for (double[] block : blocks) {
            gc.fillRect(block[0], block[1], block[2], block[3]);
        }
    }
}
private void initMediaPlayer() { // media player func
    String mediaPath = "C:/Users/user/Documents/NetBeansProjects/JavaFXApplication5/src/sound/GroundTheme.mp3";
    Media media = new Media(new File(mediaPath).toURI().toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // make the music loop
}
public static void main(String[] args) {
    launch(args);
}
}