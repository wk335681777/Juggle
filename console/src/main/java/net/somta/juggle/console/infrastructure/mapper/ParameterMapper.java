package net.somta.juggle.console.infrastructure.mapper;


import net.somta.core.base.IBaseMapper;
import net.somta.juggle.console.domain.flow.FlowParametersInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoParamesVO;
import net.somta.juggle.console.infrastructure.po.ParameterPO;
import net.somta.juggle.console.domain.parameter.vo.ParameterVO;
import net.somta.juggle.console.infrastructure.po.flow.FlowInfoParamesPO;
import net.somta.juggle.console.interfaces.param.flow.FlowInfoSaveParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Gavin
 */
public interface ParameterMapper extends IBaseMapper {


    /**
     * 新增参数
     * @param parameterPo
     * @return
     */
    int addParameter(ParameterPO parameterPo);

    /**
     * 批量新增参数
     * @param parameterList
     * @return
     */
    int batchAddParameter(@Param("parameterList") List<ParameterPO> parameterList);

    /**
     * 根据sourceType和sourceId删除参数
     * @param parameterVo
     * @return
     */
    int deleteParameter(ParameterVO parameterVo);

    /**
     *
     * @param parameterQueryVo
     * @return
     */
    List<ParameterPO> getParameterListByVO(ParameterVO parameterQueryVo);

    int addParameters(FlowParametersInfoAO flowParametersInfoAO);

    FlowParametersInfoAO findById(Long id);

    int deletedParamsById(FlowInfoParamesVO flowInfoParamesVO);

    List<FlowInfoParamesPO> getParameterList(FlowInfoParamesVO flowInfoParamesVO);

}
