package game;

import cards.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import utils.Utils;
import utils.converters.ConverterUtils;
import utils.converters.CardConverter;
import lombok.Data;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Data
public class CollectionL5R {

    private final String FIVERINGSDB = "https://api.fiveringsdb.com/cards";
    private final Integer TIMEOUT = 3000;
    private final String[] DISCARD_PACKS = Constants.DISCARD_PACKS;

    private final String RRG_VERSION_KEY = Constants.RRG_VERSION;
    private final String RECORDS_KEY = Constants.RECORDS;
    private final String SUCCESS_KEY = Constants.SUCCESS;
    private final String SIZE_KEY = Constants.SIZE;

    private List<ConflictCard> conflictCardList;
    private List<DynastyCard> dynastyCardList;
    private List<ProvinceCard> provinceCardList;
    private List<RoleCard> roleCardList;
    private List<StrongholdCard> strongholdCardList;

    private Utils utils;
    private CardConverter cardConverter;
    private ConverterUtils converter;
    private JsonParser parser;
    private Gson gson;
    private File file;
    private Object obj;
    private JsonObject jsonObject;
    private JsonArray records;

    private boolean connectionOK;

    private List<JsonCard> allCards;
    private String size;
    private String rrgVersion;

    public CollectionL5R() {
        init();
    }

    private void init() {
        this.conflictCardList = new ArrayList<>();
        this.dynastyCardList = new ArrayList<>();
        this.provinceCardList = new ArrayList<>();
        this.roleCardList = new ArrayList<>();
        this.strongholdCardList = new ArrayList<>();
        this.parser = new JsonParser();
        this.gson = new Gson();
        this.cardConverter = CardConverter.getInstance();
        this.utils = Utils.getInstance();
        this.converter = ConverterUtils.getInstance();
        readURL();
        deleteExceptions();
        initializeAll();
    }

    private Function<String, File> getCardFileReader = filename -> {
        ClassLoader cl = getClass().getClassLoader();
        try {
            return new File(Objects.requireNonNull(cl.getResource(filename)).getFile());
        } catch (NullPointerException npe) { //Si llega aquí es porque no existe el archivo.
            throw npe;
        }
    };

    private void initializeAll() {
        initializeConflictCardList();
        initializeDynastyCardList();
        initializeProvinceCardList();
        initializeRoleCardList();
        initializeStrongholdCardList();
    }

    /**
     * Se hace la llamada a la API de FiveRingsDB para obtener la colección completa de cartas.
     */
    private void readURL() {
        try {
            URL url = new URL(FIVERINGSDB);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            if (connection.getResponseCode() != 200)
                readFile();
            System.out.println("Connection: OK");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            readFile(content);
            this.connectionOK = true;
        } catch (SocketTimeoutException | SocketException e) {
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFile(StringBuilder content) {
        this.obj = parser.parse(content.toString());
        this.jsonObject = (JsonObject) obj;
        if (!this.jsonObject.getAsJsonPrimitive(SUCCESS_KEY).getAsBoolean())
            readFile();
        this.records = jsonObject.getAsJsonArray(RECORDS_KEY);
        Type type = new TypeToken<List<JsonCard>>() {
        }.getType();
        this.allCards = gson.fromJson(records, type);
        this.size = jsonObject.getAsJsonPrimitive(SIZE_KEY).getAsString();
        this.rrgVersion = jsonObject.getAsJsonPrimitive(RRG_VERSION_KEY).getAsString();
    }

    private void readFile() {
        System.out.println("FAIL Connection --> reading file...");
        this.connectionOK = false;
        try {
            this.file = getCardFileReader.apply("allcards.json");
        } catch (NullPointerException npe) {
            System.out.println("It doesn't exist the json file.");
        }
        try {
            this.obj = parser.parse(new FileReader(file));
            this.jsonObject = (JsonObject) obj;
            this.records = jsonObject.getAsJsonArray(RECORDS_KEY);
            Type type = new TypeToken<List<JsonCard>>() {
            }.getType();
            this.allCards = gson.fromJson(records, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteExceptions() {
        List<JsonCard> newList = new ArrayList<>(this.allCards);
        newList.stream().
                filter(Objects::nonNull)
                .filter(
                        card -> {
                            Type type = new TypeToken<List<JsonPackCards>>() {
                            }.getType();
                            List<JsonPackCards> packCards = gson.fromJson(card.getPack_cards(),
                                    type);
                            return packCards.size() == 0 || notInCollection(packCards);
                        }
                ).forEach(card -> this.allCards.remove(card));
        System.out.println("Total number of cards " + this.allCards.size());

    }

    private boolean notInCollection(List<JsonPackCards> packCards) {
        for(String pack : DISCARD_PACKS) {
            if(pack.equals(packCards.get(0).getPack().get("id").toString()))
                return true;
        }
        return false;
    }

    private void initializeConflictCardList() {
        allCards.stream().filter(card -> card.getSide().equalsIgnoreCase(Constants.CONFLICT)).
                forEach(card -> conflictCardList.add(cardConverter.jsonCardToConflictCard.apply(card,gson)));
        System.out.println("Conflict Card List: OK (" + this.conflictCardList.size() + " cards)");
    }

    private void initializeDynastyCardList() {
        allCards.stream().filter(card -> card.getSide().equalsIgnoreCase(Constants.DYNASTY)).
                forEach(card -> dynastyCardList.add(cardConverter.jsonCardToDynastyCard.apply(card,gson)));
        System.out.println("Dynasty Card List: OK (" + this.dynastyCardList.size() + " cards)");
    }

    private void initializeProvinceCardList() {
        allCards.stream().filter(card -> card.getType().equalsIgnoreCase(Constants.PROVINCE)).
                forEach(card -> provinceCardList.add(cardConverter.jsonCardToProvinceCard.apply(card,gson)));
        System.out.println("Province Card List: OK (" + this.provinceCardList.size() + " cards)");
    }

    private void initializeRoleCardList() {
        allCards.stream().filter(card -> card.getSide().equalsIgnoreCase(Constants.ROLE)).
                forEach(card -> roleCardList.add(cardConverter.jsonCardToRoleCard.apply(card,gson)));
        System.out.println("Role Card List: OK (" + this.roleCardList.size() + " cards)");
    }

    private void initializeStrongholdCardList() {
        allCards.stream().filter(card -> card.getType().equalsIgnoreCase(Constants.STRONGHOLD)).
                forEach(card -> strongholdCardList.add(cardConverter.jsonCardToStrongholdCard.apply(card,gson)));
        System.out.println("Stronghold Card List: OK (" + this.strongholdCardList.size() + " cards)");
    }

}
