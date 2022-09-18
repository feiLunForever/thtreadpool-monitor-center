package org.idea.threadpool.monitor.console.utils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 7:07 下午 2022/9/8
 */
public class BeanConvertUtils {

    public static <K, T> List<T> convertList(List<K> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        } else {
            List targetList = new ArrayList((int) ((double) sourceList.size() / 0.75D) + 1);
            Iterator item = sourceList.iterator();

            while (item.hasNext()) {
                K source = (K) item.next();
                targetList.add(convert(source, targetClass));
            }

            return targetList;
        }
    }


    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        } else {
            T t = newInstance(targetClass);
            BeanUtils.copyProperties(source, t);
            return t;
        }
    }

    private static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (Exception var2) {
            throw new BeanInstantiationException(targetClass, "instantiation error", var2);
        }
    }


}
