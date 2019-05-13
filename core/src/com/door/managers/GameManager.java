package com.door.managers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;
import com.door.gameobjects.DoorClass;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameManager {

    static Array<DoorClass> doors ; // массив из трёх дверей
    static Texture doorTexture; // текстура для изображения двери

    static Texture carTexture; // текстура для изображения автомобиля
    static Texture goatTexture; // текстура для изображения козы
    static Vector3 temp = new Vector3(); // временный вектор для хранения входных координат
    static IntArray goatIndices; // массив целых чисел для хранения индексов дверей с козами
    static Sprite restartSprite;
    static Texture restartTexture;
    static final float RESTART_RESIZE_FACTOR = 5500f;
    static Texture backtexture;
    static Sprite backSprite;

    private static final float DOOR_RESIZE_FACTOR = 2500f;
    private static final float DOOR_VERT_POSITION_FACTOR = 3f;
    private static final float DOOR1_HORIZ_POSITION_FACTOR = 7.77f;
    private static final float DOOR2_HORIZ_POSITION_FACTOR = 2.57f;
    private static final float DOOR3_HORIZ_POSITION_FACTOR = 1.52f;
    static float width,height;

    public static enum Level {
        START,
        CONFIRM,
        END
    }
    static Level level;
    static boolean hasWon = false;

    public static void initialize(float width,float height){
        restartTexture = new Texture(Gdx.files.internal("restart.png"));
        restartSprite = new Sprite(restartTexture);
        restartSprite.setSize(restartSprite.getWidth()* width/RESTART_RESIZE_FACTOR,
                restartSprite.getHeight()* width/RESTART_RESIZE_FACTOR);
        restartSprite.setPosition(0 ,0);
        level = Level.START;
        goatIndices = new IntArray();
        GameManager.width = width;
        GameManager.height = height;
        backtexture = new Texture(Gdx.files.internal("background.jpg"));
        backSprite = new Sprite(backtexture);
        backSprite.setSize(width, height);
        backSprite.setPosition(0,0f);
        doorTexture = new Texture(Gdx.files.internal("door_close.png"));
        carTexture = new Texture(Gdx.files.internal("door_open_car.png"));
        goatTexture = new Texture(Gdx.files.internal("door_open_goat.png"));
        initDoors();
        TextManager.initialize(width, height);

    }

    public static void renderGame(SpriteBatch batch){
        backSprite.draw(batch);
        //Отобразить(нарисовать) каждую дверь
        for(DoorClass door : doors)
            door.render(batch);
        TextManager.displayMessage(batch);
        restartSprite.draw(batch);
    }


    public static void dispose() {
        //удалить (уничтожить) текстуру двери, чтобы исключить перегрузку памяти устройства
        doorTexture.dispose();
        carTexture.dispose();
        goatTexture.dispose();
        restartTexture.dispose();
        backtexture.dispose();

    }

    public static void initDoors(){
        doors = new Array<DoorClass>();

        //создать экземляр каждой двери и добавить их в массив doors
        for(int i=0;i<3;i++){
            doors.add(new DoorClass());
        }

        //установить позиции для отображения каждой двери
        doors.get(0).position.set(width/DOOR1_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);
        doors.get(1).position.set(width/DOOR2_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);
        doors.get(2).position.set(width/DOOR3_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);

        for(DoorClass door : doors){
            // создаём для каждой двери нашего массива спрайт для её отображения
            door.closeSprite = new Sprite(doorTexture);
            door.openSprite = new Sprite();
            door.width = door.closeSprite.getWidth()* (width/DOOR_RESIZE_FACTOR);
            door.height = door.closeSprite.getHeight()* (width/DOOR_RESIZE_FACTOR);
            door.closeSprite.setSize(door.width, door.height);
            door.closeSprite.setPosition(door.position.x,door.position.y);

            //установка размеров для открытой двери
            door.openSprite.setSize(door.width, door.height);
            door.openSprite.setPosition(door.position.x, door.position.y);
        }
        //установка текстур для открытых дверей
        doors.get(0).openSprite.setRegion(goatTexture);
        doors.get(0).isGoat= true;
        doors.get(1).openSprite.setRegion(carTexture);
        doors.get(1).isGoat= false;
        doors.get(2).openSprite.setRegion(goatTexture);
        doors.get(2).isGoat= true;
    }

    /* Поиск двери с козой среди оставшихся невыбранных */
    public static IntArray getGoatIndices(int selectedDoorIndex){
        goatIndices.clear(); // удаляем все предыдущие значения из массива
        for(int i=0;i<doors.size;i++){
            // исключаем выбранную пользователем дверь
            if(i!=selectedDoorIndex && doors.get(i).isGoat) {
                goatIndices.add(i);
            }
        }
        return goatIndices;
    }

    public static void restartGame(){
        // перетасуем позиции дверей в массиве doors, используя метод shuffle
        doors.shuffle();
        // переустановим позицию каждой двери
        doors.get(0).position.set(width/DOOR1_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);
        doors.get(1).position.set(width/DOOR2_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);
        doors.get(2).position.set(width/DOOR3_HORIZ_POSITION_FACTOR,height/DOOR_VERT_POSITION_FACTOR);

        for(int i=0;i<GameManager.doors.size;i++){
            GameManager.doors.get(i).isOpen = false;
            // переустановим позициии спрайтов
            GameManager.doors.get(i).closeSprite.setPosition(GameManager.doors.get(i).position.x, GameManager.doors.get(i).position.y);
            GameManager.doors.get(i).openSprite.setPosition(GameManager.doors.get(i).position.x, GameManager.doors.get(i).position.y);
        }

        GameManager.hasWon = false;
        // перезагрузим игру
        GameManager.level = GameManager.level.START;
        TextManager.confirm = new StringBuffer( (String) "You selected door no.Do you want to switch or stay?");
    }

}