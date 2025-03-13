package net.somta.juggle.console.infrastructure.repository;

import net.somta.juggle.console.domain.mock.respository.IMockRepository;
import net.somta.juggle.console.infrastructure.mapper.MockMapper;
import net.somta.juggle.console.infrastructure.po.MockPO;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

@Component
public class MockRepositoryImpl implements IMockRepository {
    @Resource
    private MockMapper mockMapper;

    @Override
    public Boolean add(MockPO entity) {
        mockMapper.add(entity);
        return true;
    }

    @Override
    public Boolean delete(Long id) {
        mockMapper.deleteById(id);
        return true;
    }

    @Override
    public Boolean update(MockPO entity) {
        mockMapper.update(entity);
        return true;
    }

    @Override
    public MockPO get(Long id) {
        return mockMapper.queryById(id);
    }

    @Override
    public long count(MockQueryParam param) {
        return mockMapper.queryListCount(param);
    }

    @Override
    public MockPO getByMockKey(String appCode, String mockKey) {
        MockQueryParam param = new MockQueryParam();
        param.setMockKey(mockKey);
        param.setAppCode(appCode);
        List<MockPO> mockPOList = mockMapper.queryByList(param);
        return mockPOList.isEmpty() ? null : mockPOList.get(0);
    }

    @Override
    public List<MockPO> queryList(MockQueryParam param) {
        return mockMapper.queryByList(param);
    }
}
