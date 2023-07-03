package com.tekfilo.inventory.multitenancy;

public class UserContext {

    private static final ThreadLocal<Integer> loggedInUser = new ThreadLocal<Integer>();

    public static Integer getLoggedInUser() {
        return loggedInUser.get();
    }

    public static void setLoggedInUser(Integer userId) {
        loggedInUser.set(userId);
    }

    public static void clear() {
        loggedInUser.remove();
    }
}
