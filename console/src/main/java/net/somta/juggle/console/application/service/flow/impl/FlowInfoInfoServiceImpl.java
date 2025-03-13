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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.somta.core.exception.BizException;
import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.application.assembler.flow.IFlowInfoAssembler;
import net.somta.juggle.console.domain.flow.FlowParametersInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.FlowInfoAO;
import net.somta.juggle.console.domain.flow.flowinfo.repository.IFlowInfoRepository;
import net.somta.juggle.console.application.service.flow.IFlowInfoService;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoParamesVO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoQueryVO;
import net.somta.juggle.console.domain.flow.flowinfo.vo.FlowInfoVO;
import net.somta.juggle.console.domain.flow.version.enums.FlowVersionStatusEnum;
import net.somta.juggle.console.domain.flow.version.repository.IFlowVersionRepository;
import net.somta.juggle.console.domain.flow.version.view.FlowVersionView;
import net.somta.juggle.console.domain.flow.version.vo.FlowVersionQueryVO;
import net.somta.juggle.console.interfaces.dto.flow.FlowInfoDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowInfoPageParam;
import net.somta.juggle.console.interfaces.param.flow.FlowInfoSaveParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static net.somta.juggle.console.domain.flow.version.enums.FlowVersionErrorEnum.ENABLE_FLOW_NOT_DELETE;

/**
 * @author husong
 * @since 1.0.0
 */
@Service
public class FlowInfoInfoServiceImpl implements IFlowInfoService {

    private final IFlowInfoRepository flowInfoRepository;
    private final IFlowVersionRepository flowVersionRepository;

    public FlowInfoInfoServiceImpl(IFlowInfoRepository flowInfoRepository, IFlowVersionRepository flowVersionRepository) {
        this.flowInfoRepository = flowInfoRepository;
        this.flowVersionRepository = flowVersionRepository;
    }

    @Override
    public Boolean deleteFlowInfo(Long flowId) {
        FlowInfoAO flowInfoAo = flowInfoRepository.queryFlowInfo(flowId);
        FlowVersionQueryVO flowVersionQueryVO = new FlowVersionQueryVO();
        flowVersionQueryVO.setFlowId(flowInfoAo.getId());
        flowVersionQueryVO.setFlowVersionStatus(FlowVersionStatusEnum.ENABLE.getCode());
        List<FlowVersionView> flowVersionViewList = flowVersionRepository.queryFlowVersionList(flowVersionQueryVO);
        if(CollectionUtils.isNotEmpty(flowVersionViewList)){
            throw new BizException(ENABLE_FLOW_NOT_DELETE);
        }
        return flowInfoRepository.deleteFlowInfoAndFlowVersion(flowId);
    }

    @Override
    public PageInfo getFlowInfoPageList(FlowInfoPageParam flowInfoPageParam) {
        FlowInfoQueryVO flowInfoQueryVO = IFlowInfoAssembler.IMPL.paramToVo(flowInfoPageParam);
        Page<FlowInfoDTO> page = PageHelper.startPage(flowInfoPageParam.getPageNum(), flowInfoPageParam.getPageSize());
        List<FlowInfoVO> flowInfoList = flowInfoRepository.queryFlowInfoList(flowInfoQueryVO);
        List<FlowInfoDTO> flowInfoDTOList = IFlowInfoAssembler.IMPL.voListToDtoList(flowInfoList);
        PageInfo pageInfo = new PageInfo(flowInfoDTOList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public Boolean saveParamsFlowInfo(FlowInfoSaveParam flowInfoSaveParam) {
        // 创建 FlowParametersInfoAO 对象
        FlowParametersInfoAO flowParametersInfoAO = new FlowParametersInfoAO();

        // 将 FlowInfoSaveParam 中的 headers 转换为 KeyValuePair 列表
        List<FlowParametersInfoAO.KeyValuePair> keyValuePairs = flowInfoSaveParam.getHeaders().stream()
                .map(entry -> new FlowParametersInfoAO.KeyValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 设置转换后的 headers 和 body
        flowParametersInfoAO.setFlowId(flowInfoSaveParam.getId());
        flowParametersInfoAO.setHeaders(keyValuePairs);
        flowParametersInfoAO.setBody(flowInfoSaveParam.getBody());
        flowParametersInfoAO.setAppCode(flowInfoSaveParam.getAppCode());
        flowParametersInfoAO.setDeleted(flowParametersInfoAO.getDeleted());

        // 设置时间戳和创建/更新用户信息
        flowParametersInfoAO.setCreatedAt(new Date());  // 设置当前时间为创建时间
        flowParametersInfoAO.setUpdatedAt( flowParametersInfoAO.getCreatedAt());  // 设置更新时间为创建时间
        flowParametersInfoAO.setCreatedBy(IdentityContext.getIdentity().getUserId());  // 设置当前用户为创建者
        flowParametersInfoAO.setUpdatedBy(flowParametersInfoAO.getCreatedBy());   // 设置当前用户为更新者

        // 保存参数到数据库
        return flowInfoRepository.saveParamesFlow(flowParametersInfoAO);
    }

    @Override
    public PageInfo getFlowInfoParamsPageList(FlowInfoSaveParam flowInfoSaveParam) {
        FlowInfoParamesVO flowInfoParamesVO = new FlowInfoParamesVO();
        flowInfoParamesVO.setFlowId(flowInfoSaveParam.getId());
        flowInfoParamesVO.setAppCode(flowInfoSaveParam.getAppCode());
        Page<FlowInfoParamesVO> page = PageHelper.startPage(flowInfoSaveParam.getPageNum(), flowInfoSaveParam.getPageSize());
        List<FlowInfoParamesVO> flowInfoList = flowInfoRepository.queryFlowInfoParamsList(flowInfoParamesVO);
        PageInfo pageInfo = new PageInfo(flowInfoList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public Boolean deleteParamInfo(Long id) {
        FlowParametersInfoAO flowParametersInfoAO= flowInfoRepository.findById(id);
        FlowInfoParamesVO flowInfoParamesVO = new FlowInfoParamesVO();
        flowInfoParamesVO.setId(flowParametersInfoAO.getId());
        flowInfoParamesVO.setAppCode(flowParametersInfoAO.getAppCode());
        flowInfoParamesVO.setHeaders(flowInfoParamesVO.getHeaders());
        flowInfoParamesVO.setBody(flowInfoParamesVO.getBody());

        return flowInfoRepository.deletedParamsById(flowInfoParamesVO);
    }
}
