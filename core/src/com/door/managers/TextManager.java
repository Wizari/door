package com.door.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class TextManager {

    static BitmapFont font; //мы будем отображать текст на экране
    //используя эту переменную

    //Текст, относящийся к разным состояниям
    static String start = "Select a door";
    static StringBuffer confirm;
    static String win = "You Win!";
    static String lose = "You Lose!";
    //высоты и ширина области просмотра
    static float width,height;

    public static void initialize(float width,float height){

        TextManager.width = width;
        TextManager.height= height;
        //установим цвет шрифта - cyan
        font = new BitmapFont();
        font.setColor(Color.CYAN);
        //масштабируем размер шрифта в соответствии с шириной экрана
        font.getData().setScale(width/800f);

        confirm = new StringBuffer( (String) "You selected door no.Do you want to switch or stay?");
    }

    public static void displayMessage(SpriteBatch batch){

        GlyphLayout glyphLayout = new GlyphLayout(); //используем класс GlyphLayout

        switch(GameManager.level){
            case START:
                //вычисления для определения позиции сообщения на экране
                glyphLayout.setText(font, start);
                font.draw(batch, glyphLayout, (width/2 - glyphLayout.width/2), GameManager.doors.first().closeSprite.getY()/2 + glyphLayout.height/2);
                break;
            case CONFIRM:
                glyphLayout.setText(font, confirm);
                font.draw(batch, glyphLayout, (width/2 - glyphLayout.width/2), GameManager.doors.first().closeSprite.getY()/2 + glyphLayout.height/2);
                break;
            case END:
                // отображаем победный/проигрышный вариант текста в зависимости от значения переменной hasWon
                if(GameManager.hasWon) {
                    glyphLayout.setText(font, win);
                    font.draw(batch, glyphLayout, (width/2 - glyphLayout.width/2), GameManager.doors.first().closeSprite.getY()/2 + glyphLayout.height/2);
                }
                else {
                    glyphLayout.setText(font, lose);
                    font.draw(batch, glyphLayout, (width/2 - glyphLayout.width/2), GameManager.doors.first().closeSprite.getY()/2 + glyphLayout.height/2);
                }
                break;
        }
    }

    public static void setSelectedDoor(int doorIndex){
        //вставляем номер выбранной пользователем двери в текст строки confirm
        confirm.insert(confirm.indexOf("door no")+ "door no".length(), " "+(doorIndex+1));
    }
}
