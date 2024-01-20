package agh.ics.oop.presenter.util;

import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.IWorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

import static agh.ics.oop.model.util.MathUtil.getColorGradient;

public class WorldElementBoxFactory {
    private static final HashMap<String, Image> imagesHashMap = new HashMap<>();
    private static Image getImage(IWorldElement element) {
        String url = "textures/" + element.getResourceName();
        if(!imagesHashMap.containsKey(url)){
            imagesHashMap.put(url, new Image(url));
        }

        return imagesHashMap.get(url);
    }

    private static Node getSquare(int size, Color color){
        return new Rectangle(size, size, color);
    }

    public static Node getWorldElementBox(IWorldElement element, int size){
        if(size < 20)
            return getSquare(size, element.getColor());

        ImageView imageView = new ImageView(getImage(element));
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);

        return imageView;
    }

    public static Node getAnimalBox(Animal animal, int size, boolean tracked){
        if(size < 20)
            return getSquare(size, animal.getColor());

        VBox vbox = new VBox();
        vbox.setMaxWidth(size);
        vbox.setMaxHeight(size);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(1);

        ImageView imageView = new ImageView(getImage(animal));
        imageView.setFitHeight(0.8*size);
        imageView.setFitWidth(0.8*size);

        Rectangle rect = new Rectangle();

        rect.setHeight(0.1*size);
        rect.setWidth(0.8*size*animal.getHealth());

        rect.setArcHeight(0.1*size);
        rect.setArcWidth(0.8*size*animal.getHealth()*0.3);
        rect.setFill(getColorGradient((float)animal.getHealth(), Color.RED, Color.LIME));

        vbox.getChildren().add(imageView);
        vbox.getChildren().add(rect);

        vbox.getStyleClass().add("animal-node");
        if(tracked){
            vbox.getStyleClass().add("tracked-animal");
        }

        return vbox;
    }
}
