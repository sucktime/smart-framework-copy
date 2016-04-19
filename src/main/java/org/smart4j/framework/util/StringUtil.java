package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bomb on 16/4/11.
 */
public final class StringUtil {

    /**
     * 字符串分隔符
     */
    public static final String SEPARATOR = String.valueOf((char) 29);

    /**
     * 判断字符串为空
     * @param string
     * @return
     */
    public static boolean isEmpty(String string){
        if(string != null){
            string = string.trim();
        }
        return StringUtils.isEmpty(string);
    }

    /**
     * 用分割串分割字符串
     * @param str
     * @param separator
     * @return
     */
    public static String[] splitString(String str, String separator){
        /**
         * 将整个separator串视为一个分隔符
         */
        return StringUtils.splitByWholeSeparator(str, separator);
    }
}
