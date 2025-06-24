package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.model.Employee;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private boolean isLoggedIn = false;

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

        registerGet("/login", exchange -> {
            Map<String, Object> data = new HashMap<>();
            data.put("user", new SampleDataModel().getUser());
            renderTemplate(exchange, "login.ftlh", data);
        });

        registerPost("/login", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

            String login = "", password = "";
            for (String param : body.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("login")) {
                        login = pair[1];
                    } else if (pair[0].equals("password")) {
                        password = pair[1];
                    }
                }
            }

            if (login.equals("one@one.one") && password.equals("123")) {
                exchange.getResponseHeaders().add("Location", "/books");
                exchange.sendResponseHeaders(302, -1);
            } else {
                exchange.getResponseHeaders().add("Location", "/login");
                exchange.sendResponseHeaders(302, -1);
            }
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
            renderTemplate(exchange, "book.ftl", data);
        });

        registerGet("/employee", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String id = getQueryParam(query, "id");
            Employee emp = SampleDataModel.getEmployeeById(id);

            Map<String, Object> data = new HashMap<>();

            if (emp != null) {
                data.put("employee", emp);
                renderTemplate(exchange, "employee.ftl", data);
            } else {
                sendText(exchange, "Сотрудник не найден");
            }
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
            // путь к каталогу в котором у нас хранятся шаблоны
            // это может быть совершенно другой путь, чем тот, откуда сервер берёт файлы
            // которые отправляет пользователю
            cfg.setDirectoryForTemplateLoading(new File("data"));

            // прочие стандартные настройки о них читать тут
            // https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
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
            // Загружаем шаблон из файла по имени.
            // Шаблон должен находится по пути, указанном в конфигурации
            Template temp = freemarker.getTemplate(templateFile);

            // freemarker записывает преобразованный шаблон в объект класса writer
            // а наш сервер отправляет клиенту массивы байт
            // по этому нам надо сделать "мост" между этими двумя системами

            // создаём поток, который сохраняет всё, что в него будет записано в байтовый массив
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // создаём объект, который умеет писать в поток и который подходит для freemarker
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {

                // обрабатываем шаблон заполняя его данными из модели
                // и записываем результат в объект "записи"
                temp.process(dataModel, writer);
                writer.flush();

                // получаем байтовый поток
                var data = stream.toByteArray();

                // отправляем результат клиенту
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private Object getSampleDataModel() {
        // возвращаем экземпляр тестовой модели-данных
        // которую freemarker будет использовать для наполнения шаблона
//        return new SampleDataModel();

        SampleDataModel sdm = new SampleDataModel();
        Map<String, Object> data = new HashMap<>();
        data.put("user", sdm.getUser());
        data.put("customers", sdm.getCustomers());
        data.put("currentDateTime", sdm.getCurrentDateTime());
        return data;
    }


}
