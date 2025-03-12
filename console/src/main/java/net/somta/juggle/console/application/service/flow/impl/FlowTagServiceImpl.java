package net.somta.juggle.console.application.service.flow.impl;

import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.application.service.flow.IFlowTagService;
import net.somta.juggle.console.domain.flow.tag.repository.IFlowTagRepository;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagPO;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagDTO;
import net.somta.juggle.console.interfaces.dto.flow.FlowTagTreeDTO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Predicate;

@Component
public class FlowTagServiceImpl implements IFlowTagService {
    @Autowired
    private IFlowTagRepository flowTagRepository;

    @Override
    public Boolean add(FlowTagDTO entity) {
        IdentityContext.getIdentity().getUserId();
        FlowTagPO po = new FlowTagPO();
        BeanUtils.copyProperties(entity, po);
        // todo: 检查code不能重复
        po.setCreatedBy(IdentityContext.getIdentity().getUserId());
        po.setUpdatedBy(IdentityContext.getIdentity().getUserId());

        return flowTagRepository.add(po);
    }

    @Override
    public Boolean delete(Long id, String appCode) {
        return flowTagRepository.delete(id, appCode);
    }

    @Override
    public Boolean update(FlowTagDTO entity) {
        FlowTagPO flowTagPO = flowTagRepository.get(entity.getId());
        if (flowTagPO == null) {
            return false;
        }

        flowTagPO.setName(entity.getName());
        flowTagPO.setUpdatedBy(IdentityContext.getIdentity().getUserId());
        return flowTagRepository.update(flowTagPO);
    }

    @Override
    public FlowTagDTO get(Long id) {
        FlowTagPO flowTagPO = flowTagRepository.get(id);
        if (flowTagPO == null) {
            return null;
        }

        FlowTagDTO flowTagDTO = new FlowTagDTO();
        BeanUtils.copyProperties(flowTagPO, flowTagDTO);
        return flowTagDTO;
    }

    @Override
    public long count(FlowTagQueryParam param) {
        return flowTagRepository.count(param);
    }

    @Override
    public List<FlowTagDTO> queryList(FlowTagQueryParam param) {
        List<FlowTagPO> flowTagPOList = flowTagRepository.queryList(param);
        List<FlowTagDTO> flowTagDTOList = new ArrayList<>();
        for (FlowTagPO flowTagPO : flowTagPOList) {
            FlowTagDTO flowTagDTO = new FlowTagDTO();
            BeanUtils.copyProperties(flowTagPO, flowTagDTO);
            flowTagDTOList.add(flowTagDTO);
        }

        return flowTagDTOList;
    }

    @Override
    public List<FlowTagTreeDTO> queryTree(FlowTagQueryParam param) {
        Assert.hasText(param.getAppCode(), "appCode can't be empty");
        param.setOwnerId(IdentityContext.getIdentity().getUserId());
        List<FlowTagPO> flowTagPOList = flowTagRepository.queryTree(param);

        FlowTagTreeDTO root = getDefaultRoot(param.getAppCode());
        FlowTagTreeDTO person =getDefaultPersonRoot(param.getAppCode());
        Map<String, List<FlowTagTreeDTO>> flowTagPOMap = new HashMap<>();
        for (FlowTagPO flowTagPO : flowTagPOList) {
            FlowTagTreeDTO flowTagTreeDTO = new FlowTagTreeDTO();
            BeanUtils.copyProperties(flowTagPO, flowTagTreeDTO);
            flowTagPOMap.computeIfAbsent(flowTagPO.getParentCode(), k -> new ArrayList<>()).add(flowTagTreeDTO);
        }

        processChildren(root, flowTagPOMap, flowTagTreeDTO -> flowTagTreeDTO.getAccessType().equals("public") );
        processChildren(person, flowTagPOMap, flowTagTreeDTO -> flowTagTreeDTO.getAccessType().equals("private"));

        return Arrays.asList(root, person);
    }

    private void processChildren(FlowTagTreeDTO parent, Map<String, List<FlowTagTreeDTO>> flowTagPOMap, Predicate<FlowTagTreeDTO> predicate) {
        if (!predicate.test(parent)) {
            return;
        }

        List<FlowTagTreeDTO> chidlren = flowTagPOMap.get(parent.getCode());
        if (chidlren == null || chidlren.isEmpty()) {
            return;
        }

        parent.addChildren(chidlren);
        for (FlowTagTreeDTO child : chidlren) {
            processChildren(child, flowTagPOMap, predicate);
        }
    }

    private FlowTagTreeDTO getDefaultRoot(String appCode) {
        FlowTagTreeDTO root = new FlowTagTreeDTO();
        root.setCode("r");
        root.setName("全部");
        root.setPath("");
        root.setId(-1L);
        root.setAppCode(appCode);
        root.setAccessType("public");
        return root;
    }

    private FlowTagTreeDTO getDefaultPersonRoot(String appCode) {
        FlowTagTreeDTO root = new FlowTagTreeDTO();
        root.setCode("p");
        root.setName("我的标签");
        root.setPath("/p");
        root.setId(-2L);
        root.setAppCode(appCode);
        root.setAccessType("private");
        return root;
    }

    private void check

}
