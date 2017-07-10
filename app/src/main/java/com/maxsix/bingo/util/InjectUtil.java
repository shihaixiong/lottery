package com.maxsix.bingo.util;

/**
 * Created by shihaixiong on 2016/5/30.
 */
import java.lang.reflect.Field;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**注入框架

 */
public class InjectUtil {
    /**
     * 自动注入findViewById()
     * @param activity
     *         当前的activity
     */
    public static void autoInjectView(Activity activity) {
        Class<?> classAct = activity.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(com.maxsix.bingo.util.InjectView.class)) {//判断是否为InjectView注解
                com.maxsix.bingo.util.InjectView injectView = field.getAnnotation(com.maxsix.bingo.util.InjectView.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(activity, activity.findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /**
     * fragment 注释
     * @param fragment
     * @param v
     */
    public static void autoInjectView(Fragment fragment ,View v)
    {
        //得到Activity对应的Class
        Class clazz=fragment.getClass();
        //得到该Activity的所有字段
        Field[] fields=clazz.getDeclaredFields();

        for(Field field :fields)
        {
            //判断字段是否标注InjectView
            if(field.isAnnotationPresent(com.maxsix.bingo.util.InjectView.class))
            {
                //如果标注了，就获得它的id
                com.maxsix.bingo.util.InjectView notes=field.getAnnotation(com.maxsix.bingo.util.InjectView.class);
                int id=notes.value();
                if(id>0)
                {
                    //反射访问私有成员，必须加上这句
                    field.setAccessible(true);
                    //然后对这个属性复制
                    try {
                        field.set(fragment, v.findViewById(id));
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}