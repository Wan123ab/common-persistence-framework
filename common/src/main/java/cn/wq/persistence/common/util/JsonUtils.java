package cn.wq.persistence.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther : 万强
 * @date : 2019/8/20 10:23
 * @desc : Json工具类
 * @version : 1.0
 */
@SuppressWarnings("unchecked")
public class JsonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        /**
         * 允许出现特殊字符和转义符
         */
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        /**
         * 允许出现单引号
         */
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        /**
         * 忽略未知属性，比如查出来3个字段，但映射的vo只有2个字段，那么只会映射匹配的字段，并且不会报错
         */
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        /**
         * 设置忽略大小写，默认为false，需要改为true
         */
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * javaBean、列表数组转换为json字符串
     */
    public static String obj2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * javaBean、列表数组转换为json字符串,忽略空值
     */
    public static String obj2JsonIgnoreNull(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json 转JavaBean
     */

    public static <T> T json2Obj(String jsonString, Class<T> clazz) {
        //objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换为map
     * @return
     */
    public static Map json2map(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换为map
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) {
        Map<String, Map<String, Object>> map = null;
        Map<String, T> result = new HashMap<String, T>();
        try {
            map = (Map<String, Map<String, Object>>) objectMapper.readValue(jsonString, new TypeReference<Map<String, T>>() {});
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2Obj(entry.getValue(), clazz));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 深度转换json成map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> json2mapDeeply(String json) {
        return json2MapRecursion(json, objectMapper);
    }

    /**
     * 把json解析成list，如果list内部的元素存在jsonString，继续解析
     *
     * @param json
     * @param mapper 解析工具
     * @return
     * @throws Exception
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) {
        if (json == null) {
            return null;
        }

        List list = null;
        try {
            list = mapper.readValue(json, List.class);
            for (Object obj : list) {
                if (obj instanceof String) {
                    String str = (String) obj;
                    if (str.startsWith("[")) {
                        obj = json2ListRecursion(str, mapper);
                    } else if (obj.toString().startsWith("{")) {
                        obj = json2MapRecursion(str, mapper);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 把json解析成map，如果map内部的value存在jsonString，继续解析
     *
     * @param json
     * @param mapper
     * @return
     * @throws Exception
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) {
        if (json == null) {
            return null;
        }

        Map<String, Object> map = null;
        try {
            map = mapper.readValue(json, Map.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object obj = entry.getValue();
                if (obj instanceof String) {
                    String str = ((String) obj);

                    if (str.startsWith("[")) {
                        List<?> list = json2ListRecursion(str, mapper);
                        map.put(entry.getKey(), list);
                    } else if (str.startsWith("{")) {
                        Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                        map.put(entry.getKey(), mapRecursion);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 与javaBean json数组字符串转换为列表
     */
    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz) {

        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = (List<T>) objectMapper.readValue(jsonArrayStr, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    /**
     * map  转JavaBean
     */
    public static <T> T map2Obj(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * map  转JavaBean
     * @return
     */
    public static Map<String, Object> obj2Map(Object obj) {
        return obj2Obj(obj, Map.class);
    }

    /**
     * map 转json
     *
     * @param map
     * @return
     */
    public static String map2Json(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * obj转指定类型的Obj
     */
    public static <T> T obj2Obj(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

}
 
