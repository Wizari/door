package com.door.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.door.gameobjects.DoorClass;

public class InputManager {

    public static void handleInput(OrthographicCamera camera) {

        // Было ли касание экрана?
        if (Gdx.input.justTouched()) {
            // Получаем координаты касания
            // И устанавливаем значения координат в вектор temp
            GameManager.temp.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            // получаем координаты касания
            // относительно области просмотра нашей "камеры"
            camera.unproject(GameManager.temp);
            float touchX = GameManager.temp.x;
            float touchY = GameManager.temp.y;
            // Выполняем итерацию массива doors и проверяем
            // Было ли выполнено касание по какой-нибудь двери?
            for (int i = 0; i < GameManager.doors.size; i++) {
                DoorClass door = GameManager.doors.get(i);
                // проверка выполнится только для закрытой двери
                if (!door.isOpen) {
                    if (handleDoor(door, touchX,touchY,i)) {
                        break;
                    }
                }
            }
            handleRestart(touchX,touchY);
        }
    }

    public static boolean handleDoor(DoorClass door,float touchX,float touchY,int doorIndex){

        // Проверяем, находятся ли координаты касания экрана
        // в границах позиции двери
        if ((touchX >= door.position.x) && touchX <= (door.position.x + door.width)
                && (touchY >= door.position.y) && touchY <= (door.position.y + door.height)) {

            switch (GameManager.level) {
                case START:
                    //открыть случайную дверь из оставшихся
                    //после того, как пользователь выбрал предполагаемую им дверь
                    GameManager.doors.get(GameManager.getGoatIndices(doorIndex).random()).isOpen = true;
                    //меняем состояние нашей игры на CONFIRM
                    GameManager.level = GameManager.Level.CONFIRM;
                    TextManager.setSelectedDoor(doorIndex);
                    break;

                case CONFIRM:
                    door.isOpen = true; //открыть выбранную дверь
                    if(!door.isGoat){
                        GameManager.hasWon=true;
                    }
                    GameManager.level = GameManager.Level.END;
                    //меняем состояние нашей игры на END
                    break;
            }
            return true;
        }
        return false;
    }
    public static void handleRestart(float touchX,float touchY) {
        // определяем, было ли касание кнопки restart, используя границы спрайта
        if ((touchX >= GameManager.restartSprite.getX()) && touchX <= (GameManager.restartSprite.getX() + GameManager.restartSprite.getWidth()) && (touchY >= GameManager.restartSprite.getY()) && touchY <= (GameManager.restartSprite.getY() + GameManager.restartSprite.getHeight())) {
            GameManager.restartGame();
        }
    }
}
