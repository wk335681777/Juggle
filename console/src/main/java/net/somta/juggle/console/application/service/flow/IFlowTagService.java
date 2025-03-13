package net.somta.juggle.console.application.service.flow;

import net.somta.juggle.console.interfaces.dto.flow.FlowTagDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagTreeDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;

import java.util.List;

public interface IFlowTagService {

    Boolean add(FlowTagDTO entity);

    Boolean delete(Long id, String appCode);

    Boolean update(FlowTagDTO entity);

    FlowTagDTO get(Long id);

    long count(FlowTagQueryParam param);

    List<FlowTagDTO> queryList(FlowTagQueryParam param);

    List<FlowTagTreeDTO> queryTree(FlowTagQueryParam param);
}
