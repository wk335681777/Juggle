package net.somta.juggle.console.domain.flow.definition.repository;

import net.somta.juggle.console.domain.flow.definition.FlowDefinitionAO;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoQueryVO;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoVO;
import net.somta.juggle.console.infrastructure.po.flow.FlowDefinitionInfoPO;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionExportDTO;
import net.somta.juggle.console.interfaces.param.flow.definition.FlowDefinitionCopyParam;

import java.util.List;

/**
 * @author husong
 */
public interface IFlowDefinitionRepository {
    Long addFlowDefinition(FlowDefinitionAO flowDefinitionAo);

    Boolean deleteFlowDefinitionById(Long flowDefinitionId);

    Boolean updateFlowDefinition(FlowDefinitionAO flowDefinitionAo);

    Boolean copyFlowDefinition(FlowDefinitionCopyParam flowDefinitionCopyParam);

    Boolean saveFlowDefinitionContent(FlowDefinitionAO flowDefinitionAo);

    FlowDefinitionAO queryFlowDefinitionInfo(Long flowDefinitionId);

    FlowDefinitionAO queryFlowDefinitionByKey(String flowKey);

    List<FlowDefinitionInfoVO> queryFlowDefinitionList(FlowDefinitionInfoQueryVO flowDefinitionInfoQueryVo);

    List<FlowDefinitionExportDTO> batchGetByIds(String appCode, List<Long> flowIds);

    List<FlowDefinitionInfoPO> batchGetByFlowKeys(String appCode, List<String> flowKeys);

    void batchAdd(List<FlowDefinitionInfoPO> flowDefinitionInfoPoLit);

    void batchUpdate(String appCode, List<FlowDefinitionInfoPO> flowDefinitionInfoPoLit);
}
