package cn.wq.persistence.common.util;

import java.util.Arrays;

/**
 * @version 1.0
 * @auther 万强
 * @date 2019/10/3 10:49
 * @desc 异常工具类
 */
public class ExceptionUtils {

    /**
     * 获取异常错误信息和详细堆栈信息
     *
     * @param t 异常
     * @return 异常错误信息和详细堆栈信息
     */
    public static String getExceptionDetail(Throwable t) {
        if (t == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (t.getCause() instanceof Exception) {
            Arrays.stream(org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseStackTrace(t.getCause())).
                    forEach(error -> sb.append(error).append("\n"));
        } else {
            Arrays.stream(org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseStackTrace(t)).
                    forEach(error -> sb.append(error).append("\n"));
        }

        return sb.toString();
    }

}
 
