package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.model.Book;
import kg.attractor.java.model.Employee;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);
        initRoutes();
    }

    private void initRoutes() {
        registerGet("/books", exchange -> {
            Map<String, Object> data = new HashMap<>();
            data.put("books", SampleDataModel.getBooks());
            data.put("user", new SampleDataModel().getUser());
            renderTemplate(exchange, "books.ftl", data);
        });

        registerGet("/book", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String id = getQueryParam(query, "id");
            var book = SampleDataModel.getBookById(id);

            if (book == null) {
                try {
                    sendByteData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_HTML, "Книга не найдена".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("book", book);
            renderTemplate(exchange, "book.ftlh", data);
        });

        registerGet("/employee", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String id = getQueryParam(query, "id");
            Employee emp = SampleDataModel.getEmployeeById(id);

            Map<String, Object> data = new HashMap<>();

            if (emp != null) {
                List<Book> issuedBooks = SampleDataModel.getBooks().stream()
                        .filter(b -> emp.getIssuedBookIds().contains(b.getId()))
                        .toList();
                data.put("employee", emp);
                data.put("books", issuedBooks);
                renderTemplate(exchange, "employee.ftl", data);
            } else {
                sendText(exchange, "Сотрудник не найден");
            }
        });

        registerGet("/employees", exchange -> {
            Map<String, Object> data = new HashMap<>();
            data.put("employees", SampleDataModel.getEmployees());
            renderTemplate(exchange, "employees.ftl", data);
        });
    }




    private void sendText(HttpExchange exchange, String responseText) {
        try {
            byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getQueryParam(String query, String key) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals(key)) {
                return pair[1];
            }
        }
        return null;
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange, "sample.html", getSampleDataModel());
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private Object getSampleDataModel() {
        SampleDataModel sdm = new SampleDataModel();
        Map<String, Object> data = new HashMap<>();
        data.put("user", sdm.getUser());
        data.put("customers", sdm.getCustomers());
        data.put("currentDateTime", sdm.getCurrentDateTime());
        return data;
    }

    protected Path makeFilePath(String... parts) {
        return Path.of("data", parts);
    }
}