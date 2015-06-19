package co.solinx.forestserial.util;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by linx on 2015/6/19.
 */
public class FieldUtil {

    /**
     * 对字段名称进行排序，
     *
     * @param fields
     * @return 排序后Field
     */
    public Field[] fieldSort(Field[] fields) {
        String[] fieldName = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldName[i] = fields[i].getName();
        }
        //对字段名称排序
        Arrays.sort(fieldName);
        Field[] newField = new Field[fields.length];
        //根据名称排序Field
        for (int i = 0; i < fieldName.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                if (field.getName().equals(fieldName[i])) {
                    newField[i] = field;
                }
            }
        }

        return newField;
    }


    /**
     * ȡ���ֶε�ֵ
     * @param field
     * @param obj
     * @return
     */
    public Object getFieldValue(Field field,Object obj) {
        Object value = null;
        try {
            field.setAccessible(true);
            value = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

}
