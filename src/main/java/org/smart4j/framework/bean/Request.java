package org.smart4j.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Bomb on 16/4/13.
 * @author mrJ
 * @since 1.0.0
 *
 * 封装请求信息
 */
public class Request {

    //请求方法
    private String requestMethod;

    //请求路径
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod(){
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public int hashCode() {
        /**
         * 1. transient和static成员属性不参加计算
         * 2. 通过反射获得所有属性(包括私有属性和父类属性)
         * 3. 可指明不参与计算的属性数组
         */
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object){
        /**
         * 1. transient和static成员属性不参加计算
         * 2. 通过反射获得所有属性(包括私有属性和父类属性)
         * 3. 可指明不参与计算的属性数组
         */
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestPath='" + requestPath + '\'' +
                '}';
    }

}
