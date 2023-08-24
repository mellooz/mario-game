package javafxapplication5;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Cloud extends Pane {
    private ImageView imageView; // image view for the bird
    private final Timeline timeline; // timeline for animation

    public Cloud() {
        super();
        Image image = new Image("javafxapplication5/clouds2.png"); // replace with path to your bird image
        ImageView v = new ImageView(image);
        getChildren().addAll(v);

       // set initial position
        v.setTranslateX(-4000);
        setTranslateY(-250);
       // change the photo size 
        setScaleX(0.1);
        setScaleY(0.1);
       
       // create timeline for animation
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> move()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    // move method for animation
    private void move() {
        // update x position by 1 pixel
        setTranslateX(getTranslateX() + 1);
        // check if bird has reached edge of screen
        if (getTranslateX() > getParent().getLayoutBounds().getWidth()) {
            setTranslateX(-imageView.getImage().getWidth());}
    }
}

        

