package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bomb on 16/4/13.
 * @author mrj
 * @since 1.0.0
 */
public final class ClassHelper {
    /**
     * 定义集合类,用于存放所加载的类
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
        //System.out.println("Class Set: "+CLASS_SET);
    }

    /**
     * 获取应用下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取所有@Service注解的类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Service.class)){
                serviceSet.add(cls);
            }
        }
        return serviceSet;
    }

    /**
     * 获取所有@Controller注解的类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> controllerSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(Controller.class)){
                controllerSet.add(cls);
            }
        }
        return controllerSet;
    }

    /**
     * 获取所有Bean类
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        beanSet.addAll(getServiceClassSet());
        beanSet.addAll(getControllerClassSet());
        return beanSet;
    }

    /**
     * 获取某类型的所有子类型
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> childrenSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            //isAssignableFrom(): 是否是 同类型或子类型
            if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
                childrenSet.add(cls);
            }
        }
        return childrenSet;
    }

    /**
     * 获取被某注解标记的所有类型
     * @param annotation
     * @return
     */
    public static Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> annotatedSet = new HashSet<Class<?>>();
        for(Class<?> cls : CLASS_SET){
            if (cls.isAnnotationPresent(annotation)){
                annotatedSet.add(cls);
            }
        }
        return annotatedSet;
    }
}
