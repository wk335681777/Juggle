package net.somta.juggle.console.domain.app.repository;

import net.somta.juggle.console.domain.app.AppEntity;
import net.somta.juggle.console.domain.app.vo.AppVO;
import net.somta.juggle.console.interfaces.param.app.AppQueryParam;

import java.util.List;

/**
 * @author husong
 */
public interface IAppRepository {
    Boolean add(AppEntity entity);

    Boolean delete(Long id);

    Boolean update(AppEntity entity);

    AppVO get(Long id);

    List<AppVO> queryPageList(AppQueryParam queryParam);
}
