package cn.lijun816.common.util;

import lombok.Data;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * FileNameUtil
 *
 * @author lijun 2020/10/13
 */
public class FileNameUtil {
    private static final Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    public static String filenameFilter(String str) {
        return str == null ? "null" : FilePattern.matcher(str).replaceAll("");
    }
}