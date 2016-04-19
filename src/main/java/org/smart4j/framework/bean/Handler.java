package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * Created by Bomb on 16/4/13.
 * @author mrJ
 * @since 1.0.0
 *
 * 封装Action信息
 */
public class Handler {

    //@Controller类
    private Class<?> controllerClass;

    //@Action类
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    @Override
    public String toString() {
        return "Handler{" +
                "controllerClass=" + controllerClass +
                ", actionMethod=" + actionMethod +
                '}';
    }
}
