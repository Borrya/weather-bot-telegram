import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //80dd1b2ad24cc5850b3b454d962fad89 -- API нашего токена.
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message +
                "&units=metric&appid=80dd1b2ad24cc5850b3b454d962fad89");

        //читаем содержание файла, лежащего по ссылке url, т.е. наш файл json.
        Scanner in = new Scanner((InputStream)url.getContent());
        String result = "";

        while(in.hasNext()){
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result); //получили файл json в виде объекта
        model.setName(object.getString("name")); //достали оттуда имя города

        JSONObject main = object.getJSONObject("main"); //получили json-объект main из общего json-а
        model.setHumidity(main.getDouble("humidity"));
        model.setTemp(main.getDouble("temp"));

        JSONArray getArray = object.getJSONArray("weather"); // получили json-массив weather из общего json-а
        for(int i = 0; i < getArray.length(); i++){
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String)obj.get("icon"));
            model.setMain((String)obj.get("main"));
        }
        return "City: " + model.getName() + "\n" +
                "Temperature: " + model.getTemp() + "C" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n" +
                "Main: " + model.getMain() + "\n" +
                "http://openweathermap.org/img/wn/" + model.getIcon() + ".png";
    }
}
