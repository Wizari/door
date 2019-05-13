package com.door.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class DoorClass {

    public Sprite openSprite; //создаём спрайт для отображения открытой двери
    public Sprite closeSprite; //создаём спрайт для отображения закрытой двери
    public boolean isOpen = false; //Переменная для определения, в каком положении дверь находится в данный момент
    public boolean isGoat = false; // указывает, стоит ли коза за дверью
    public Vector2 position = new Vector2(); // позиция двери
    //размеры двери
    public float height; //высота двери
    public float width; //ширина двери

    public void render(SpriteBatch batch){
        if(isOpen){
            openSprite.draw(batch);
        }
        else{
            closeSprite.draw(batch);
        }
    }
}