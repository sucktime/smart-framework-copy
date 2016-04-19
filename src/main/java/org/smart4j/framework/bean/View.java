package org.smart4j.framework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bomb on 16/4/13.
 * @author mrJ
 * @since 1.0.0
 *
 * Action方法的返回类型: View对应于一个JSP页面及其model数据
 */

public class View {

    //视图路径
    private String path;

    //模型数据
    private Map<String, Object> model;

    public View(String path){
        this.path = path;
        model = new HashMap<String, Object>();
    }

    public View addModel(String key, Object value){
        model.put(key, value);
        return this;
    }

    public String getPath(){
        return path;
    }

    public Map<String, Object> getModel(){
        return model;
    }
}
