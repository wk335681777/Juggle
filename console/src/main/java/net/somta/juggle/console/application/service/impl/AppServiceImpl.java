package net.somta.juggle.console.application.service.impl;

import net.somta.juggle.console.application.service.IAppService;
import net.somta.juggle.console.domain.app.AppEntity;
import net.somta.juggle.console.domain.app.repository.IAppRepository;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AppServiceImpl implements IAppService {
    @Resource
    private IAppRepository appRepository;


    @Override
    public Boolean add(AppAddParam param) {
        AppEntity appEntity = new AppEntity();
        BeanUtils.copyProperties(param, appEntity);
        return appRepository.add(appEntity);
    }

    @Override
    public Boolean delete(Long id) {
        return appRepository.delete(id);
    }

    @Override
    public Boolean update(AppUpdateParam param) {
        AppEntity appEntity = new AppEntity();
        BeanUtils.copyProperties(param, appEntity);
        return appRepository.update(appEntity);
    }

    @Override
    public AppVO get(Long id) {
        return appRepository.get(id);
    }

    @Override
    public List<AppVO> queryPageList(AppQueryParam param) {
        return appRepository.queryPageList(param);
    }
}
