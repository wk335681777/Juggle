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

import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.domain.flow.version.FlowVersionAO;
import net.somta.juggle.console.domain.flow.version.repository.IFlowVersionRepository;
import net.somta.juggle.console.domain.flow.version.view.FlowVersionInfoView;
import net.somta.juggle.console.domain.flow.version.view.FlowVersionView;
import net.somta.juggle.console.domain.flow.version.vo.FlowVersionQueryVO;
import net.somta.juggle.console.exception.BusinessException;
import net.somta.juggle.console.infrastructure.converter.flow.IFlowVersionConverter;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowDefinitionMapper;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowVersionMapper;
import net.somta.juggle.console.infrastructure.po.flow.FlowDefinitionInfoPO;
import net.somta.juggle.console.infrastructure.po.flow.FlowVersionPO;
import net.somta.juggle.console.interfaces.param.flow.FlowVersionParam;
import net.somta.juggle.core.model.Flow;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author husong
 * @since 1.0.0
 */
@Repository
public class FlowVersionRepositoryImpl implements IFlowVersionRepository {

    private final FlowVersionMapper flowVersionMapper;
    private final FlowDefinitionMapper flowDefinitionMapper;

    public FlowVersionRepositoryImpl(FlowVersionMapper flowVersionMapper, FlowDefinitionMapper flowDefinitionMapper) {
        this.flowVersionMapper = flowVersionMapper;
        this.flowDefinitionMapper = flowDefinitionMapper;
    }

    @Override
    public void deleteFlowVersionById(Long flowVersionId) {
        FlowVersionPO flowVersionPo = new FlowVersionPO();
        flowVersionPo.setId(flowVersionId);
        flowVersionPo.setDeleted(1);
        flowVersionMapper.update(flowVersionPo);
    }

    @Override
    public Boolean updateFlowVersion(FlowVersionAO flowVersionAo) {
        FlowVersionPO flowVersionPo = IFlowVersionConverter.IMPL.aoToPo(flowVersionAo);
        flowVersionMapper.update(flowVersionPo);
        return true;
    }

    @Override
    public String queryLatestVersion(String flowKey) {
        return flowVersionMapper.queryLatestVersion(flowKey);
    }

    @Override
    public FlowVersionAO getFlowVersionInfo(Long flowVersionId) {
        FlowVersionPO flowVersionPo = flowVersionMapper.queryById(flowVersionId);
        FlowVersionAO flowVersionAo = IFlowVersionConverter.IMPL.poToAo(flowVersionPo);
        return flowVersionAo;
    }

    @Override
    public FlowVersionInfoView queryFlowVersionInfoByKey(String flowKey, String flowVersion) {
        FlowVersionQueryVO flowVersionQueryVo = new FlowVersionQueryVO();
        flowVersionQueryVo.setFlowKey(flowKey);
        flowVersionQueryVo.setFlowVersion(flowVersion);
        FlowVersionInfoView flowVersionInfoView = flowVersionMapper.queryFlowVersionInfoByKey(flowVersionQueryVo);
        return flowVersionInfoView;
    }

    @Override
    public List<FlowVersionView> queryFlowVersionList(FlowVersionQueryVO flowVersionQueryVO) {
        List<FlowVersionView> flowVersionViewList = flowVersionMapper.queryFlowVersionList(flowVersionQueryVO);
        return flowVersionViewList;
    }

    @Override
    public Boolean restoreFlowVersion(FlowVersionParam flowVersionParam) {
        // 从版本表里通过id查找整条数据
        FlowVersionPO flowVersionPO = flowVersionMapper.queryById(flowVersionParam.getFlowVersionId());

        // 还原整条数据
        FlowDefinitionInfoPO restoreFlowDefinition = restoreVersion(flowVersionPO);

        // 根据 flowKey 查询定义表中的数据
        FlowDefinitionInfoPO existingFlowDefinition = flowDefinitionMapper.queryFlowDefinitionByKey(restoreFlowDefinition.getFlowKey(),restoreFlowDefinition.getAppCode());
        System.out.println(existingFlowDefinition);

        if (existingFlowDefinition != null) {
                restoreFlowDefinition.setId(existingFlowDefinition.getId());
                flowDefinitionMapper.update(restoreFlowDefinition);
        }

        return true;
    }

    private FlowDefinitionInfoPO restoreVersion(FlowVersionPO flowVersionPO) {
        // 创建一个新的 FlowDefinitionPO 实例
        FlowDefinitionInfoPO restoreFlowDefinition = new FlowDefinitionInfoPO();

        // 复制 FlowVersionPO 中的相关数据到 FlowDefinitionPO
        restoreFlowDefinition.setFlowType(flowVersionPO.getFlowType());
        restoreFlowDefinition.setFlowName(flowVersionPO.getFlowName());
        restoreFlowDefinition.setFlowKey(flowVersionPO.getFlowKey());
        restoreFlowDefinition.setAppCode(flowVersionPO.getAppCode());
        restoreFlowDefinition.setFlowContent(flowVersionPO.getFlowContent());
        restoreFlowDefinition.setRemark(flowVersionPO.getFlowVersionRemark());


        // 设置时间戳和创建/更新用户信息
        restoreFlowDefinition.setCreatedAt(new Date());  // 设置当前时间为创建时间
        restoreFlowDefinition.setUpdatedAt(restoreFlowDefinition.getCreatedAt());  // 设置更新时间为创建时间
        restoreFlowDefinition.setCreatedBy(IdentityContext.getIdentity().getUserId());  // 设置当前用户为创建者
        restoreFlowDefinition.setUpdatedBy(restoreFlowDefinition.getCreatedBy());  // 设置当前用户为更新者

        // 默认删除状态为0，表示未删除
        restoreFlowDefinition.setDeleted(0);

        return restoreFlowDefinition;
    }


}
