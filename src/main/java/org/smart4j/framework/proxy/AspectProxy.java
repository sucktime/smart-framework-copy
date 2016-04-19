package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by Bomb on 16/4/18.
 *
 * 切面代理
 */

public class AspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        begin();//@
        try{
            //判断是否需要拦截该方法
            if(intercept(cls,method,params)){
                before(cls, method, params);//@before
                result = proxyChain.doProxyChain();
                after(cls, method, params, result);//@after
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e){
            LOGGER.error("proxy failure.", e);
            error(cls, method, params, e); //@afterThrowing
            throw e;
        } finally {
            end(); //@
        }
        return result;
    }

    public void begin() {
    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) {
    }

    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    public void end() {
    }
}
