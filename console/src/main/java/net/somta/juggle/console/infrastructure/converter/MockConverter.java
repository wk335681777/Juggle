package net.somta.juggle.console.infrastructure.converter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import net.somta.juggle.console.infrastructure.po.MockPO;
import net.somta.juggle.console.interfaces.dto.MockDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MockConverter {
    public static List<MockDTO> convert(List<MockPO> poList) {
        if (poList == null || poList.isEmpty()) {
            return new ArrayList<>();
        }
        List<MockDTO> dtoList = new ArrayList<>();
        for (MockPO po : poList) {
            dtoList.add(convert(po));
        }
        return dtoList;
    }

    public static MockDTO convert(MockPO po) {
        if (po == null) {
            return null;
        }

        MockDTO mockDTO = new MockDTO();
        BeanUtils.copyProperties(po, mockDTO);

        String conditions = po.getConditions();
        List<MockDTO.MockCondition> conditionList = JSONUtil.toList(conditions, MockDTO.MockCondition.class);
        mockDTO.setConditions(conditionList);
        return mockDTO;
    }

    public static MockPO convert(MockDTO mockDTO) {
        MockPO entity = new MockPO();
        BeanUtils.copyProperties(mockDTO, entity);
        List<MockDTO.MockCondition> conditionList = mockDTO.getConditions();
        for (MockDTO.MockCondition condition : conditionList) {
            MockDTO.MockResponse mockResponse = condition.getResponse();
            List<MockDTO.ResponseHeader> responseHeaderList = mockResponse.getResponseHeader();
            Iterator<MockDTO.ResponseHeader> iterator = responseHeaderList.iterator();
            while (iterator.hasNext()) {
                MockDTO.ResponseHeader responseHeader = iterator.next();
                if (StringUtils.isEmpty(responseHeader.getKey())) {
                    // iterator.remove();
                }
            }
        }
        entity.setMockKey(createMockKey(mockDTO.getAppCode(), entity.getUrl()));
        entity.setConditions(new JSONArray(conditionList).toString());

        return entity;
    }

    private static String createMockKey(String appCode, String url) {
        int index = url.indexOf('?');
        String path = url;
        if (index != -1) {
            path = url.substring(0, index);
        }

        return SecureUtil.md5(appCode + "|" + path);
    }
}
