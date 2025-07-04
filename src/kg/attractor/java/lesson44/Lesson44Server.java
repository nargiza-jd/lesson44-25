package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.*;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Lesson44Server extends BasicServer {

    private static final Configuration fm = initFreeMarker();

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setWrapUncheckedExceptions(true);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException("Can't init FreeMarker", e);
        }
    }

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        initRoutes();
    }

    private void initRoutes() {

        registerGet("/employee", ex -> {
            String id   = getQueryParam(ex, "id");
            Employee emp = SampleDataModel.getEmployeeById(id);

            if (emp == null) { sendText(ex, ResponseCodes.NOT_FOUND, "Сотрудник не найден"); return; }

            List<Book> issued = SampleDataModel.getBooks().stream()
                    .filter(b -> emp.getIssuedBookIds().contains(b.getId()))
                    .toList();

            renderTemplate(ex, "employee.ftl",
                    Map.of("employee", emp, "books", issued));
        });

        registerGet("/employees", ex ->
                renderTemplate(ex, "employees.ftl",
                        Map.of("employees", SampleDataModel.getEmployees()))
        );
    }

    protected void renderTemplate(HttpExchange ex, String tpl, Object model) {
        try (ByteArrayOutputStream buf = new ByteArrayOutputStream();
             Writer w = new OutputStreamWriter(buf))
        {
            Template t = fm.getTemplate(tpl);
            t.process(model, w);
            w.flush();
            sendByteData(ex, ResponseCodes.OK, ContentType.TEXT_HTML, buf.toByteArray());

        } catch (TemplateNotFoundException nf) {
            sendText(ex, ResponseCodes.NOT_FOUND, "Шаблон «"+tpl+"» не найден");

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            sendText(ex, ResponseCodes.SERVER_ERROR, "Ошибка сервера: "+e.getMessage());
        }
    }

    protected void sendText(HttpExchange ex, ResponseCodes code, String msg) {
        try {
            byte[] b = msg.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            ex.sendResponseHeaders(code.getCode(), b.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(b); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected Path makeFilePath(String... parts) {
        return Path.of("data", parts);
    }

    protected String getQueryParam(HttpExchange ex, String key) {
        String query = ex.getRequestURI().getRawQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }
}