/*package com.society.view.Welcome;
import com.society.view.Guard_portal.GuardDashboard;
import com.society.view.Owner_portal.OwnerDashboard;
import com.society.view.Resident_portal.ResidentDashboard;
import com.society.view.Secretary_portal.SecretaryDashboard;

import javafx.application.Application;
import javafx.stage.Stage;
public class Splash extends Application {


//public static Stage stage;
//     ResidentDashboard residentDashboard=new ResidentDashboard();
     

    @Override
     public void start(Stage stage) throws Exception {
        stage.setTitle( "Society360 - Owner Portal" );
        stage.setScene(OwnerDashboard.createScene(stage));
        stage.show();
     }

}*/

   /*@Override
     public void start(Stage stage) throws Exception {
        stage.setTitle( "Society360 - Owner Portal" );
        stage.setScene(residentDashboard.getResidentDashboardScene(stage));
        stage.show();
     }*/



//     public static Stage stage;


// SecretaryDashboard secretaryDashboard = new SecretaryDashboard();   
    // @Override
   //  public void start(Stage stage) throws Exception {

//         stage.setScene(secretaryDashboard.createScene(stage));
//         stage.show();
//     }







  //  stage.setTitle( "Society360 - Owner Portal" );
   //    stage.setScene(OwnerDashboard.createScene(stage));
   //    stage.show();
    
   //  }

package com.society.view.Welcome;
import com.society.view.Welcome.LogInPage;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Splash extends Application {

    // COLORS

    private static final String DARK_BROWN = "#4E342E";
    private static final String BROWN = "#6D4C41";
    private static final String GOLD = "#B8895A";
    private static final String CREAM = "#F8F3EE";
    private static final String LIGHT_BROWN = "#D7CCC8";

    @Override
    public void start(Stage stage) {

        // ROOT
       
        StackPane root = new StackPane();
        // ================= BACKGROUND IMAGE =================

        Image bgImage = new Image(getClass().getResource("/Background-Society360.jpeg").toExternalForm());

        ImageView bgView = new ImageView(bgImage);

        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);

        bgView.fitWidthProperty().bind(
                root.widthProperty()
        );

        bgView.fitHeightProperty().bind(
                root.heightProperty()
        );

        // Background first
        root.getChildren().add(bgView);


        // =====================================================
        // MAIN ANIMATION CONTAINER
        // =====================================================

        StackPane animationPane = new StackPane();

        animationPane.setPrefSize(600, 600);


        // =====================================================
        // 360 CIRCLE
        // =====================================================

        Circle outerCircle = new Circle(145);

        outerCircle.setFill(Color.TRANSPARENT);

        outerCircle.setStroke(Color.web(GOLD));

        outerCircle.setStrokeWidth(5);

        outerCircle.setOpacity(0);


        // =====================================================
        // BUILDING GROUP
        // =====================================================

        StackPane building = createBuilding();

        building.setOpacity(0);

        building.setScaleX(0.5);

        building.setScaleY(0.5);


        // =====================================================
        // SMALL DECORATIVE DOTS
        // =====================================================

        Circle dot1 = createDot(0, -145);
        Circle dot2 = createDot(125, -70);
        Circle dot3 = createDot(120, 80);
        Circle dot4 = createDot(-120, 80);
        Circle dot5 = createDot(-125, -70);


        dot1.setOpacity(0);
        dot2.setOpacity(0);
        dot3.setOpacity(0);
        dot4.setOpacity(0);
        dot5.setOpacity(0);


        // =====================================================
        // LOGO TEXT
        // =====================================================

        Label societyText = new Label("Society");

        societyText.setFont(
                Font.font("Arial", FontWeight.BOLD, 42)
        );

        societyText.setTextFill(
                Color.web(DARK_BROWN)
        );


        Label numberText = new Label("360");

        numberText.setFont(
                Font.font("Arial", FontWeight.BOLD, 42)
        );

        numberText.setTextFill(
                Color.web(DARK_BROWN)
        );


        javafx.scene.layout.HBox logoText =
                new javafx.scene.layout.HBox(
                        0,
                        societyText,
                        numberText
                );

        logoText.setAlignment(Pos.CENTER);

        logoText.setOpacity(0);


        // =====================================================
        // TAGLINE
        // =====================================================

        Label tagline =new Label("Manage. Connect. Simplify.");

        tagline.setFont(
                Font.font("Arial", 16)
        );

        tagline.setTextFill(
                Color.web(DARK_BROWN)
        );

        tagline.setOpacity(0);


        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        animationPane.getChildren().addAll(
                outerCircle,
                dot1,
                dot2,
                dot3,
                dot4,
                dot5,
                building
        );


        VBox completeLogo =new VBox(
                        12,
                        animationPane,
                        logoText,
                        tagline
                );

        completeLogo.setAlignment(Pos.CENTER);


        root.getChildren().add(
                completeLogo
        );


        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =new Scene(
                        root
                );


        stage.setTitle("Society360");

        stage.setScene(scene);

        // Automatically maximize the window
        stage.setMaximized(true);

        stage.show();


        // =====================================================
        // START ANIMATION
        // =====================================================

        animateSplash(
                stage,
                outerCircle,
                building,
                dot1,
                dot2,
                dot3,
                dot4,
                dot5,
                logoText,
                tagline
        );
    }


    // =========================================================
    // BUILDING
    // =========================================================

    private StackPane createBuilding() {

        StackPane building =new StackPane();


        // -----------------------------------------------------
        // MAIN BUILDING
        // -----------------------------------------------------

        Rectangle mainBuilding =new Rectangle(
                        150,
                        230
                );

        mainBuilding.setArcWidth(8);

        mainBuilding.setArcHeight(8);

        mainBuilding.setFill(
                Color.web(DARK_BROWN)
        );


        // -----------------------------------------------------
        // LEFT BUILDING
        // -----------------------------------------------------

        Rectangle leftBuilding =new Rectangle(
                        70,
                        125
                );

        leftBuilding.setFill(
                Color.web(BROWN)
        );

        leftBuilding.setTranslateX(-75);

        leftBuilding.setTranslateY(32);


        // -----------------------------------------------------
        // RIGHT BUILDING
        // -----------------------------------------------------

        Rectangle rightBuilding =new Rectangle(
                        85,
                        155
                );

        rightBuilding.setFill(
                Color.web(BROWN)
        );

        rightBuilding.setTranslateX(75);

        rightBuilding.setTranslateY(32);


        // -----------------------------------------------------
        // WINDOWS
        // -----------------------------------------------------

        javafx.scene.layout.GridPane windows =new javafx.scene.layout.GridPane();

        windows.setHgap(18);

        windows.setVgap(13);

        windows.setAlignment(Pos.CENTER);

        for (int row = 0; row < 4; row++) {

            for (int col = 0; col < 2; col++) {

                Rectangle window =new Rectangle(
                                18,
                                22
                        );

                window.setFill(
                        Color.web("#F6D69A")
                );

                windows.add(
                        window,
                        col,
                        row
                );
            }
        }


        // -----------------------------------------------------
        // DOOR
        // -----------------------------------------------------

        Rectangle door =new Rectangle(
                        35,
                        55
                );

        door.setFill(
                Color.web("#2D1C17")
        );

        door.setTranslateY(72);


        building.getChildren().addAll(
                leftBuilding,
                rightBuilding,
                mainBuilding,
                windows,
                door
        );


        return building;
    }


    // =========================================================
    // DOT
    // =========================================================

    private Circle createDot(
            double x,
            double y
    ) {

        Circle dot =new Circle(6);

        dot.setFill(
                Color.web(GOLD)
        );

        dot.setTranslateX(x);

        dot.setTranslateY(y);

        return dot;
    }


    // =========================================================
    // ANIMATION
    // =========================================================

    private void animateSplash(
            Stage stage,
            Circle circle,
            StackPane building,
            Circle dot1,
            Circle dot2,
            Circle dot3,
            Circle dot4,
            Circle dot5,
            javafx.scene.layout.HBox logoText,
            Label tagline
    ) {


        // =====================================================
        // 1. CIRCLE APPEARS
        // =====================================================

        FadeTransition circleFade =new FadeTransition(
                        Duration.seconds(0.5),
                        circle
                );

        circleFade.setFromValue(0);

        circleFade.setToValue(1);


        // =====================================================
        // 2. BUILDING APPEARS
        // =====================================================

        FadeTransition buildingFade =new FadeTransition(
                        Duration.seconds(0.4),
                        building
                );

        buildingFade.setFromValue(0);

        buildingFade.setToValue(1);


        ScaleTransition buildingScale =new ScaleTransition(
                        Duration.seconds(0.5),
                        building
                );

        buildingScale.setFromX(0.5);

        buildingScale.setFromY(0.5);

        buildingScale.setToX(1);

        buildingScale.setToY(1);


        ParallelTransition buildingAnimation =new ParallelTransition(
                        buildingFade,
                        buildingScale
                );


        // =====================================================
        // 3. 360° CIRCLE ROTATES
        // =====================================================

        RotateTransition rotate =new RotateTransition(
                        Duration.seconds(0.9),
                        circle
                );

        rotate.setByAngle(360);

        // =====================================================
        // 4. DOTS APPEAR
        // =====================================================

        FadeTransition dotFade1 =new FadeTransition(
                        Duration.seconds(0.3),
                        dot1
                );

        dotFade1.setFromValue(0);
        dotFade1.setToValue(1);


        FadeTransition dotFade2 =new FadeTransition(
                        Duration.seconds(0.3),
                        dot2
                );

        dotFade2.setFromValue(0);
        dotFade2.setToValue(1);


        FadeTransition dotFade3 =new FadeTransition(
                        Duration.seconds(0.3),
                        dot3
                );

        dotFade3.setFromValue(0);
        dotFade3.setToValue(1);


        FadeTransition dotFade4 =new FadeTransition(
                        Duration.seconds(0.3),
                        dot4
                );

        dotFade4.setFromValue(0);
        dotFade4.setToValue(1);


        FadeTransition dotFade5 =new FadeTransition(
                        Duration.seconds(0.3),
                        dot5
                );

        dotFade5.setFromValue(0);
        dotFade5.setToValue(1);


        // All dots animate together
        ParallelTransition dots =new ParallelTransition(
                        dotFade1,
                        dotFade2,
                        dotFade3,
                        dotFade4,
                        dotFade5
                );

        // =====================================================
        // 5. TEXT APPEARS
        // =====================================================

        FadeTransition textFade =new FadeTransition(
                        Duration.seconds(0.5),
                        logoText
                );

        textFade.setFromValue(0);

        textFade.setToValue(1);


        // =====================================================
        // 6. TAGLINE APPEARS
        // =====================================================

        FadeTransition taglineFade =new FadeTransition(
                        Duration.seconds(0.4),
                        tagline
                );

        taglineFade.setFromValue(0);

        taglineFade.setToValue(1);


        // =====================================================
        // COMPLETE SEQUENCE
        // =====================================================

        SequentialTransition animation =new SequentialTransition(

                        circleFade,

                        buildingAnimation,

                        rotate,

                        dots,

                        textFade,

                        taglineFade
                );


        animation.setOnFinished(event -> {

            // Wait a little after animation
            PauseTransition pause =new PauseTransition(
                            Duration.seconds(0.5)
                    );


            pause.setOnFinished(e -> {

                // =================================================
                // SPLASH → LOGIN PAGE
                // =================================================

                LogInPage loginPage =new LogInPage();

                stage.setScene(loginPage.createScene(stage));

                stage.show();
            });

            pause.play();
        });

        animation.play();
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        launch(args);
    }
}


