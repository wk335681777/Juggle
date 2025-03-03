package net.somta.juggle.console.domain.flow.definition.repository;

import net.somta.juggle.console.domain.flow.definition.FlowDefinitionAO;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoQueryVO;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoVO;
import net.somta.juggle.console.interfaces.param.flow.definition.FlowDefinitionCopyParam;
import org.springframework.transaction.annotation.Transactional;

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

    FlowDefinitionAO queryFlowDefinitionByKey(String flowKey, String appCode);

    List<FlowDefinitionInfoVO> queryFlowDefinitionList(FlowDefinitionInfoQueryVO flowDefinitionInfoQueryVo);

}
