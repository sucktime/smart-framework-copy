package org.smart4j.framework.util;

/**
 * Created by Bomb on 16/4/11.
 */
public final class CastUtil {

    /**
     * 转型为String
     * @param object
     * @return
     */
    public static String castString(Object object){
        return castString(object, "");
    }

    /**
     * 转型为String
     * @param object
     * @param defaultValue
     * @return
     */
    public static String castString(Object object, String defaultValue){
        return object != null ? String.valueOf(object) : defaultValue;
    }

    /**
     * 转型为int
     * @param object
     * @return
     */
    public static int castInt(Object object){
        return castInt(object, 0);
    }

    /**
     * 转型为int
     * @param object
     * @param defaultValue
     * @return
     */
    public static int castInt(Object object, int defaultValue){
        int intValue = defaultValue;
        if(object != null){
            String stringValue = castString(object);
            if( !StringUtil.isEmpty(stringValue) ){
                try{
                    intValue = Integer.parseInt(stringValue);
                } catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转型为long
     * @param object
     * @return
     */
    public static long castLong(Object object){
        return castLong(object, 0);
    }
    /**
     * 转型为long
     * @param object
     * @param defaultValue
     * @return
     */
    public static long castLong(Object object, long defaultValue){
        long longValue = defaultValue;
        if(object != null){
            String stringValue = castString(object);
            if( !StringUtil.isEmpty(stringValue)){
                try{
                    longValue = Long.parseLong(stringValue);
                } catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    public static boolean castBoolean(Object object){
        return castBoolean(object, false);
    }

    public static boolean castBoolean(Object object, boolean defaultValue){
        boolean booleanValue = defaultValue;
        if(object != null){
            booleanValue = Boolean.parseBoolean(CastUtil.castString(object));
        }
        return booleanValue;
    }

}
