package net.somta.juggle.console.application.service.impl;

import net.somta.juggle.common.utils.IpUtils;
import net.somta.juggle.console.application.service.IToolService;
import net.somta.juggle.console.application.service.flow.IDeployMaster;
import net.somta.juggle.console.interfaces.param.tool.FreemarkExecuteParam;
import net.somta.juggle.core.model.CamelResult;
import net.somta.juggle.core.model.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class ToolServiceImpl implements IToolService {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private IDeployMaster deployMaster;
    @Value("${camel.server.port}")
    private String camelServerPort;

    @Override
    public CamelResult freemarkExecute(FreemarkExecuteParam param) {
        ResponseEntity<CamelResult> response = restTemplate.postForEntity(getServerUri() + "/tool/freemarkerExecute", param, CamelResult.class);
        return response.getBody();
    }

    private String getServerUri() {
        List<ServerInfo> serverInfoList = deployMaster.getServers();
        String serverUri;
        if (serverInfoList.isEmpty()) {
            return "http://localhost:" + camelServerPort;
        }

        ServerInfo serverInfo;
        String localIp = IpUtils.getLocalIp();
        Optional<ServerInfo> optional = serverInfoList.stream().filter(r->r.getIp().equalsIgnoreCase(localIp)).findFirst();
        serverInfo = optional.orElseGet(() -> serverInfoList.get(new Random().nextInt(serverInfoList.size())));

        serverUri = serverInfo.getProtocol() + "://" + serverInfo.getIp() + ":" + camelServerPort;
        return serverUri;
    }
}
