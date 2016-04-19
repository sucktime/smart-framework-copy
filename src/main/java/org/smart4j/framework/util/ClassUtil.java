package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Bomb on 16/4/12.
 */
public final class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     *
     * @return 当前线程的默认类加载器
     */
    public static ClassLoader getClassLoader() {
        //当前线程的默认类加载器
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     *
     * @param className
     * @param initialize
     * @return 加载的类对象
     */
    public static Class<?> loadClass(String className, boolean initialize) {
        Class<?> cls;
        try {
            cls = Class.forName(className, initialize, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure.", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 获取指定包名下的所有类(包名可能是目录,也可能是jar包)
     *
     * @param packageName
     * @return 类对象集合
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                System.out.println("package url: "+url);//
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        /*
                         * URL不会自动对空格等进行编码解码,而URI类会
                         */
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classSet, packagePath,packageName);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("get class set failure.", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 迭代加载某目录下的class
     *
     * @param classSet
     * @param packagePath
     * @param packageName
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, final String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) ||
                        pathname.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!StringUtil.isEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                // doAddClass()
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (!StringUtil.isEmpty(subPackagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (!StringUtil.isEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classSet, String className) {
        //默认不初始化类或接口
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }
}
