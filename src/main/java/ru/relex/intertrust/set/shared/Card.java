package ru.relex.intertrust.set.shared;

import java.io.Serializable;

/**
 * Описатель карты.
 */
public class Card implements Serializable {
    // цвет фигур на карте
    private int color;
    // число фигур на карте
    private int shapeCount;
    // вариант заполнения фигур на карте
    private int fill;
    // тип фигур на карте
    private int shape;

    public Card(int color, int shapeCount, int fill, int shape) {
        this.color = color;
        this.shapeCount = shapeCount;
        this.fill = fill;
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    public int getShapeCount() {
        return shapeCount;
    }

    public int getFill() {
        return fill;
    }

    public int getShape() {
        return shape;
    }

    public Card() { }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Card))
            return false;
        Card card = (Card)obj;
        return (card.getColor()==color && card.getFill()==fill && card.getShape()==shape && card.getShapeCount()==shapeCount);
    }
}