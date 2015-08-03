package co.solinx.forestserial.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
                if(field.getName().indexOf("$VALUES")==-1) {
                    if (field.getName().equals(fieldName[i])) {
                        newField[i] = field;
                    }
                }
            }
        }

        return newField;
    }

    /**
     * 取得对象类型字段
     *
     * @param fields
     * @return
     */
    public Field[] getObjectTypeField(Field[] fields) {
        ArrayList<Field> objectTypeList = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getType().isPrimitive()) {
                objectTypeList.add(field);
            }
        }
        Field[] objectTypes = new Field[0];
        if (objectTypeList != null) {
            objectTypes = objectTypeList.toArray(objectTypes);
        }

        return objectTypes;
    }

    /**
     * 取得原生类型字段
     *
     * @param fields
     * @return
     */
    public Field[] getPrimitiveTypeField(Field[] fields) {
        ArrayList<Field> primitiveList = new ArrayList();
        for (Field field : fields) {
            if (field.getType().isPrimitive()) {
                primitiveList.add(field);
            }
        }
        Field[] primitive = new Field[0];
        if (primitiveList != null) {
            primitive = primitiveList.toArray(primitive);
        }
        return primitive;
    }

    /**
     * 取得字段值
     *
     * @param field
     * @param obj
     * @return
     */
    public Object getFieldValue(Field field, Object obj) {
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
