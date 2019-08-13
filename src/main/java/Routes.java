public class Routes {

    @WebRoute("/test")
    public static String test() {
        return "This is the test response with annotation";
    }

    @WebRoute("/test2")
    public static String test2() {
        return "This is the other test response with annotation";
    }

}
