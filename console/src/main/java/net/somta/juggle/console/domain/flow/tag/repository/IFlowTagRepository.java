package net.somta.juggle.console.domain.flow.tag.repository;

import net.somta.juggle.console.infrastructure.po.flow.FlowTagPO;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagTreeDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;

import java.util.List;

/**
 * @author husong
 */
public interface IFlowTagRepository {
    Boolean add(FlowTagPO entity);

    Boolean delete(Long id, String appCode);

    Boolean update(FlowTagPO entity);

    FlowTagPO get(Long id);

    long count(FlowTagQueryParam param);

    List<FlowTagPO> queryList(FlowTagQueryParam param);

    List<FlowTagPO> queryTree(FlowTagQueryParam param);
}
