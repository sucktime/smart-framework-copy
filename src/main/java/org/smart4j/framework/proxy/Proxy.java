package org.smart4j.framework.proxy;

/**
 * Created by Bomb on 16/4/18.
 */

public interface Proxy {

    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
