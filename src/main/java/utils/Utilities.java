package utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class Utilities {

    public static int getId(String requestUrl, String endpoint) {
        int id = 0;
        String idAsString = StringUtils.substringBefore(StringUtils.substringAfterLast(requestUrl, "/"), "?");
        if (!idAsString.isEmpty() && !idAsString.equals(endpoint)) {
            try {
                id = Integer.parseInt(idAsString);
            } catch (NumberFormatException e) {
                id = -1;
            }
        }
        return id;
    }

    public static void getJson(HttpServletResponse resp, String body) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        resp.getWriter().write(body);
    }
}
