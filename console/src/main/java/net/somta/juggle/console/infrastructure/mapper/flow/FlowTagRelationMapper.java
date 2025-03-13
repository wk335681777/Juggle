package net.somta.juggle.console.infrastructure.mapper.flow;

import net.somta.core.base.IBaseMapper;
import net.somta.juggle.console.domain.flow.definition.vo.FlowTagRelationVo;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagPO;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagRelationPO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowTagRelationMapper extends IBaseMapper {

    void deleteFlowTagByFlowDefinitionId(@Param("flowDefinitionId") Long flowDefinitionId, @Param("appCode") String appCode);

    void deleteFlowTagByFlowTagId(@Param("flowTagId") Long flowTagId, @Param("appCode") String appCode);

    void batchAdd(@Param("flowTagRelationPOList") List<FlowTagRelationPO> flowTagRelationPOList);

    List<FlowTagRelationVo> queryByFlowInstanceIds(@Param("flowDefinitionIdList") List<Long> flowDefinitionIdList, @Param("userId") Long userId);
}
