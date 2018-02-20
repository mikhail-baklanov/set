package ru.relex.intertrust.set.client.util;

/**
 * Класс для вспомогательных методов.
 */
public class Utils {

    private Utils() {}

    /**
     * Метод форматирует вывод таймера.
     *
     * @param timeMs время в миллисекундах
     */
    public static String formatTime(long timeMs) {
        timeMs = Math.abs(timeMs)/1000;
        return (timeMs/60 < 10 ? "0" + timeMs/60 : timeMs/60) + ":" + (timeMs%60 < 10 ? "0" + timeMs%60 : timeMs%60);
    }
    public static void changeURL(String room){
        StringBuilder url= new StringBuilder(com.google.gwt.user.client.Window.Location.getHref());
        int i=0;
        for(i=0;i<url.length();i++)
            if(url.charAt(i)=='=') {
            i++;
            break;
        }

        com.google.gwt.user.client.Window.Location.assign(url.substring(0,i)+room);
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
