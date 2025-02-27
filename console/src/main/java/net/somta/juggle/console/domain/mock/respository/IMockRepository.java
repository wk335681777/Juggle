package net.somta.juggle.console.domain.mock.respository;

import net.somta.juggle.console.infrastructure.po.MockPO;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;

import java.util.List;

/**
 * @author husong
 */
public interface IMockRepository {
    Boolean add(MockPO entity);

    Boolean delete(Long id);

    Boolean update(MockPO entity);

    MockPO get(Long id);

    long count(MockQueryParam param);

    MockPO getByMockKey(String appCode, String mockKey);

    List<MockPO> queryList(MockQueryParam param);
}
