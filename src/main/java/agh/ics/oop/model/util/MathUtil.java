package agh.ics.oop.model.util;

import javafx.scene.paint.Color;

public class MathUtil {
    public static Color getColorGradient(double percent, Color start, Color end){
        return new Color(
                start.getRed() + percent*(end.getRed() - start.getRed()),
                start.getGreen() + percent*(end.getGreen() - start.getGreen()),
                start.getBlue() + percent*(end.getBlue() - start.getBlue()),
                start.getOpacity() + percent*(end.getOpacity() - start.getOpacity())
        );
    }

    public static double clamp(double x, double a, double b) {
        if (x < a) return a;
        return Math.min(x, b);
    }
}
