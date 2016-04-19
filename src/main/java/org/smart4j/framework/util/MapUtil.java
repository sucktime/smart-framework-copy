package org.smart4j.framework.util;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * Created by Bomb on 16/4/13.
 */
public final class MapUtil {

    public static boolean isEmpty(Map<?,?> map){
        /**
         * MapUtils提供null-safe的isEmpty()
         */
        return MapUtils.isEmpty(map);
    }
}
