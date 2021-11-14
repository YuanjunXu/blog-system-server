package blog.system.server.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TextUtils {

    public static final String regEx ="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }


    public static boolean isEmailAddressOk(String emailAddress) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String servletPath = request.getServletPath();
        //log.info("requestURI == > " + requestURI);
        //log.info("servletPath == > " + servletPath);
        //log.info("url == > " + requestURL.toString());
        String originalDomain = requestURL.toString().replace(servletPath, "");
        log.info("originalDomain = = > " + originalDomain);
        return originalDomain;
    }

}
