package love.forte.bot.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

public class WeatherQuery {
    private final String KEY = "key=cecc28e842414680900682174892209c";
    private String qWeatherAPI = "https://devapi.qweather.com/v7/weather/now?";
    private String qCityAPI = "https://geoapi.qweather.com/v2/city/lookup?";

    public void queryWeather(String name) {
        System.out.println("请输入城市名字");
        String codeName = "";
        try {
            codeName = URLEncoder.encode(name, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String cityID = getCityId(codeName);

        String info = getInfo(qWeatherAPI + KEY + "&location=" + cityID);

        JSONObject jsonObject = JSON.parseObject(info);
        JSONObject now = (JSONObject) jsonObject.get("now");
        String temp = now.getString("temp");
        String feelTemp = now.getString("feelsLike");
        String text = now.getString("text");
        String windDir = now.getString("windDir");
        String preessure = now.getString("pressure");
        String date = now.getString("obsTime").substring(0, 16);

        System.out.println("当前城市：" + name);
        System.out.println("天气：" + text);
        System.out.println("更新时间：" + date);
        System.out.println("温度：" + temp + "℃");
        System.out.println("体感温度：" + feelTemp + "℃");
        System.out.println("风向：" + windDir);
        System.out.println("气压：" + preessure);

    }


    public String getCityId(String name){
        // 请求位置ID
        String urlString = qCityAPI + KEY + "&location=" + name;
        String info = getInfo(urlString);

        // 解析出ID,并返回
        JSONObject json = JSON.parseObject(info);
        JSONArray locations = (JSONArray)json.get("location");
        JSONObject location = locations.getJSONObject(0);
        String cityCode = location.getString("id");

        return cityCode;
    }

    public String getInfo(String urlString) {
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(5 * 1000);

            InputStream is = urlConnection.getInputStream();
            GZIPInputStream gzipInputStream = new GZIPInputStream(is);
            InputStreamReader reader = new InputStreamReader(gzipInputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = null;

            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
