package net.somta.juggle.console.interfaces.controller.monitor;

import io.swagger.v3.oas.annotations.Operation;
import net.somta.core.protocol.ResponseDataResult;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.application.service.flow.IFlowDefinitionService;
import net.somta.juggle.console.application.service.monitor.IFlowMonitorService;
import net.somta.juggle.console.interfaces.dto.MockDTO;
import net.somta.juggle.console.interfaces.dto.monitor.FlowMonitorDTO;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;
import net.somta.juggle.console.interfaces.param.monitor.flowMonitor.FlowMonitorQueryParam;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import java.util.List;

import static net.somta.juggle.common.constants.ApplicationConstants.JUGGLE_SERVER_VERSION;

@RestController
@RequestMapping(JUGGLE_SERVER_VERSION + "/flowMonitor")
public class FlowMonitorController {

    @Resource
    private IFlowMonitorService flowMonitorService;

    @Operation(summary = "查询server IP列表")
    @GetMapping("/serverIpList")
    public ResponseDataResult<List<String>> get(){
        List<String> serverIpList = flowMonitorService.queryWorkerIpList();
        return ResponseDataResult.setResponseResult(serverIpList);
    }

    @Operation(summary = "查询分页列表")
    @PostMapping("/page")
    public ResponsePaginationDataResult<FlowMonitorDTO> queryPageList(@RequestBody FlowMonitorQueryParam param){
        return flowMonitorService.queryPageList(param);
    }
}
