package net.somta.juggle.console.application.service;

import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.dto.MockDTO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;

public interface IMockService {

    Boolean add(MockDTO param);

    Boolean delete(Long id);

    Boolean update(MockDTO param);

    MockDTO get(Long id);

    ResponsePaginationDataResult<MockDTO> queryPageList(MockQueryParam param);
}
