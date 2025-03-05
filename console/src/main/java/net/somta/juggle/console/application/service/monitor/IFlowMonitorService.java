package net.somta.juggle.console.application.service.monitor;

import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.interfaces.dto.monitor.FlowMonitorDTO;
import net.somta.juggle.console.interfaces.param.monitor.flowMonitor.FlowMonitorQueryParam;

import java.util.List;

public interface IFlowMonitorService {

    List<String> queryWorkerIpList();

    ResponsePaginationDataResult<FlowMonitorDTO> queryPageList(FlowMonitorQueryParam param);

}
