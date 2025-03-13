package net.somta.juggle.console.application.service.monitor.impl;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.PostConstruct;
import net.somta.core.exception.BizException;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.application.service.flow.IDeployMaster;
import net.somta.juggle.console.application.service.flow.IFlowDefinitionService;
import net.somta.juggle.console.application.service.flow.IFlowVersionService;
import net.somta.juggle.console.application.service.monitor.IFlowMonitorService;
import net.somta.juggle.console.domain.monitor.enums.FlowMonitorErrorEnum;
import net.somta.juggle.console.exception.BusinessException;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionInfoDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowVersionDTO;
import net.somta.juggle.console.interfaces.dto.monitor.FlowMonitorDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowVersionPageParam;
import net.somta.juggle.console.interfaces.param.flow.definition.FlowDefinitionPageParam;
import net.somta.juggle.console.interfaces.param.monitor.flowMonitor.FlowMonitorQueryParam;
import net.somta.juggle.core.model.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FlowMonitorServiceImpl implements IFlowMonitorService {
    private static final Logger log = LoggerFactory.getLogger(FlowMonitorServiceImpl.class);

    @Resource
    private IFlowDefinitionService flowDefinitionService;
    @Resource
    private IFlowVersionService flowVersionService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private IDeployMaster deployMaster;
    @Value("${camel.server.port}")
    private String camelServerPort;
    private static final String camelMonitorUrl = "http://${ip}:${port}/hawtio/jolokia/?maxDepth=7&maxCollectionSize=50000&ignoreErrors=true&canonicalNaming=false&mimeType=application/json";

    @PostConstruct
    public void init() {

    }

    @Override
    public List<String> queryWorkerIpList() {
        List<ServerInfo> serverInfoList = deployMaster.getServers();
        return serverInfoList.stream().map(ServerInfo::getIp).collect(Collectors.toList());
    }

    @Override
    public ResponsePaginationDataResult<FlowMonitorDTO> queryPageList(FlowMonitorQueryParam param) {
        Map<String, FlowMonitorDTO> flowMonitorDTOHashMap = new HashMap<>();

        List<FlowMonitorDTO> flowMonitorDTOS = new ArrayList<>();
        long totalCount;

        if ("dev-camel-context".equalsIgnoreCase(param.getContextType())) {
            FlowDefinitionPageParam flowDefinitionPageParam = new FlowDefinitionPageParam();
            flowDefinitionPageParam.setAppCode(param.getAppCode());
            flowDefinitionPageParam.setPageSize(param.getPageSize());
            flowDefinitionPageParam.setPageNum(param.getPageNum());
            flowDefinitionPageParam.setFlowKey(param.getFlowKey());
            flowDefinitionPageParam.setFlowName(param.getFlowName());

            PageInfo<FlowDefinitionInfoDTO> pageInfo = flowDefinitionService.getFlowDefinitionPageList(flowDefinitionPageParam);
            List<FlowDefinitionInfoDTO> flowDefinitionInfoDTOList = pageInfo.getList();
            for (FlowDefinitionInfoDTO flowDefinitionInfoDTO : flowDefinitionInfoDTOList) {
                FlowMonitorDTO flowMonitorDTO = new FlowMonitorDTO();
                BeanUtils.copyProperties(flowDefinitionInfoDTO, flowMonitorDTO);
                flowMonitorDTOHashMap.put(flowDefinitionInfoDTO.getFlowKey(), flowMonitorDTO);
                flowMonitorDTO.setStatus("not deployed");
                flowMonitorDTOS.add(flowMonitorDTO);
            }
            totalCount = pageInfo.getTotal();
        } else {
            FlowVersionPageParam flowVersionPageParam = new FlowVersionPageParam();
            flowVersionPageParam.setAppCode(param.getAppCode());
            flowVersionPageParam.setPageSize(param.getPageSize());
            flowVersionPageParam.setPageNum(param.getPageNum());
            flowVersionPageParam.setFlowKey(param.getFlowKey());
            flowVersionPageParam.setFlowVersionStatus(1);
            PageInfo flowVersionPageInfo = flowVersionService.getFlowVersionPageList(flowVersionPageParam);
            List<FlowVersionDTO> flowDefinitionInfoDTOList = flowVersionPageInfo.getList();
            for (FlowVersionDTO flowDefinitionInfoDTO : flowDefinitionInfoDTOList) {
                FlowMonitorDTO flowMonitorDTO = new FlowMonitorDTO();
                BeanUtils.copyProperties(flowDefinitionInfoDTO, flowMonitorDTO);
                flowMonitorDTOHashMap.put(flowDefinitionInfoDTO.getFlowKey(), flowMonitorDTO);
                flowMonitorDTO.setStatus("not deployed");
                flowMonitorDTOS.add(flowMonitorDTO);
            }
            totalCount = flowVersionPageInfo.getTotal();
        }

        Map jmxData = queryCamelJmx(param.getIp(), param.getAppCode(), param.getContextType());
        Integer status = (Integer) jmxData.get("status");
        if (status == null || !status.equals(200)) {
            log.error(JSONUtil.toJsonStr(jmxData));
            throw new BizException(FlowMonitorErrorEnum.QUERY_CAMEL_JMX_ERROR);
        }

        Map<String, Map<String, Object>> routeMap = (Map) jmxData.get("value");


        for (Map.Entry<String, Map<String, Object>> entry : routeMap.entrySet()) {
            String key = entry.getKey().substring(1, entry.getKey().length() -1);
            Map<String, Object> value = entry.getValue();
            String routeName = key.split(",")[2].split("/")[1];
            String state = (String) value.get("State");
            FlowMonitorDTO flowMonitorDTO = flowMonitorDTOHashMap.get(routeName);
            if (flowMonitorDTO != null) {
                flowMonitorDTO.setStatus(state);
                flowMonitorDTO.setCompleted((Integer) value.get("ExchangesCompleted"));
                flowMonitorDTO.setFailed((Integer) value.get("ExchangesFailed"));
                flowMonitorDTO.setHandled((Integer) value.get("FailuresHandled"));
                flowMonitorDTO.setTotal((Integer) value.get("ExchangesTotal"));
                flowMonitorDTO.setInFlight((Integer) value.get("ExchangesInflight"));
            }
        }

        return ResponsePaginationDataResult.setPaginationDataResult(totalCount, flowMonitorDTOS);
    }

    private Map queryCamelJmx(String ip, String appCode, String contextType) {
        String url = camelMonitorUrl.replace("${ip}", ip).replace("${port}", camelServerPort);
        CamelJmxRequest request = new CamelJmxRequest();
        request.setType("read");
        request.setMbean("org.apache.camel:context=" + contextType + ",type=routes,name=\"" + appCode + "/*\"");
        log.info("request camel jmx: {}", request.getMbean());
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map jmxData = response.getBody();
            return jmxData;
        } catch (ResourceAccessException e) {
            log.error("connect camel jmx error", e);
            throw new BizException(FlowMonitorErrorEnum.CONNECT_CAMEL_JMX_ERROR);
        } catch (Exception e) {
            log.error("query camel jmx error", e);
            throw new BizException(FlowMonitorErrorEnum.QUERY_CAMEL_JMX_ERROR);
        }

    }

    private static class CamelJmxRequest {
        private String type;
        private String mbean;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMbean() {
        return mbean;
    }

    public void setMbean(String mbean) {
        this.mbean = mbean;
    }
}
}
