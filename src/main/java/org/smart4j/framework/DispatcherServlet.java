package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bomb on 16/4/14.
 * @author mrJ
 * @since 1.0.0
 *
 * 转发器
 */

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //1. 加载Helper类族
        HelperLoader.init();

        //2. 获取ServletContext对象,用于注册Servlet
        ServletContext servletContext = servletConfig.getServletContext();
        //2.1 注册JSP的Servlet
        ServletRegistration jspServletRegistration = servletContext.getServletRegistration("jsp");
        jspServletRegistration.addMapping(ConfigHelper.getAppJspPath() + "*");
        //2.2 注册处理静态资源的默认Servlet
        ServletRegistration defaultServletRegistration = servletContext.getServletRegistration("default");
        defaultServletRegistration.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法与路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath   = req.getPathInfo();

        System.out.println("ContextPath:" + req.getContextPath());
        System.out.println("[Disaptcher.service] method:" + requestMethod + ", path:" + requestPath);

        //获取@Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if(handler != null){
            //A. 获取@Controller类及其Bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //B 创建Param对象
            Map<String, Object> paramMap = new HashMap<String, Object>();
            //B.1 获取get请求参数
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            //B.2 获取put请求参数
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (!StringUtil.isEmpty(body)){
                String[] params = StringUtil.splitString(body, "&");
                if(!ArrayUtil.isEmpty(params)){
                    for(String param : params){
                        String[] arr = StringUtil.splitString(param, "=");
                        if(!ArrayUtil.isEmpty(arr) && arr.length == 2){
                            String paramName = arr[0];
                            String paramValue = arr[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }
            //B.3
            Param param = new Param(paramMap);

            //C. 调用@Action方法
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);

            //D. 处理@Action方法返回值
            if (result instanceof View){
                //返回JSP页面
                View view = (View)result;
                String path = view.getPath();
                if(!StringUtil.isEmpty(path)){
                    if (path.startsWith("/")){
                        System.out.println("[Dispather.service](startsWith/) path_fixed:" + req.getContextPath() + path);
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()){
                            //System.out.println("model data:" + entry);///
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        System.out.println("[Dispather.service] path_fixed:" + ConfigHelper.getAppJspPath() + path);
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                }
            } else if(result instanceof Data){
                //返回JSON数据
                Data data = (Data)result;
                Object model = data.getModel();
                if(model != null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String jsonString = JsonUtil.toJson(model);
                    writer.write(jsonString);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
