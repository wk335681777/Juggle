package net.somta.juggle.console.infrastructure.repository;

import net.somta.juggle.common.identity.IdentityContext;
import net.somta.juggle.console.domain.app.AppEntity;
import net.somta.juggle.console.domain.app.repository.IAppRepository;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.infrastructure.mapper.AppMapper;
import net.somta.juggle.console.infrastructure.po.AppPO;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppRespositoryImpl implements IAppRepository {
    @Resource
    private AppMapper appMapper;

    @Override
    public Boolean add(AppEntity entity) {
        AppPO appPO = new AppPO();
        appPO.setAppName(entity.getAppName());
        appPO.setAppCode(entity.getAppCode());
        appPO.setRemark(entity.getRemark());
        appPO.setCreatedBy(IdentityContext.getIdentity().getUserId());
        appPO.setUpdatedBy(IdentityContext.getIdentity().getUserId());

        appMapper.add(appPO);

        return true;
    }

    @Override
    public Boolean delete(Long id) {
        appMapper.deleteById(id);
        return true;
    }

    @Override
    public Boolean update(AppEntity entity) {
        AppPO appPO = new AppPO();
        appPO.setId(entity.getId());
        appPO.setAppName(entity.getAppName());
        appPO.setAppCode(entity.getAppCode());
        appPO.setRemark(entity.getRemark());
        appPO.setCreatedBy(IdentityContext.getIdentity().getUserId());
        appPO.setUpdatedBy(IdentityContext.getIdentity().getUserId());
        appMapper.update(appPO);

        return true;
    }

    @Override
    public AppVO get(Long id) {
        AppPO appPO = appMapper.queryById(id);
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(appPO, appVO);
        return appVO;
    }

    @Override
    public List<AppVO> queryPageList(AppQueryParam queryParam) {
        List<AppPO> appPOList = appMapper.queryByList(queryParam);
        List<AppVO> appVOList = new ArrayList<>();
        for (AppPO appPO : appPOList) {
            AppVO appVO = new AppVO();
            BeanUtils.copyProperties(appPO, appVO);
            appVOList.add(appVO);
        }

        return appVOList;
    }
}
