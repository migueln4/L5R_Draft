package utils.converters;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ConverterUtils {
    private static final ConverterUtils CONVERTER = new ConverterUtils();
    private ConverterUtils() {}
    public static ConverterUtils getInstance() {
        return CONVERTER;
    }

    public Function<JsonArray, List<String>> jsonArrayToList = jsonArray -> {
        List<String> list = new ArrayList<>();
        if(jsonArray != null && !jsonArray.isJsonNull()) {
            jsonArray.forEach(str -> list.add(str.getAsString()));
        }
        return list;
    };
}
