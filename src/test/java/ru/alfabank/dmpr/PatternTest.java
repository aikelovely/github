package ru.alfabank.dmpr;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
    @Test
    public void run() throws IOException {
        String string;
        try (FileInputStream stream = new FileInputStream("test/test.sql")) {
            string = IOUtils.toString(stream, "UTF-8");
        }

        Pattern pattern = Pattern.compile("'.*(\\?).*'");
        Matcher matcher = pattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(1);
            //matcher.appendReplacement(sb, "*");
        }
        //matcher.appendTail(sb);
        System.out.println(sb.toString());
    }
}
