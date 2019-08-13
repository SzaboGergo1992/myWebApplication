import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws IOException {
        init();
    }

    private static void init() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.createContext("/test2", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            for(Method m: Routes.class.getMethods()) {
                if(m.isAnnotationPresent(WebRoute.class)) {
                    /*
                      Here comes your logic.
                      If the given path from the HttpExchange method is
                      the SAME like the WebRoute annotation's path,
                      you should INVOKE this method.
                    */
                    if(m.getAnnotation(WebRoute.class).value().equals(t.getRequestURI().getPath())) {
                        try {
                            String response = (String) m.invoke(null);
                            t.sendResponseHeaders(200, response.length());
                            OutputStream os = t.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } catch (IllegalAccessException e) {
                            System.out.println(e.getMessage());;
                        } catch (InvocationTargetException f) {
                            f.printStackTrace();
                        }
                    }
                }
            }

        }
    }
}
