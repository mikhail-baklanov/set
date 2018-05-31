package ru.relex.intertrust.set.client.util;

import com.google.gwt.user.client.Window;

/**
 * Класс для вспомогательных методов.
 */
public class Utils {

    private Utils() {}

    /**
     * Форматирование значения времени из миллисекунд в вид ММ:СС.
     * @param timeMs время в миллисекундах, может быть отрицательным
     */
    public static String formatTime(long timeMs) {
        timeMs = Math.abs(timeMs)/1000;
        long m = timeMs/60;
        long s = timeMs%60;
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    /**
     * Метод выводит сообщения в консоль браузера.
     *
     * @param message сообщение, которое будет напечатано
     */
    public native static void consoleLog(String message) /*-{
        console.log(message);
    }-*/;

    /**
     * Метод выводит переданные объекты в консоль браузера.
     *
     * @param objects объекты, информация о которых будет выведена
     */
    public native static void consoleLog(Object... objects) /*-{
        console.log(objects);
    }-*/;
}
