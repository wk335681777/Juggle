package net.somta.juggle.console.application.service;

import net.somta.juggle.console.domain.app.AppEntity;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.param.app.AppAddParam;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;
import net.somta.juggle.console.interfaces.param.app.AppUpdateParam;

import java.util.List;

public interface IAppService {

    Boolean add(AppAddParam param);

    Boolean delete(Long id);

    Boolean update(AppUpdateParam param);

    AppVO get(Long id);

    List<AppVO> queryPageList(AppQueryParam param);
}
