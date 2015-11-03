package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelloServlet extends HttpServlet {

    Users users = new Users();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        System.out.println(request.getRequestURI());
        parseRequestGET(writer, request.getRequestURI());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doDelete ");
        System.out.println(request.getRequestURI());
        super.doDelete(request, response);
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doHead ");
        System.out.println(request.getRequestURI());
        super.doHead(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doOption ");
        System.out.println(request.getRequestURI());
        super.doOptions(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doPost ");
        System.out.println(request.getRequestURI());
        super.doPost(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doPut ");
        System.out.println(request.getRequestURI());
        super.doPut(request, response);
    }

    @Override
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.print("doTrace ");
        System.out.println(request.getRequestURI());
        super.doTrace(request, response);
    }

    private String createPage() {

        StringBuilder sb = new StringBuilder();
        sb.append("<center>");
        sb.append("<br>");
        sb.append("<img style=\"-webkit-user-select: none; display: block; margin:auto;\"");
        sb.append("src=\"http://www.infopulse.com/wp-content/themes/infopulse/img/infopulse-logo.png\">");
        sb.append("<H1>").append(setFormatTime().format(new Date())).append("</H1>");
        sb.append("<H2>").append(new Random().nextLong()).append("</H2>");
        sb.append("</center>");
        return sb.toString();
    }

    private String page404() {
        StringBuilder sb = new StringBuilder();
        sb.append("<img style=\"-webkit-user-select: none; display: block; margin:auto;\"");
        sb.append("src=\"http://0.s3.envato.com/files/161763/Screenshots/01_white_color_scheme.__large_preview.jpg\">");
        return sb.toString();
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

    private void parseRequestGET(PrintWriter writer, String request) throws IOException {

        ArrayList<String> url = (new ArrayList<String>(Arrays.asList(request.split("/"))));

        if (request.contains(".js") || request.contains(".css")) {
            StringBuilder sb = new StringBuilder();
            sb.append(url.get(url.size() - 2));
            sb.append("/");
            sb.append(url.get(url.size() - 1));
            writer.print(readFileToString(sb.toString()));
            return;
        }

        writer.println(includeCSS("css/style.css"));
        writer.println(includeJS("js/background.js"));
        writer.println(createPage());

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
        }
        writer.println("</H3>");
    }

    private String responseMath(ArrayList<String> url) {
        try {
            return String.valueOf(Math.pow(Long.parseLong(url.get(2)), 2));
        } catch (Exception e) {
            return printException(e);
        }
    }

    private String responseFile(ArrayList<String> url) {
        BufferedReader in = null;
        try {
            if (url.get(url.size() - 1).contains(".txt")) {
                StringBuilder fileName = new StringBuilder("file");
                for (int i = 2; i < url.size(); i++) {
                    fileName.append("/").append(url.get(i));
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
            } else {
                throw new Exception("incorrect file request");
            }
        } catch (Exception e) {
            return printException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                return printException(e);
            }
        }
    }

    private String responseName(ArrayList<String> url) {
        if (url.size() < 3) {
            return users.toString();
        } else {
            users.getUsers().add(url.get(2));
            return "Hi! " + url.get(2);
        }
    }

    private SimpleDateFormat setFormatTime() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    private String printException(Exception e) {
        return "<p style=\"color:#ff0000\">" + e + "</h1>";
    }

    private String readFileToString(String request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            File file = new File("resources/" + request);
            reader = new BufferedReader(new FileReader(file));
            String s = "";
            while (s != null) {
                s = reader.readLine();
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            System.out.println("File not found " + request);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }
}