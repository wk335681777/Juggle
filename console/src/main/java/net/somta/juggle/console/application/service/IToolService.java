package net.somta.juggle.console.application.service;

import net.somta.juggle.console.interfaces.param.tool.FreemarkExecuteParam;
import net.somta.juggle.core.model.CamelResult;

public interface IToolService {

    CamelResult freemarkExecute(FreemarkExecuteParam param);

}
