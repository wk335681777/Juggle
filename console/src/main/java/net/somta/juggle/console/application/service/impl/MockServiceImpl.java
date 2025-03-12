package net.somta.juggle.console.application.service.impl;

import cn.hutool.json.JSONArray;
import net.somta.core.exception.BizException;
import net.somta.core.protocol.ResponsePaginationDataResult;
import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.application.service.IMockService;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.domain.mock.enums.MockErrorEnum;
import net.somta.juggle.console.domain.mock.respository.IMockRepository;
import net.somta.juggle.console.infrastructure.converter.MockConverter;
import net.somta.juggle.console.infrastructure.po.MockPO;
import net.somta.juggle.console.interfaces.dto.MockDTO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;
import net.somta.juggle.console.interfaces.param.mock.MockQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
public class MockServiceImpl implements IMockService {
    @Resource
    private IMockRepository mockRepository;

    @Override
    public Boolean add(MockDTO param) {
        // url路径不能重复
        MockPO po = MockConverter.convert(param);
        MockPO other = mockRepository.getByMockKey(po.getAppCode(), po.getMockKey());
        if (other != null) {
            throw new BizException(MockErrorEnum.PATH_KEY_EXIST);
        }

        po.setCreatedBy(IdentityContext.getIdentity().getUserId());
        po.setUpdatedBy(IdentityContext.getIdentity().getUserId());
        mockRepository.add(po);
        return true;
    }

    @Override
    public Boolean delete(Long id) {
        return mockRepository.delete(id);
    }

    @Override
    public Boolean update(MockDTO param) {
        MockPO mockPO = mockRepository.get(param.getId());
        if (mockPO == null) {
            return false;
        }

        param.setAppCode(mockPO.getAppCode());
        mockPO = MockConverter.convert(param);

        MockPO other = mockRepository.getByMockKey(mockPO.getAppCode(), mockPO.getMockKey());
        if (other != null && !other.getId().equals(mockPO.getId())) {
            throw new BizException(MockErrorEnum.PATH_KEY_EXIST);
        }

        mockPO.setUpdatedBy(IdentityContext.getIdentity().getUserId());
        return mockRepository.update(mockPO);
    }

    @Override
    public MockDTO get(Long id) {
        return MockConverter.convert(mockRepository.get(id));
    }

    @Override
    public ResponsePaginationDataResult<MockDTO> queryPageList(MockQueryParam param) {
        long count = mockRepository.count(param);
        if (count == 0) {
            return ResponsePaginationDataResult.setPaginationDataResult(count, Collections.emptyList());
        }

        List<MockPO> mockPOList = mockRepository.queryList(param);
        List<MockDTO> mockDTOList = MockConverter.convert(mockPOList);

        return ResponsePaginationDataResult.setPaginationDataResult(count, mockDTOList);
    }
}
