package net.somta.juggle.console.infrastructure.mapper;

import net.somta.core.base.IBaseMapper;
import net.somta.juggle.console.infrastructure.po.MockPO;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;

import java.util.List;

public interface MockMapper  extends IBaseMapper {

    List<MockPO> pageQuery(MockQueryParam param);
}
