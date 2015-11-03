package servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloServlet extends HttpServlet {

    Users users = new Users();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(getRequestURIwithDate(request));
        parseRequest(response, request);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doDelete ");
        System.out.println(getRequestURIwithDate(request));
        super.doDelete(request, response);
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doHead ");
        System.out.println(getRequestURIwithDate(request));
        super.doHead(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doOption ");
        System.out.println(getRequestURIwithDate(request));
        super.doOptions(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doPost ");
        System.out.println(getRequestURIwithDate(request));
        super.doPost(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doPut ");
        System.out.println(getRequestURIwithDate(request));
        super.doPut(request, response);
    }

    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doTrace ");
        System.out.println(getRequestURIwithDate(request));
        super.doTrace(request, response);
    }

    private String startPage() {

        StringBuilder sb = new StringBuilder();
        sb.append("<center>");
        sb.append("<br>");
        sb.append("<img style=\"-webkit-user-select: none; display: block; margin:auto;\"");
        sb.append("src=\"infopulse-logo.png\">");
        sb.append("<H1>").append(setFormatTime().format(new Date())).append("</H1>");
        sb.append("<H2>").append(new Random().nextLong()).append("</H2>");
        sb.append("</center>");
        return sb.toString();
    }

    private String page404() {
        StringBuilder sb = new StringBuilder();
        sb.append("<img style=\"-webkit-user-select: none; display: block; margin:auto;\"");
        sb.append("src=\"img/404.png\">");
        return sb.toString();
    }

    private void parseRequest(HttpServletResponse response, HttpServletRequest request) throws IOException {

        List<String> url = new ArrayList<>(Arrays.asList(request.getRequestURI().split("/")));

        if (request.getRequestURI().contains("favicon.ico")) {
            response.setContentType("image/png");
            writeFile(response, "img/", "favicon.png");
            return;
        }

        if (isStyle(request.getRequestURI())) {
            writeFile(response, "css/", url.get(url.size() - 1));
            return;
        }

        if (isScript(request.getRequestURI())) {
            writeFile(response, "js/", url.get(url.size() - 1));
            return;
        }

        if (isImage(request.getRequestURI())) {
            writeFile(response, "img/", url.get(url.size() - 1));
            return;
        }

        PrintWriter writer = response.getWriter();
        writer.println(includeCSS("css/style.css"));
        writer.println(includeJS("js/background.js"));
        writer.println(startPage());

        if (url.size() == 0) {
            return;
        }

        writer.println("<H3>");
        switch (url.get(1)) {
            case ("math"):
                writer.println(responseMath(url));
                break;
            case ("file"):
                writer.println(responseFile(url));
                break;
            case ("name"):
                writer.println(responseName(url));
                break;
            default:
                writer.println(page404());
        }
        writer.println("</H3>");
    }

    private String includeCSS(String cssFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("<head>").append("\n");
        sb.append("<link rel=\"stylesheet\" href=\"");
        sb.append(cssFile);
        sb.append("\" type=\"text/css\">").append("\n");
        sb.append("</head>").append("\n");
        return sb.toString();
    }

    private String includeJS(String jsFile) {
        StringBuilder sb = new StringBuilder();
        sb.append("<body>").append("\n");
        sb.append("<script type=\"text/javascript\" src=\"");
        sb.append(jsFile);
        sb.append("\"></script>").append("\n");
        sb.append("</body>").append("\n");
        return sb.toString();
    }

    private String responseMath(List<String> url) {
        try {
            if (url.size() < 3) {
                return "Enter Number";
            }
            BigDecimal big = new BigDecimal(url.get(2));
            big = big.multiply(big);
            return big.toString();
        } catch (Exception e) {
            return printException(e);
        }
    }

    private String responseFile(List<String> url) {

        BufferedReader in;
        StringBuilder fileName = new StringBuilder();
        try {
            if (isText(url.get(url.size() - 1))) {
                fileName.append("resources");
                for (int i = 1; i < url.size(); i++) {
                    fileName.append("/").append(url.get(i));
                }
            } else {
                throw new Exception("incorrect file type");
            }
            File file = new File(fileName.toString());
            in = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String s = "";
            while (s != null) {
                sb.append(s);
                sb.append("\n");
                s = in.readLine();
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            return printException(e);
        }
    }

    private String responseName(List<String> url) {
        if (url.size() < 3) {
            return users.toString();
        } else {
            users.getUsers().add(url.get(2));
            return "Hi! " + url.get(2);
        }
    }

    private String printException(Exception e) {
        return "<p style=\"color:#ff0000\">" + e + "</p>";
    }

    public boolean isFileType(String fileType, String request) {
        Pattern p = Pattern.compile(fileType);
        Matcher m = p.matcher(request);
        return m.matches();
    }

    public boolean isStyle(String request) {
        Pattern p = Pattern.compile(".+\\.(css)");
        Matcher m = p.matcher(request);
        return m.matches();
    }

    public boolean isScript(String request) {
        Pattern p = Pattern.compile(".+\\.(js)");
        Matcher m = p.matcher(request);
        return m.matches();
    }

    public boolean isImage(String request) {
        Pattern p = Pattern.compile(".+\\.(jpg|png)");
        Matcher m = p.matcher(request);
        return m.matches();
    }

    public boolean isText(String request) {
        Pattern p = Pattern.compile(".+\\.(txt)");
        Matcher m = p.matcher(request);
        return m.matches();
    }

    private void writeFile(HttpServletResponse response, String filePath, String fileName) throws IOException {

        File file = new File(new StringBuilder()
                .append("resources")
                .append("/")
                .append(filePath)
                .append("/")
                .append(fileName)
                .toString());

        int bufferSize = 64 * 1024;
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), bufferSize);
        byte[] buffer = new byte[bufferSize];
        while (true) {
            int i = in.read(buffer);
            if (i < 0) {
                break;
            }
            out.write(buffer, 0, i);
        }
        out.flush();
        out.close();
    }

    private String getRequestURIwithDate(HttpServletRequest request) {
        return new StringBuilder()
                .append(setFormatTime().format(new Date()))
                .append(" ")
                .append(request.getRequestURI())
                .toString();
    }

    private SimpleDateFormat setFormatTime() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }
}