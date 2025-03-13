/*
Copyright (C) 2022-2024 husong

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, visit <https://www.gnu.org/licenses/gpl-3.0.html>.
*/
package net.somta.juggle.console.infrastructure.repository.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.somta.core.helper.JsonSerializeHelper;
import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.domain.flow.definition.FlowDefinitionAO;
import net.somta.juggle.console.domain.flow.definition.enums.FlowDefinitionErrorEnum;
import net.somta.juggle.console.domain.flow.definition.enums.VariableTypeEnum;
import net.somta.juggle.console.domain.flow.definition.repository.IFlowDefinitionRepository;
import net.somta.juggle.console.domain.flow.definition.vo.*;
import net.somta.juggle.console.domain.flow.version.view.FlowVersionInfoView;
import net.somta.juggle.console.domain.flow.version.vo.FlowVersionQueryVO;
import net.somta.juggle.console.domain.parameter.ParameterEntity;
import net.somta.juggle.console.domain.parameter.enums.ParameterSourceTypeEnum;
import net.somta.juggle.console.domain.parameter.enums.ParameterTypeEnum;
import net.somta.juggle.console.domain.parameter.repository.IParameterRepository;
import net.somta.juggle.console.domain.parameter.vo.ParameterVO;
import net.somta.juggle.console.exception.BusinessException;
import net.somta.juggle.console.infrastructure.converter.IVariableInfoConverter;
import net.somta.juggle.console.infrastructure.converter.flow.IFlowDefinitionConverter;
import net.somta.juggle.console.infrastructure.mapper.ParameterMapper;
import net.somta.juggle.console.infrastructure.mapper.VariableInfoMapper;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowDefinitionMapper;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowTagRelationMapper;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowVersionMapper;
import net.somta.juggle.console.infrastructure.po.ParameterPO;
import net.somta.juggle.console.infrastructure.po.VariableInfoPO;
import net.somta.juggle.console.infrastructure.po.flow.FlowDefinitionInfoPO;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagRelationPO;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionExportDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagRelationQueryParam;
import net.somta.juggle.console.interfaces.param.flow.definition.FlowDefinitionCopyParam;
import net.somta.juggle.core.enums.VariablePrefixEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author husong
 * @since 1.0.0
 */
@Repository
public class FlowDefinitionRepositoryImpl implements IFlowDefinitionRepository {
    private final static Logger logger = LoggerFactory.getLogger(FlowDefinitionRepositoryImpl.class);

    private final FlowDefinitionMapper flowDefinitionMapper;
    private final ParameterMapper parameterMapper;
    private final VariableInfoMapper variableInfoMapper;
    private final IParameterRepository parameterRepository;
    private final FlowVersionMapper flowVersionMapper;
    @Autowired
    private FlowTagRelationMapper flowTagRelationMapper;

    public FlowDefinitionRepositoryImpl(FlowDefinitionMapper flowDefinitionMapper, ParameterMapper parameterMapper, VariableInfoMapper variableInfoMapper, IParameterRepository parameterRepository, FlowVersionMapper flowVersionMapper) {
        this.flowDefinitionMapper = flowDefinitionMapper;
        this.parameterMapper = parameterMapper;
        this.variableInfoMapper = variableInfoMapper;
        this.parameterRepository = parameterRepository;
        this.flowVersionMapper = flowVersionMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addFlowDefinition(FlowDefinitionAO flowDefinitionAo) {
        validateFlowKey(flowDefinitionAo.getFlowKey(),flowDefinitionAo.getAppCode());
        FlowDefinitionInfoPO flowDefinitionInfoPo = IFlowDefinitionConverter.IMPL.aoToPo(flowDefinitionAo);
        flowDefinitionInfoPo.setCreatedAt(new Date());
        flowDefinitionInfoPo.setCreatedBy(IdentityContext.getIdentity().getUserId());
        Long flowDefinitionId = flowDefinitionMapper.addFlowDefinitionInfo(flowDefinitionInfoPo);

        saveParametersAndVariables(flowDefinitionInfoPo.getId(), flowDefinitionAo);

        flowDefinitionAo.setId(flowDefinitionInfoPo.getId());
        saveFlowTags(flowDefinitionAo);

        return flowDefinitionId;
    }

    /**
     * 校验流程编码的格式和唯一性
     */
    private void validateFlowKey(String flowKey,String appCode) {
        if (flowKey != null && !flowKey.matches("^[a-zA-Z0-9_]+$")) {
            throw new BusinessException(FlowDefinitionErrorEnum.FLOW_KEY_FORMAT_VALIDATOR_ERROR);
        }

        // 校验流程编码唯一性
        FlowDefinitionInfoPO existingFlowDefinition = flowDefinitionMapper.queryFlowDefinitionByKey(flowKey,appCode);
        if (existingFlowDefinition != null) {
            throw new BusinessException(FlowDefinitionErrorEnum.FLOW_KEY_EXIST_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteFlowDefinitionById(Long flowDefinitionId) {
        FlowDefinitionInfoPO oldPO = flowDefinitionMapper.queryById(flowDefinitionId);
        if (oldPO == null) {
            return false;
        }

        FlowDefinitionInfoPO flowDefinitionInfoPo = new FlowDefinitionInfoPO();
        flowDefinitionInfoPo.setId(flowDefinitionId);
        flowDefinitionInfoPo.setDeleted(1);
        flowDefinitionMapper.update(flowDefinitionInfoPo);
        parameterMapper.deleteParameter(new ParameterVO(ParameterSourceTypeEnum.FLOW.getCode(),flowDefinitionId));

        FlowTagRelationQueryParam flowTagRelationQueryParam = new FlowTagRelationQueryParam();
        flowTagRelationQueryParam.setFlowDefinitionId(flowDefinitionId);
        flowTagRelationQueryParam.setAppCode(oldPO.getAppCode());

        List<FlowTagRelationPO> flowTagRelationPOList = flowTagRelationMapper.queryByList(flowTagRelationQueryParam);
        for (FlowTagRelationPO flowTagRelationPO : flowTagRelationPOList) {
            flowTagRelationMapper.deleteById(flowTagRelationPO.getId());
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateFlowDefinition(FlowDefinitionAO flowDefinitionAo) {
        FlowDefinitionInfoPO flowDefinitionInfoPo = IFlowDefinitionConverter.IMPL.aoToPo(flowDefinitionAo);
        flowDefinitionInfoPo.setUpdatedAt(new Date());
        flowDefinitionInfoPo.setUpdatedBy(IdentityContext.getIdentity().getUserId());
        flowDefinitionMapper.update(flowDefinitionInfoPo);
        parameterMapper.deleteParameter(new ParameterVO(ParameterSourceTypeEnum.FLOW.getCode(), flowDefinitionAo.getId()));
        variableInfoMapper.deleteVariableByFlowDefinitionId(new VariableDeleteVO(flowDefinitionAo.getId(),3));
        saveParametersAndVariables(flowDefinitionInfoPo.getId(),flowDefinitionAo);
        saveFlowTags(flowDefinitionAo);
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean copyFlowDefinition(FlowDefinitionCopyParam flowDefinitionCopyParam) {
        // 校验输入参数
        if (flowDefinitionCopyParam == null || flowDefinitionCopyParam.getId() == null) {
            logger.error("FlowDefinitionAO or ID is null");
            return false;
        }

        // 从数据库中检索原始流程定义信息
        FlowDefinitionInfoPO originalFlowDefinition = flowDefinitionMapper.queryById(flowDefinitionCopyParam.getId());
        if (originalFlowDefinition == null) {
            return false;
        }

        // 创建并复制流程定义信息
        FlowDefinitionInfoPO newFlowDefinition = createNewFlowDefinitionFrom(originalFlowDefinition, flowDefinitionCopyParam);

        // 设置新流程定义的创建/更新时间以及创建/更新者
        newFlowDefinition.setCreatedAt(new Date());
        newFlowDefinition.setUpdatedAt(newFlowDefinition.getCreatedAt());

        newFlowDefinition.setCreatedBy(IdentityContext.getIdentity().getUserId());
        newFlowDefinition.setUpdatedBy(newFlowDefinition.getCreatedBy());

        // 插入新的流程定义到数据库
        flowDefinitionMapper.add(newFlowDefinition);

        // 返回操作成功的标志
        return true;
    }
    // 用于创建并复制流程定义信息
    private FlowDefinitionInfoPO createNewFlowDefinitionFrom(FlowDefinitionInfoPO original,FlowDefinitionCopyParam flowDefinitionCopyParam) {
        FlowDefinitionInfoPO newFlowDefinition = new FlowDefinitionInfoPO();

        // 设置新流程名称和备注，如果传入的参数为空则使用原流程的值
        newFlowDefinition.setFlowName(flowDefinitionCopyParam.getFlowName() != null ? flowDefinitionCopyParam.getFlowName() : original.getFlowName());
        newFlowDefinition.setRemark(flowDefinitionCopyParam.getRemark() != null ? flowDefinitionCopyParam.getRemark() : original.getRemark());

        // 复制其他字段
        newFlowDefinition.setFlowContent(original.getFlowContent());
        newFlowDefinition.setFlowKey(original.autoFlowKey());
        newFlowDefinition.setFlowType(original.getFlowType());
        newFlowDefinition.setAppCode(original.getAppCode());

        return newFlowDefinition;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveFlowDefinitionContent(FlowDefinitionAO flowDefinitionAo) {
        FlowDefinitionInfoPO flowDefinitionInfoPo = IFlowDefinitionConverter.IMPL.aoToPo(flowDefinitionAo);
        flowDefinitionMapper.update(flowDefinitionInfoPo);
        variableInfoMapper.deleteVariableByFlowDefinitionId(new VariableDeleteVO(flowDefinitionAo.getId(),null));
        List<VariableInfoPO> variableInfoPoList = IVariableInfoConverter.IMPL.voListToPoList(flowDefinitionAo.getVariableInfoList(),flowDefinitionAo.getId());
        if(CollectionUtils.isNotEmpty(variableInfoPoList)){
            variableInfoMapper.batchAddVariable(variableInfoPoList);
        }
        return true;
    }

    private void saveFlowTags(FlowDefinitionAO flowDefinitionAo) {
        List<Long> flowTagIdList = flowDefinitionAo.getFlowTagIdList();
        // 只能删除自己的标签，这里带有权限控制
        List<FlowTagRelationVo> flowTagRelationVoList = flowTagRelationMapper.queryByFlowInstanceIds(Arrays.asList(flowDefinitionAo.getId()),
                IdentityContext.getIdentity().getUserId());

        List<FlowTagRelationPO> poList = new ArrayList<>();
        for (Long flowTagId : flowTagIdList) {
            FlowTagRelationPO po = new FlowTagRelationPO();
            po.setAppCode(flowDefinitionAo.getAppCode());
            po.setTagId(flowTagId);
            po.setFlowDefinitionId(flowDefinitionAo.getId());
            po.setCreatedBy(IdentityContext.getIdentity().getUserId());
            po.setUpdatedBy(IdentityContext.getIdentity().getUserId());
            poList.add(po);
        }

        for (FlowTagRelationVo flowTagRelationVo : flowTagRelationVoList) {
            flowTagRelationMapper.deleteById(flowTagRelationVo.getId());
        }

        if (!poList.isEmpty()) {
            flowTagRelationMapper.batchAdd(poList);
        }
    }


    @Override
    public FlowDefinitionAO queryFlowDefinitionInfo(Long flowDefinitionId) {
        FlowDefinitionInfoPO flowDefinitionInfoPo = flowDefinitionMapper.queryById(flowDefinitionId);
        FlowDefinitionAO flowDefinitionAo = IFlowDefinitionConverter.IMPL.poToAo(flowDefinitionInfoPo);
        ParameterEntity parameterEntity = parameterRepository.getParameter(new ParameterVO(ParameterSourceTypeEnum.FLOW.getCode(), flowDefinitionId));
        flowDefinitionAo.setParameterEntity(parameterEntity);
        return flowDefinitionAo;
    }


    @Override
    public FlowDefinitionAO queryFlowDefinitionByKey(String flowKey, String appCode) {
        FlowDefinitionInfoPO flowDefinitionInfoPo = flowDefinitionMapper.queryFlowDefinitionByKey(flowKey,appCode);
        FlowDefinitionAO flowDefinitionAo = IFlowDefinitionConverter.IMPL.poToAo(flowDefinitionInfoPo);
        ParameterEntity parameterEntity = parameterRepository.getParameter(new ParameterVO(ParameterSourceTypeEnum.FLOW.getCode(), flowDefinitionInfoPo.getId()));
        flowDefinitionAo.setParameterEntity(parameterEntity);
        return flowDefinitionAo;
    }

    @Override
    public List<FlowDefinitionInfoVO> queryFlowDefinitionList(FlowDefinitionInfoQueryVO flowDefinitionInfoQueryVO) {
        List<FlowDefinitionInfoVO> flowDefinitionInfoVOList = flowDefinitionMapper.queryFlowDefinitionList(flowDefinitionInfoQueryVO);
        List<Long> flowDefinitionIdList = new ArrayList<>();
        Map<Long, FlowDefinitionInfoVO> flowDefinitionInfoVOMap = new HashMap<>();
        for (FlowDefinitionInfoVO flowDefinitionInfoVO : flowDefinitionInfoVOList) {
            flowDefinitionIdList.add(flowDefinitionInfoVO.getId());
            flowDefinitionInfoVOMap.put(flowDefinitionInfoVO.getId(), flowDefinitionInfoVO);
        }

        if (flowDefinitionIdList.isEmpty()) {
            return flowDefinitionInfoVOList;
        }

        List<FlowTagRelationVo> flowTagRelationPOList = flowTagRelationMapper.queryByFlowInstanceIds(flowDefinitionIdList, IdentityContext.getIdentity().getUserId());
        for (FlowTagRelationVo flowTagRelationVo : flowTagRelationPOList) {
            FlowDefinitionInfoVO flowDefinitionInfoVO = flowDefinitionInfoVOMap.get(flowTagRelationVo.getFlowDefinitionId());
            List<Long> flowTagList = flowDefinitionInfoVO.getFlowTagIdList();
            List<String> flowTagNameList = flowDefinitionInfoVO.getFlowTagNameList();
            if (flowTagList == null) {
                flowTagList = new ArrayList<>();
                flowDefinitionInfoVO.setFlowTagIdList(flowTagList);
            }

            if (flowTagNameList == null) {
                flowTagNameList = new ArrayList<>();
                flowDefinitionInfoVO.setFlowTagNameList(flowTagNameList);
            }

            flowTagList.add(flowTagRelationVo.getTagId());
            flowTagNameList.add(flowTagRelationVo.getTagName());
        }

        return flowDefinitionInfoVOList;
    }

    @Override
    public List<FlowDefinitionExportDTO> batchGetByIds(String appCode, List<Long> flowIds) {
        List<FlowDefinitionInfoPO> poList = flowDefinitionMapper.batchGetByIds(appCode, flowIds);
        List<FlowDefinitionExportDTO> dtoList = new ArrayList<>(poList.size());
        for (FlowDefinitionInfoPO po : poList) {
            FlowDefinitionExportDTO dto = new FlowDefinitionExportDTO();
            BeanUtils.copyProperties(po, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<FlowDefinitionInfoPO> batchGetByFlowKeys(String appCode, List<String> flowKeys) {
        return flowDefinitionMapper.batchGetByFlowKeys(appCode, flowKeys);
    }

    @Override
    public void batchAdd(List<FlowDefinitionInfoPO> flowDefinitionInfoPoLit) {
        flowDefinitionMapper.batchAdd(flowDefinitionInfoPoLit);
    }

    @Override
    public void batchUpdate(String appCode, List<FlowDefinitionInfoPO> flowDefinitionInfoPoLit) {
        flowDefinitionMapper.batchUpdate(appCode, flowDefinitionInfoPoLit);
    }

    @Override
    public Boolean draftFlowDefinition(FlowVersionQueryVO flowVersionQueryVO) {

        //查询deleted=0,status=1，排列后返回 ID 最大的一条记录
        FlowVersionInfoView flowVersionInfoView=flowVersionMapper.queryLatestVersionData(flowVersionQueryVO);

        if (flowVersionInfoView == null) {
             return false;
        }

        FlowDefinitionInfoPO newDraftFlowDefinition = draftVersion(flowVersionQueryVO,flowVersionInfoView);

        flowDefinitionMapper.update( newDraftFlowDefinition);


        return true;
    }

    private FlowDefinitionInfoPO draftVersion(FlowVersionQueryVO flowVersionQueryVO,FlowVersionInfoView flowVersionInfoView) {

        FlowDefinitionInfoPO newDraftFlowDefinition = new FlowDefinitionInfoPO();

        newDraftFlowDefinition.setId(flowVersionQueryVO.getFlowId());
        newDraftFlowDefinition.setFlowType(flowVersionInfoView.getFlowType());
        newDraftFlowDefinition.setFlowName(flowVersionInfoView.getFlowName());
        newDraftFlowDefinition.setFlowKey(flowVersionInfoView.getFlowKey());
        newDraftFlowDefinition.setAppCode(flowVersionInfoView.getAppCode());
        newDraftFlowDefinition.setFlowContent(flowVersionInfoView.getFlowContent());

        newDraftFlowDefinition.setCreatedAt(new Date());  // 设置当前时间为创建时间
        newDraftFlowDefinition.setUpdatedAt(newDraftFlowDefinition.getCreatedAt());  // 设置更新时间为创建时间
        newDraftFlowDefinition.setCreatedBy(IdentityContext.getIdentity().getUserId());  // 设置当前用户为创建者
        newDraftFlowDefinition.setUpdatedBy(newDraftFlowDefinition.getCreatedBy());  // 设置当前用户为更新者

        newDraftFlowDefinition.setDeleted(0);

        return newDraftFlowDefinition;
    }


    private void saveParametersAndVariables(Long flowDefinitionId,FlowDefinitionAO flowDefinitionAo){
        List<ParameterPO> parameterPoList = flowDefinitionAo.getParameterEntity().getParameterPoList(flowDefinitionId,ParameterSourceTypeEnum.FLOW.getCode());
        List<VariableInfoPO> variableInfoPoList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(flowDefinitionAo.getVariableInfoList())){
            List<VariableInfoVO> middleVariableList = flowDefinitionAo.getVariableInfoList().stream()
                    .filter(variable -> VariableTypeEnum.MIDDLE_VARIABLE.getCode() == variable.getEnvType())
                    .collect(Collectors.toList());

            VariableInfoPO middleVariableInfoPo;
            for (VariableInfoVO variableInfo :middleVariableList){
                middleVariableInfoPo = new VariableInfoPO();
                middleVariableInfoPo.setFlowDefinitionId(flowDefinitionId);
                middleVariableInfoPo.setEnvKey(variableInfo.getEnvKey());
                middleVariableInfoPo.setEnvName(variableInfo.getEnvName());
                middleVariableInfoPo.setEnvType(VariableTypeEnum.MIDDLE_VARIABLE.getCode());
                try {
                    middleVariableInfoPo.setDataType(JsonSerializeHelper.serialize(variableInfo.getDataType()));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                variableInfoPoList.add(middleVariableInfoPo);
            }
        }

        if(CollectionUtils.isNotEmpty(parameterPoList)){
            parameterPoList.stream().forEach(parameter -> {
                parameter.setSourceId(flowDefinitionId);
                VariableInfoPO variableInfoPo = new VariableInfoPO();
                variableInfoPo.setFlowDefinitionId(flowDefinitionId);
                if(parameter.getParamType() == ParameterTypeEnum.INPUT_PARAM.getCode()){
                    variableInfoPo.setEnvKey(VariablePrefixEnum.INPUT_VARIABLE_PREFIX.getCode() + parameter.getParamKey());
                }else if(parameter.getParamType() == ParameterTypeEnum.OUTPUT_PARAM.getCode()){
                    variableInfoPo.setEnvKey(VariablePrefixEnum.OUTPUT_VARIABLE_PREFIX.getCode() + parameter.getParamKey());
                }else {
                    variableInfoPo.setEnvKey(parameter.getParamKey());
                }
                variableInfoPo.setEnvName(parameter.getParamName());
                variableInfoPo.setEnvType(parameter.getParamType() == ParameterTypeEnum.INPUT_PARAM.getCode() ? VariableTypeEnum.INPUT_PARAM_VARIABLE.getCode() : VariableTypeEnum.OUTPUT_PARAM_VARIABLE.getCode());
                variableInfoPo.setDataType(parameter.getDataType());
                variableInfoPoList.add(variableInfoPo);
            });
            parameterMapper.batchAddParameter(parameterPoList);
        }

        if(CollectionUtils.isNotEmpty(variableInfoPoList)){
            variableInfoMapper.batchAddVariable(variableInfoPoList);
        }
    }
}
