package com.doit.net.scan.udp.service;

import com.doit.net.scan.udp.base.RemMacroItem;

/**
 * Created by wly on 2019/7/11.
 * 处理成功接口
 */
public interface IHandlerFinish<T extends RemMacroItem> {
	void workFinish(T body);
}
