package net.somta.juggle.console.interfaces.listener;

import cn.hutool.json.JSONObject;
import net.somta.juggle.console.application.sse.SseManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Component
public class RedisMessageSubscriber implements MessageListener {
    @Resource
    private SseManager sseManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(msg);
        String id = jsonObject.get("debugConnId").toString();
        String content = jsonObject.get("content").toString();
//        System.out.println("Message received from channel: " + new String(pattern, StandardCharsets.UTF_8) + " - Message: " + msg);
        sseManager.sendMessage(id, content);
    }
}