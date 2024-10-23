package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.DurationTypeAdapter;
import utils.LocalDateTimeTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private static Gson gson;

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    private static void gsonCreater() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }

    public static Gson getGson() {
        if (gson == null) {
            gsonCreater();
            return gson;
        } else {
            return gson;
        }
    }
}
