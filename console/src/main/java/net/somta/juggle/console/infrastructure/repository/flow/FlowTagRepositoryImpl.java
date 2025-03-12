package net.somta.juggle.console.infrastructure.repository.flow;

import net.somta.juggle.console.domain.flow.tag.repository.IFlowTagRepository;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowTagMapper;
import net.somta.juggle.console.infrastructure.mapper.flow.FlowTagRelationMapper;
import net.somta.juggle.console.infrastructure.po.flow.FlowTagPO;
import net.somta.juggle.console.interfaces.param.flow.FlowTagQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FlowTagRepositoryImpl implements IFlowTagRepository {
    @Autowired
    private FlowTagMapper flowTagMapper;
    @Autowired
    private FlowTagRelationMapper flowTagRelationMapper;

    @Override
    public Boolean add(FlowTagPO entity) {
        flowTagMapper.add(entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id, String appCode) {
        flowTagMapper.deleteById(id);
        flowTagRelationMapper.deleteFlowTagByFlowTagId(id, appCode);
        return true;
    }

    @Override
    public Boolean update(FlowTagPO entity) {
        flowTagMapper.update(entity);
        return true;
    }

    @Override
    public FlowTagPO get(Long id) {
        return flowTagMapper.queryById(id);
    }

    @Override
    public long count(FlowTagQueryParam param) {
        return flowTagMapper.queryListCount(param);
    }

    @Override
    public List<FlowTagPO> queryList(FlowTagQueryParam param) {
        return flowTagMapper.queryByList(param);
    }

    @Override
    public List<FlowTagPO> queryTree(FlowTagQueryParam param) {
        return flowTagMapper.queryTree(param);
    }
}
