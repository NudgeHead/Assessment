package org.automation.assessment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Automation {

    private static final String userURL = "http://jsonplaceholder.typicode.com/users";
    private static final String todoURL = "http://jsonplaceholder.typicode.com/todos?userId=";

    public static void main(String[] args) {
        try {
            JSONArray users = getUsers();
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                double LATITUDE = user.getJSONObject("address").getJSONObject("geo").getDouble("lat");
                double LONGITUDE = user.getJSONObject("address").getJSONObject("geo").getDouble("lng");

                if (FanCodeCity(LATITUDE, LONGITUDE)) {
                    int userId = user.getInt("id");
                    JSONArray todos = getTodos(userId);
                    TaskCompletion(user.getString("name"), todos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray getUsers() throws Exception {
        return APIrequest(userURL);
    }

    private static JSONArray getTodos(int userId) throws Exception {
        return APIrequest(todoURL + userId);
    }

    private static JSONArray APIrequest(String endPoint) throws Exception {
        URL url = new URL(endPoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONArray(response.toString());
    }

    private static boolean FanCodeCity(double LATITUDE, double LONGITUDE) {
        return (LATITUDE >= -40 && LATITUDE <= 5) && (LONGITUDE >= 5 && LONGITUDE <= 100);
    }

    private static void TaskCompletion(String UserName, JSONArray todos) {
        int totalTodos = todos.length();
        int completedTodos = 0;

        for (int i = 0; i < totalTodos; i++) {
            if (todos.getJSONObject(i).getBoolean("completed")) {
                completedTodos++;
            }
        }

        if (totalTodos > 0) {
            float completionPercentage = (completedTodos / (float) totalTodos) * 100;
            if (completionPercentage > 50) {
                System.out.println(UserName + ", the user, has completed " + Math.round(completionPercentage) + " of their todos.");
            } else {
                System.out.print(UserName + ", the user, has not completed more than 50% of their todos");
            }
        } else {
            System.out.println(UserName + ", the user, has no todos.");
        }
    }
}


