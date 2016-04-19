package org.smart4j.framework.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * Created by Bomb on 16/4/13.
 */
public final class CollectionUtil {

    /**
     * 判断Collection是否为空
     * @param collection
     * @return 是否: null或空
     */
    public static boolean isEmpty(Collection<?> collection){
        /**
         * CollectionUtils提供null-safe的isEmpty()
         */
        return CollectionUtils.isEmpty(collection);
    }
}
