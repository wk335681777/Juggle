package net.somta.juggle.console.domain.flow.flowinfo.repository;

import net.somta.juggle.console.domain.flow.FlowParametersInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.FlowInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoParamesVO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoQueryVO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoVO;

import java.util.List;

/**
 * @author husong
 */
public interface IFlowInfoRepository {

    Boolean deleteFlowInfoAndFlowVersion(Long flowId);

    FlowInfoAO queryFlowInfo(Long flowInfoId);

    List<FlowInfoVO> queryFlowInfoList(FlowInfoQueryVO flowInfoQueryVo);

    Boolean deployFlow(FlowInfoAO flowInfoAo);

    Boolean saveParamesFlow(FlowParametersInfoAO flowParametersInfoAO );

   FlowParametersInfoAO findById(Long id);

    Boolean deletedParamsById(FlowInfoParamesVO flowInfoParamesVO);


    List<FlowInfoParamesVO> queryFlowInfoParamsList(FlowInfoParamesVO flowInfoParamesVO);

}
