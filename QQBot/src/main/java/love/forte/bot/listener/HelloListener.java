package love.forte.bot.listener;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.event.FriendMessageEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Component
public class HelloListener {

    @Listener
    public void hello(FriendMessageEvent messageEvent) {
        String message = messageEvent.getMessageContent().getPlainText();

        String answer = getAnswer(message);

        messageEvent.replyBlocking(answer);

    }

    public static String getAnswer(String msg) {
        String content;
        try {
            URLConnection urlConnection = new URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + msg).openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.setRequestMethod("GET");

            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                        (connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder bs = new StringBuilder();
                String l;
                while ((l = bufferedReader.readLine()) != null) {
                    bs.append(l).append("\n");
                }
                content = bs.toString();

                JSONObject jsonObject = JSON.parseObject(content);

                content = jsonObject.getString("content");
                content = myReplace(content);
                content = content + "（机器人测试）";
            } else {
                return "网络错误";
            }
        } catch (MalformedURLException e) {
            return "消息未能识别";
        } catch (IOException e) {
            return "消息未能识别";
        } catch (Exception e) {
            return "未知错误";
        }

        return content;
    }
    public static String myReplace (String content) {

        char[] str = content.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < str.length; i ++) {
            if(str[i] == '{') {
                while(i < str.length && str[i] != '}') i ++;
                stringBuilder.append("\n");
            }
            else stringBuilder.append(str[i]);
        }

        return stringBuilder.toString();
    }
}
