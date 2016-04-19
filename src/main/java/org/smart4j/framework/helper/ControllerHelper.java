package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bomb on 16/4/13.
 * <p>
 * 准备好Request与Handler的映射关系
 */
public final class ControllerHelper {

    //用于存放Request于Handler的映射关系
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    //扫描准备映射信息
    static {
        //获取所有@Controller类型
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        System.out.println("ControllerSet:"+controllerClassSet);

        if (!CollectionUtil.isEmpty(controllerClassSet)) {

            for (Class<?> controllerClass : controllerClassSet) {
                //获取@Controller类型的所有方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if(!ArrayUtil.isEmpty(methods)){
                    for (Method method : methods) {
                        //判断是否被@Action注解
                        if(method.isAnnotationPresent(Action.class)){
                            //从Action注解中的value获取URL映射规则
                            Action action = method.getDeclaredAnnotation(Action.class);
                            String mapping = action.value();
                            //验证URL规则
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if (!ArrayUtil.isEmpty(array) && array.length == 2){
                                    //获取RequestMethod与RequestPath
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    //注册该Request->Handler映射信息
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    System.out.println("action: request="+request + ", handler="+handler);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取请求处理器Handler
     * @param requestMethod
     * @param requestPath
     * @return 相应的handler
     */
    public static Handler getHandler(String requestMethod, String requestPath){
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
