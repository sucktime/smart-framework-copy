package org.smart4j.framework;

import org.smart4j.framework.helper.*;
import org.smart4j.framework.util.ClassUtil;

/**
 * Created by Bomb on 16/4/13.
 * <p>
 * <bold>集中</bold>加载相应Helper类, 非必要
 */

public final class HelperLoader {
    public static void init() {
        Class<?>[] classArr = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classArr) {
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
