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
package net.somta.juggle.console.application.service.flow.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.somta.core.helper.JsonSerializeHelper;
import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.common.utils.IpUtils;
import net.somta.juggle.console.application.assembler.flow.IFlowDefinitionAssembler;
import net.somta.juggle.console.application.service.flow.IDeployMaster;
import net.somta.juggle.console.application.service.flow.IFlowDefinitionService;
import net.somta.juggle.console.application.service.flow.IFlowRuntimeService;
import net.somta.juggle.console.domain.flow.definition.FlowDefinitionAO;
import net.somta.juggle.console.domain.flow.definition.repository.IFlowDefinitionRepository;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoQueryVO;
import net.somta.juggle.console.domain.flow.definition.vo.FlowDefinitionInfoVO;
import net.somta.juggle.console.domain.flow.flowinfo.FlowInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.repository.IFlowInfoRepository;
import net.somta.juggle.console.domain.flow.version.vo.FlowVersionQueryVO;
import net.somta.juggle.console.domain.parameter.ParameterEntity;
import net.somta.juggle.console.domain.flow.definition.repository.IVariableInfoRepository;
import net.somta.juggle.console.domain.flow.definition.vo.VariableInfoVO;
import net.somta.juggle.console.infrastructure.po.flow.FlowDefinitionInfoPO;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionExportDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowDefinitionInfoDTO;
import net.somta.juggle.common.param.TriggerDataParam;
import net.somta.juggle.console.interfaces.param.flow.definition.*;
import net.somta.juggle.core.model.Flow;
import net.somta.juggle.core.model.FlowResult;
import net.somta.juggle.core.model.ServerInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author husong
 * @since 1.0.0
 */
@Service
public class FlowDefinitionServiceImpl implements IFlowDefinitionService {

    private final IFlowRuntimeService flowRuntimeService;
    private final IVariableInfoRepository variableInfoRepository;
    private final IFlowInfoRepository flowRepository;
    private final IFlowDefinitionRepository flowDefinitionRepository;
    @Resource
    private IDeployMaster deployMaster;
    @Value("${camel.worker.rest.dev.port}")
    private String camelWorkerRestDevPort;

    public FlowDefinitionServiceImpl(IFlowRuntimeService flowRuntimeService, IVariableInfoRepository variableInfoRepository, IFlowInfoRepository flowRepository, IFlowDefinitionRepository flowDefinitionRepository) {
        this.flowRuntimeService = flowRuntimeService;
        this.variableInfoRepository = variableInfoRepository;
        this.flowRepository = flowRepository;
        this.flowDefinitionRepository = flowDefinitionRepository;
    }

    @Override
    public Boolean addFlowDefinition(FlowDefinitionAddParam flowDefinitionAddParam) {
        FlowDefinitionAO flowDefinitionAo =IFlowDefinitionAssembler.IMPL.paramToAo(flowDefinitionAddParam);

        String flowKey = flowDefinitionAddParam.getFlowKey();
        flowDefinitionAo.setFlowKey(flowKey != null && !flowKey.isEmpty() ? flowKey : flowDefinitionAo.autoFlowKey(flowKey));

        flowDefinitionAo.initDefaultFlowContent(flowDefinitionAddParam.getFlowName());
        flowDefinitionAo.initParameterList(flowDefinitionAddParam.getFlowInputParams(),flowDefinitionAddParam.getFlowOutputParams());

        flowDefinitionRepository.addFlowDefinition(flowDefinitionAo);
        return true;
    }


    @Override
    public Boolean deleteFlowDefinition(Long flowDefinitionId) {
        return flowDefinitionRepository.deleteFlowDefinitionById(flowDefinitionId);
    }

    @Override
    public Boolean updateFlowDefinition(FlowDefinitionUpdateParam flowDefinitionUpdateParam) {
        FlowDefinitionAO flowDefinitionAo =IFlowDefinitionAssembler.IMPL.paramToAo(flowDefinitionUpdateParam);
        flowDefinitionAo.initParameterList(flowDefinitionUpdateParam.getFlowInputParams(),flowDefinitionUpdateParam.getFlowOutputParams());

        return flowDefinitionRepository.updateFlowDefinition(flowDefinitionAo);
    }

    @Override
    public Boolean saveFlowDefinitionContent(FlowDefinitionContentParam flowDefinitionContentParam) {
        FlowDefinitionAO flowDefinitionAo = IFlowDefinitionAssembler.IMPL.paramToAo(flowDefinitionContentParam);
        try {
            flowDefinitionAo.processFlowContent();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return flowDefinitionRepository.saveFlowDefinitionContent(flowDefinitionAo);
    }

    @Override
    public FlowDefinitionAO getFlowDefinitionInfo(Long flowDefinitionId) {
        FlowDefinitionAO flowDefinitionAo = flowDefinitionRepository.queryFlowDefinitionInfo(flowDefinitionId);
        List<VariableInfoVO> variableInfoVoList = variableInfoRepository.queryVariableInfoList(flowDefinitionAo.getId());
        flowDefinitionAo.setVariableInfoList(variableInfoVoList);
        return flowDefinitionAo;
    }

    @Override
    public FlowDefinitionInfoDTO getDebugInfo(Long flowDefinitionId) {
        FlowDefinitionAO flowDefinitionAo = flowDefinitionRepository.queryFlowDefinitionInfo(flowDefinitionId);
        FlowDefinitionInfoDTO dto = IFlowDefinitionAssembler.IMPL.aoToDto(flowDefinitionAo);
        processFirstComponentNode(dto);
        return dto;
    }

    @Override
    public FlowDefinitionAO getFlowDefinitionByKey(String flowKey, String appCode) {
        FlowDefinitionAO flowDefinitionAo = flowDefinitionRepository.queryFlowDefinitionByKey(flowKey, appCode);
        List<VariableInfoVO> variableInfoVoList = variableInfoRepository.queryVariableInfoList(flowDefinitionAo.getId());
        flowDefinitionAo.setVariableInfoList(variableInfoVoList);
        return flowDefinitionAo;
    }

    @Override
    public PageInfo getFlowDefinitionPageList(FlowDefinitionPageParam flowDefinitionPageParam) {
        FlowDefinitionInfoQueryVO flowDefinitionInfoQueryVo = IFlowDefinitionAssembler.IMPL.paramToVo(flowDefinitionPageParam);
        Page<FlowDefinitionInfoDTO> page = PageHelper.startPage(flowDefinitionPageParam.getPageNum(), flowDefinitionPageParam.getPageSize());
        List<FlowDefinitionInfoVO> flowDefinitionList = flowDefinitionRepository.queryFlowDefinitionList(flowDefinitionInfoQueryVo);
        List<FlowDefinitionInfoDTO> flowDefinitionInfoDtoList = IFlowDefinitionAssembler.IMPL.voListToDtoList(flowDefinitionList);
        processFirstComponentNode(flowDefinitionInfoDtoList);
        PageInfo pageInfo = new PageInfo(flowDefinitionInfoDtoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    private void processFirstComponentNode(List<FlowDefinitionInfoDTO> flowDefinitionInfoDTOList) {
        for (FlowDefinitionInfoDTO dto : flowDefinitionInfoDTOList) {
            processFirstComponentNode(dto);
        }
    }

    private void processFirstComponentNode(FlowDefinitionInfoDTO flowDefinitionInfoDTO) {
        List<ServerInfo> serverInfoList = deployMaster.getServers();
        String serverUri;
        if (serverInfoList.isEmpty()) {
            serverUri = "http://localhost:" + camelWorkerRestDevPort;
        } else {
//            ServerInfo serverInfo = serverInfoList.get(new Random().nextInt(serverInfoList.size()));
//            serverUri = serverInfo.getProtocol() + "://" + serverInfo.getIp() + ":" + camelWorkerRestDevPort;

            ServerInfo serverInfo;
            String localIp = IpUtils.getLocalIp();
            Optional<ServerInfo> optional = serverInfoList.stream().filter(r->r.getIp().equalsIgnoreCase(localIp)).findFirst();
            serverInfo = optional.orElseGet(() -> serverInfoList.get(new Random().nextInt(serverInfoList.size())));

            serverUri = serverInfo.getProtocol() + "://" + serverInfo.getIp() + ":" + camelWorkerRestDevPort;
        }

        String flowContent = flowDefinitionInfoDTO.getFlowContent();
        JSONArray jsonArray = new JSONArray(flowContent);
        Map<String, JSONObject> map = new HashMap<>();
        JSONObject startNode = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            map.put(jsonObject.getStr("key"), jsonObject);
            String type = jsonObject.getStr("elementType");
            if ("START".equals(type)) {
                startNode = jsonObject;
            }
        }

        if (startNode != null) {
            JSONArray outgoings = startNode.getJSONArray("outgoings");
            String nextNodeId = outgoings.getStr(0);
            JSONObject nextNode = map.get(nextNodeId);
            if ("NETTY_HTTP".equals(nextNode.getStr("elementType"))) {
                String uri = nextNode.getStr("uri");
                if ("async".equalsIgnoreCase(flowDefinitionInfoDTO.getFlowType())) {
                    uri = "/v0" + uri;
                }

                String fullUri = serverUri + "/" + flowDefinitionInfoDTO.getAppCode() + uri;
                flowDefinitionInfoDTO.setDebugUri(fullUri);
                flowDefinitionInfoDTO.setEnableDebug(true);
            }
        }
    }

    @Override
    public Long createFlowDefinitionByTemplate(FlowDefinitionAO flowDefinitionAo) {
        return flowDefinitionRepository.addFlowDefinition(flowDefinitionAo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deployFlowDefinition(FlowDefinitionDeployParam flowDefinitionDeployParam, FlowDefinitionAO flowDefinitionAo) {
        FlowInfoAO flowInfoAo = new FlowInfoAO();
        flowInfoAo.setAppCode(flowDefinitionAo.getAppCode());
        flowInfoAo.setFlowVersion(flowDefinitionDeployParam.getFlowDeployVersion());
        flowInfoAo.setFlowKey(flowDefinitionAo.getFlowKey());
        flowInfoAo.setFlowName(flowDefinitionAo.getFlowName());
        flowInfoAo.setFlowType(flowDefinitionAo.getFlowType());
        flowInfoAo.setFlowContent(flowDefinitionAo.getFlowContent());
        flowInfoAo.setFlowType(flowDefinitionAo.getFlowType());
        flowInfoAo.setRemark(flowDefinitionAo.getRemark());
        flowInfoAo.setFlowVersionRemark(flowDefinitionDeployParam.getFlowVersionRemark());

        ParameterEntity parameterEntity = flowDefinitionAo.getParameterEntity();
        try {
            String inputParameterString = JsonSerializeHelper.serialize(parameterEntity.getInputParameterSchema());
            flowInfoAo.setInputs(inputParameterString);
            String outputParameterString = JsonSerializeHelper.serialize(parameterEntity.getOutputParameterSchema());
            flowInfoAo.setOutputs(outputParameterString);

            String variablesString = JsonSerializeHelper.serialize(flowDefinitionAo.getFlowRuntimeVariables());
            flowInfoAo.setVariables(variablesString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return flowRepository.deployFlow(flowInfoAo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowResult debugFlow(FlowDefinitionAO flowDefinitionAo, TriggerDataParam triggerData) {
        Flow flow = new Flow();
        flow.setFlowKey(flowDefinitionAo.getFlowKey());
        flow.setFlowName(flowDefinitionAo.getFlowName());
        flow.setFlowContent(flowDefinitionAo.getFlowContent());
        flow.setInputParams(flowDefinitionAo.getParameterEntity().getInputParameterSchema());
        flow.setOutputParams(flowDefinitionAo.getParameterEntity().getOutputParameterSchema());
        flow.setVariables(flowDefinitionAo.getFlowRuntimeVariables());
        return flowRuntimeService.triggerFlow(flow, flowDefinitionAo.getFlowType(),triggerData);
    }

    @Override
    public Boolean copyFlowDefinition(FlowDefinitionCopyParam flowDefinitionCopyParam) {
        return flowDefinitionRepository.copyFlowDefinition(flowDefinitionCopyParam);
        }

    @Override
    public List<FlowDefinitionExportDTO> export(FlowDefinitionExportParam param) {
        return flowDefinitionRepository.batchGetByIds(param.getAppCode(), param.getIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFlowDefinition(String appCode, List<FlowDefinitionExportDTO> flowDefinitionExportDTOList) {
        List<String> flowKeyList = flowDefinitionExportDTOList.stream()
                .map(FlowDefinitionExportDTO::getFlowKey)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(flowKeyList)) {
            return;
        }

        Map<String, FlowDefinitionExportDTO> flowDefinitionExportDTOMap = new HashMap<>();
        for (FlowDefinitionExportDTO flowDefinitionExportDTO : flowDefinitionExportDTOList) {
            flowDefinitionExportDTOMap.put(flowDefinitionExportDTO.getFlowKey(), flowDefinitionExportDTO);
        }

        List<FlowDefinitionInfoPO> flowDefinitionInfoPOList = flowDefinitionRepository.batchGetByFlowKeys(appCode, flowKeyList);
        List<FlowDefinitionInfoPO> updateList = new ArrayList<>();
        List<FlowDefinitionInfoPO> insertList = new ArrayList<>();

        for (FlowDefinitionInfoPO flowDefinitionInfoPO : flowDefinitionInfoPOList) {
            String flowKey = flowDefinitionInfoPO.getFlowKey();
            // update item
            if (flowKeyList.contains(flowKey)) {
                FlowDefinitionExportDTO flowDefinitionExportDTO = flowDefinitionExportDTOMap.get(flowKey);
                flowDefinitionInfoPO.setFlowContent(flowDefinitionExportDTO.getFlowContent());
                flowDefinitionInfoPO.setFlowType(flowDefinitionExportDTO.getFlowType());
                flowDefinitionInfoPO.setFlowName(flowDefinitionExportDTO.getFlowName());
                flowDefinitionInfoPO.setRemark(flowDefinitionExportDTO.getRemark());
                flowDefinitionInfoPO.setCreatedBy(IdentityContext.getIdentity().getUserId());
                flowDefinitionInfoPO.setUpdatedBy(IdentityContext.getIdentity().getUserId());
                updateList.add(flowDefinitionInfoPO);
            }
        }

        List<String> origFlowKeyList = flowDefinitionInfoPOList.stream().map(FlowDefinitionInfoPO::getFlowKey).collect(Collectors.toList());
        for (FlowDefinitionExportDTO flowDefinitionExportDTO : flowDefinitionExportDTOList) {
            String flowKey = flowDefinitionExportDTO.getFlowKey();
            if (origFlowKeyList.contains(flowKey)) {
                continue;
            }

            FlowDefinitionInfoPO newPo = new FlowDefinitionInfoPO();
            newPo.setFlowKey(flowKey);
            newPo.setAppCode(appCode);
            newPo.setFlowContent(flowDefinitionExportDTO.getFlowContent());
            newPo.setFlowType(flowDefinitionExportDTO.getFlowType());
            newPo.setFlowName(flowDefinitionExportDTO.getFlowName());
            newPo.setRemark(flowDefinitionExportDTO.getRemark());
            newPo.setUpdatedBy(IdentityContext.getIdentity().getUserId());
            insertList.add(newPo);
        }

        if (!insertList.isEmpty()) {
            flowDefinitionRepository.batchAdd(insertList);
        }

        if (!updateList.isEmpty()) {
            for (FlowDefinitionInfoPO flowDefinitionInfoPO : updateList) {
                // 不支持多条记录一起更新?
                flowDefinitionRepository.batchUpdate(appCode, Arrays.asList(flowDefinitionInfoPO));
            }
        }
    }

    @Override
    public Boolean draftFlowDefinition(FlowDefinitionDraftParam flowDefinitionDraftParam) {
        FlowVersionQueryVO flowVersionQueryVO = new FlowVersionQueryVO();
        flowVersionQueryVO.setAppCode(flowDefinitionDraftParam.getAppCode());
        flowVersionQueryVO.setFlowKey(flowDefinitionDraftParam.getFlowKey());
        flowVersionQueryVO.setFlowId(flowDefinitionDraftParam.getId());
        return flowDefinitionRepository.draftFlowDefinition(flowVersionQueryVO);
    }

    public static void main(String[] args) {
    }
}
