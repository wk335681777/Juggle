package net.somta.juggle.console.application.service.flow;

import com.github.pagehelper.PageInfo;
import net.somta.juggle.console.interfaces.param.flow.FlowInfoPageParam;
import net.somta.juggle.console.interfaces.param.flow.FlowInfoSaveParam;

/**
 * @author husong
 */
public interface IFlowInfoService {

    /**
     * Delete flow information based on flow ID
     * @param flowId flow id
     * @return Boolean
     */
    Boolean deleteFlowInfo(Long flowId);

    /**
     * Query flow information pagination list
     * @param flowInfoPageParam Flow information pagination query object
     * @return Flow Information Paging Object
     */
    PageInfo getFlowInfoPageList(FlowInfoPageParam flowInfoPageParam);

    Boolean saveParamsFlowInfo(FlowInfoSaveParam flowInfoSaveParam);

    PageInfo getFlowInfoParamsPageList(FlowInfoSaveParam flowInfoSaveParam);

    Boolean deleteParamInfo(Long id);

}
