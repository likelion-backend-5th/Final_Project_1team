package mutsa.api.util;

public class UserUtil {

    public static boolean diffPassword(String password, String checkPassword) {
        return !password.equals(checkPassword);
    }
}
