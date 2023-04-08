package com.liu.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtil {
    private BeanCopyUtil(){

    }
    public static <V>V copyBean(Object source,Class<V> clazz) {
        //创建目标对象实现拷贝
        V result =null;
        try {
            result =clazz.newInstance();
            BeanUtils.copyProperties(source,result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static <O,V>List<V> copyBeanList(List<O> source, Class<V> clazz) throws InstantiationException, IllegalAccessException {
        //创建目标对象实现拷贝
      return source.stream().map(o->copyBean(o,clazz)).collect(Collectors.toList());
    }
}
