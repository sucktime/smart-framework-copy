package org.smart4j.framework.bean;

import org.smart4j.framework.util.CastUtil;

import java.util.Map;

/**
 * Created by Bomb on 16/4/13.
 * @author mrJ
 * @since 1.0.0
 *
 * 请求参数
 */

public class Param {

    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String paramName) {
        return CastUtil.castLong(paramMap.get(paramName));
    }

    /**
     * n种get方法
     */

    /**
     * 获取所有参数
     * @return 包含所有参数的Map
     */
    public Map<String, Object> getMap(){
        return paramMap;
    }
}
