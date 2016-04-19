package org.smart4j.framework.bean;

/**
 * Created by Bomb on 16/4/13.
 *
 * @author mrJ
 * @since 1.0.0
 * <p>
 * Action方法的返回类型: Data对于与一个Object对象,最终对应于一条Json数据
 */

public class Data {

    //模型数据
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
