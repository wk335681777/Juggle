package net.somta.juggle.console.application.sse;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class SseManager {
    private static final Logger log = LoggerFactory.getLogger(SseManager.class);

    private static final Map<String, SseEmitter> EMITTER_MAP = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        EXECUTOR.scheduleAtFixedRate(this::sendHeartBeat, 5, 5, TimeUnit.SECONDS);
    }

    public synchronized void addSseEmitter(String id, SseEmitter emitter) {
        if (EMITTER_MAP.size() > 100) {
            throw new RuntimeException("Too many sse emitters");
        }

        EMITTER_MAP.put(id, emitter);

        emitter.onCompletion(() -> {
            EMITTER_MAP.remove(id);
        });
        emitter.onTimeout(() ->  {
            EMITTER_MAP.remove(id);
        });
        emitter.onError((throwable) -> {
            EMITTER_MAP.remove(id);
        });
    }

    /**
     * 定期发送心跳，清除无用连接
     */
    private void sendHeartBeat() {
        if (!EMITTER_MAP.isEmpty()) {
//            log.info("sendHeartBeat...");
        }

        Map<String, SseEmitter> emitterMap = new HashMap<>(EMITTER_MAP);
        for (SseEmitter emitter : emitterMap.values()) {
            send(emitter, "\n");
        }
    }

    public void sendMessage(String id, String data) {
        SseEmitter sseEmitter = EMITTER_MAP.get(id);
        if (sseEmitter == null) {
            return;
        }

        String content = data.replace("\n\r", "<br>").replace("\r\n", "<br>")
                .replace("\n", "<br>").replace("\r", "<br>");
//        System.out.println(content);
        send(sseEmitter, content);
    }

    private void send(SseEmitter sseEmitter, String data) {
        try {
            sseEmitter.send(data);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
