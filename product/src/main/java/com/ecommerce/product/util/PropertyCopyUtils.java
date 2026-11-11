package com.ecommerce.product.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;

public class PropertyCopyUtils {
    public static void copyNonNullProperties(Object source, Object target){
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper trg = new BeanWrapperImpl(target);

        // PropertyDescriptor = metadata about each field including name + getter + setter:
        for (PropertyDescriptor pd: src.getPropertyDescriptors()){
            String propName = pd.getName();

            if ("class".equals(propName)) continue;

            Object propValue = src.getPropertyValue(propName);
            if (propValue != null && trg.isWritableProperty(propName)){
                System.out.println("saving "+ propName + " with value: " + src.getPropertyValue(propName));
                trg.setPropertyValue(propName, propValue);
            }
        }

    }
}
