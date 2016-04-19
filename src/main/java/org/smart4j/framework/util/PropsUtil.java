package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Bomb on 16/4/11.
 */
public final class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream inputStream = null;

        try{
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream == null){
                throw new FileNotFoundException(fileName + ": file is not found.");
            }
            props = new Properties();
            props.load(inputStream);
        } catch (IOException e){
            LOGGER.error("load propeties file failure",e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e){
                    LOGGER.error("close input strean failure",e);
                }
            }
        }

        return props;
    }

    /**
     * 获取字符型属性
     * @param properties
     * @param key
     * @return
     */
    public  static String getString(Properties properties, String key){
        return  getString(properties, key, "");
    }

    /**
     * 获取字符型属性
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties properties, String key, String defaultValue){
        String value = defaultValue;
        if (properties != null && properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取int属性
     * @param properties
     * @param key
     * @return
     */
    public static int getInt(Properties properties, String key){
        return getInt(properties, key ,0);
    }

    /**
     * 获取int属性
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties properties, String key, int defaultValue){
        int value = defaultValue;
        if(properties != null && properties.containsKey(key)){
            value = CastUtil.castInt(properties.getProperty(key), defaultValue);
        }
        return value;
    }

    /**
     * 获取boolean属性
     * @param properties
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties properties, String key){
        return getBoolean(properties, key,false);
    }

    /**
     * 获取boolean属性
     * @param properties
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue){
        boolean value = defaultValue;
        if(properties != null && properties.containsKey(key) ){
            return CastUtil.castBoolean(properties.get(key), defaultValue);
        }
        return value;
    }
}
