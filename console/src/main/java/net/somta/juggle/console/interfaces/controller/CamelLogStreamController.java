package net.somta.juggle.console.interfaces.controller;

import net.somta.juggle.console.application.sse.SseManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/camelLogStream")
public class CamelLogStreamController {
    @Resource
    private SseManager sseManager;
    private static final Logger log = LoggerFactory.getLogger(CamelLogStreamController.class);

//    private static final List<SseEmitter> emitters = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(20);  // 使用固定大小的线程池
    // todo: 这里写死了路径，需要改成相对路径去取
    private final String logFilePath = "D:\\workspace\\logs\\lowcode-integration.log";

//    @PostConstruct
//    public void init() {
//        executor.submit(() -> {
//            try {
//                watchLogFile();
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    @GetMapping(value = "/logStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLog(HttpServletRequest request) {
        String debugConnId = request.getParameter("debugConnId");
        if (StringUtils.isEmpty(debugConnId)) {
            throw new RuntimeException("debugConnId is empty");
        }

//        System.out.println("-------------------streamLog");
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseManager.addSseEmitter(debugConnId, emitter);

        return emitter;
    }
//
//    private void watchLogFile() throws IOException, InterruptedException {
//        Path path = Paths.get(logFilePath);
//        // 获取 WatchService 实例
//        WatchService watchService = FileSystems.getDefault().newWatchService();
//
//        // 注册文件监视器，监听文件的修改事件
//        path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
//
//        // 使用 FileChannel 来读取文件
//        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
//            long position = fileChannel.size(); // 记录文件的初始大小
//
//            while (true) {
//                // 等待文件变化事件
//                WatchKey key = watchService.poll(2, TimeUnit.SECONDS);
//                long currentSize = fileChannel.size();
//                if (currentSize > position) {
//                    fileChanged(fileChannel, position);
//                    position = currentSize; // 更新读取位置
//                }
//
//                if (key != null) {
//                    key.reset();
//                    Thread.sleep(1000); // 等待一段时间后继续监控
//                }
//            }
//        }
//    }


//    public void fileChanged(FileChannel fileChannel, long position) throws IOException {
//        // 读取新增的内容
//        fileChannel.position(position); // 设置文件读取指针到上次读取的位置
//        ByteBuffer buffer = ByteBuffer.allocate(1024); // 创建一个缓冲区
//        int bytesRead;
//        StringBuilder sb = new StringBuilder();
//        while ((bytesRead = fileChannel.read(buffer)) != -1) {
//            if (bytesRead == 0) break;
//            buffer.flip(); // 切换为读取模式
//            String chunk = StandardCharsets.UTF_8.decode(buffer).toString();
//            System.out.println(chunk);
//            sb.append(chunk);
//            buffer.clear(); // 清空缓冲区，准备下一次读取
//        }
//        send(sb.toString());
//        sb.setLength(0);
//    }

//    private void send(String data) {
//        String content = data.replace("\n\r", "<br>").replace("\r\n", "<br>")
//                .replace("\n", "<br>").replace("\r", "<br>");
//        List<SseEmitter> snapshots = new ArrayList<>(emitters);
//        for (SseEmitter emitter : snapshots) {
//            try {
//                emitter.send(content);
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        }
//    }
}
