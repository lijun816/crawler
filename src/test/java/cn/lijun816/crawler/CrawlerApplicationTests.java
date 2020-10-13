package cn.lijun816.crawler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
class CrawlerApplicationTests {

    private final static Pattern compile = Pattern.compile("mp3:'(.*)'");
    private final static Pattern compile1 = Pattern.compile("'\\+(.*)\\+'.*");

    public static void main(String[] args) {
        String txt = "https://mp3.dongporen.top/19fd0576dcab387bdc32d8e46148b6c8/5f85cea7/youshengshu/yangpaiqitan/001'+buONzIqChSbc+'";
        int endIndex = txt.indexOf("'+");
        if (endIndex > -1) {
            txt = txt.substring(0, endIndex);
            txt += ".mp3";
        }
        System.out.println(txt);

    }

    public static void main1(String[] args) {
        String content = txt;
        Matcher matcher = compile.matcher(content);
        if (matcher.find()) {
            String group1 = matcher.group(1);
            if (group1.contains("'+") && group1.contains("+'")) {
                Matcher matcher1 = compile1.matcher(group1);
                if (matcher1.find()) {
                    String varUrl = matcher1.group(1);
                    Pattern compile = Pattern.compile("(?:^|\\n)" + varUrl + " = '(.*)';");
                    Matcher matcher2 = compile.matcher(content);
                    if (matcher2.find()) {
                        String varPath = matcher2.group(1);
                        group1 = group1.replace("'", "").replace("+", "").replace(varUrl, varPath);
                    }
                }
            }
            String realMp3Url = "";
            System.out.println(group1);
            if (group1.startsWith("http")) {
                realMp3Url = group1;
            } else {
                realMp3Url = "YANG_PAI_HOST" + group1;
            }
        }
    }


    private static String txt = "<script>\n" +
            " var AajZB4qGUPKKH19K;\n" +
            " var CcjZB4qGUPKKH19K;\n" +
            " var djZB4qGUPKKH19Kb;\n" +
            " var bjZB4qGUPKKH19Kc;\n" +
            "AajZB4qGUPKKH19K = 'http://www.2uxs.com/play/9808_1_46859.html';\n" +
            "CcjZB4qGUPKKH19K = 'http://www.2uxs.com/youshengxiaoshuo/9808/';\n" +
            "bjZB4qGUPKKH19Kc = '.mp3';\n" +
            "djZB4qGUPKKH19Kb = 'https://mp3.dongporen.top/2f7b5a51a52f7ae324a3e70159140104/5f85ac24/youshengshu/yangpaiqitan/001';\n" +
            "function next()\n" +
            "    {\n" +
            "        if ((AajZB4qGUPKKH19K == 'nextnan') || (CcjZB4qGUPKKH19K == 'prenan'))\n" +
            "        {\n" +
            "            alert(\"已达到最后一集!\");        \n" +
            "        }else\n" +
            "        {\n" +
            "            window.parent.location.href=AajZB4qGUPKKH19K;\n" +
            "        }\n" +
            "    }\n" +
            "    function pre()\n" +
            "    {\n" +
            "        if ((CcjZB4qGUPKKH19K == 'prenan') || (AajZB4qGUPKKH19K == 'nextnan'))\n" +
            "        {\n" +
            "            alert(\"已达到第一集!\");\n" +
            "        }else\n" +
            "        {\n" +
            "            window.parent.location.href=CcjZB4qGUPKKH19K;\n" +
            "        }\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "\n" +
            "$(document).ready(function(){\n" +
            "    $(\"#jquery_jplayer_1\").jPlayer({\n" +
            "        ready: function (event) {\n" +
            "            $(this).jPlayer(\"setMedia\", {\n" +
            "    mp3:''+djZB4qGUPKKH19Kb+'.mp3'\n" +
            "            }).jPlayer(\"play\");\n" +
            "        },swfPath: \"jplayer\",\n" +
            "        supplied: \"mp3\",\n" +
            "        wmode: \"window\"\n" +
            "        ,\n" +
            "ended: function(event) {  \n" +
            "  if(event.jPlayer.options.loop) {\n" +
            "     $(this).jPlayer(\"play\");\n" +
            "} else {    \n" +
            "next();\n" +
            "}\n" +
            "}\n" +
            "    });\n" +
            "$(\"#playnext\").click(function() {\n" +
            "next();\n" +
            "});\n" +
            "$(\"#playup\").click(function() {\n" +
            "pre();\n" +
            "});\n" +
            "});\n" +
            "\n" +
            "//屏蔽右键菜单\n" +
            "document.oncontextmenu = function (event){\n" +
            "    if(window.event){\n" +
            "        event = window.event;\n" +
            "    }try{\n" +
            "        var the = event.srcElement;\n" +
            "        if (!((the.tagName == \"INPUT\" && the.type.toLowerCase() == \"text\") || the.tagName == \"TEXTAREA\")){\n" +
            "            return false;\n" +
            "        }\n" +
            "        return true;\n" +
            "    }catch (e){\n" +
            "        return false;\n" +
            "    }\n" +
            "}\n" +
            " </script>";

}
