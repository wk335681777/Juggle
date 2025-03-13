package net.somta.juggle.console.infrastructure.mapper.flow;

import net.somta.core.base.IBaseMapper;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagPO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;

import java.util.List;

public interface FlowTagMapper extends IBaseMapper {

    List<FlowTagPO> queryTree(FlowTagQueryParam param);


}
