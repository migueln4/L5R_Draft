package utils.converters;

import cards.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import constants.Constants;
import utils.Utils;
import utils.interfaces.ThreeParametersLambda;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static constants.Constants.ALL_ELEMENTS;
import static constants.Constants.ELEMENTS;

public final class CardConverter {

    private static final CardConverter CONVERTER = new CardConverter();
    private ConverterUtils converter;
    private Utils utils;

    private CardConverter() {
        converter = ConverterUtils.getInstance();
        utils = Utils.getInstance();
    }

    public static CardConverter getInstance() {
        return CONVERTER;
    }

    /**
     * Función que convierte una JsonCard en una DynastyCard
     */
    public ThreeParametersLambda<JsonCard, Gson, DynastyCard> jsonCardToDynastyCard = (jsonCard,gson) -> {
        DynastyCard newCard = new DynastyCard();
        Type type = new TypeToken<List<JsonPackCards>>() {
        }.getType();
        List<JsonPackCards> packCards = gson.fromJson(jsonCard.getPack_cards(), type);

        utils.setNameCard(packCards, newCard);

        newCard.setIdFiveRingsDB(jsonCard.getId());
        newCard.setDeckLimit(jsonCard.getDeck_limit());
        newCard.setName(jsonCard.getName());
        newCard.setClan(jsonCard.getClan());
        newCard.setIsRestricted(jsonCard.getIs_restricted());
        newCard.setUnicity(jsonCard.getUnicity());
        newCard.setName_extra(jsonCard.getName_extra());

        newCard.setTraits(converter.jsonArrayToList.apply(jsonCard.getTraits()));

        if (jsonCard.getRole_restriction() != null)
            utils.elementAndRoleRestrictions(newCard, jsonCard.getRole_restriction());

        newCard.getElements().add(newCard.getElementLimit());

        if (Constants.CHARACTER.equalsIgnoreCase(jsonCard.getType()))
            newCard.setCharacter(Boolean.TRUE);
        else
            newCard.setCharacter(Boolean.FALSE);


        return newCard;
    };

    /**
     * Función que convierte una JsonCard en una ProvinceCard.
     */
    public ThreeParametersLambda<JsonCard,Gson, ProvinceCard> jsonCardToProvinceCard = (jsonCard,gson) -> {
        ProvinceCard newCard = new ProvinceCard();
        Type type = new TypeToken<List<JsonPackCards>>() {
        }.getType();
        List<JsonPackCards> packCards = gson.fromJson(jsonCard.getPack_cards(), type);

        utils.setNameCard(packCards, newCard);

        newCard.setIdFiveRingsDB(jsonCard.getId());
        newCard.setDeckLimit(jsonCard.getDeck_limit());
        newCard.setName(jsonCard.getName());
        newCard.setClan(jsonCard.getClan());
        newCard.setIsRestricted(jsonCard.getIs_restricted());
        newCard.setUnicity(jsonCard.getUnicity());
        newCard.setName_extra(jsonCard.getName_extra());

        newCard.setTraits(converter.jsonArrayToList.apply(jsonCard.getTraits()));

        if (jsonCard.getRole_restriction() != null)
            utils.elementAndRoleRestrictions(newCard, jsonCard.getRole_restriction());

        if(jsonCard.getElement().equalsIgnoreCase(ALL_ELEMENTS)) {
            newCard.getElements().addAll(Arrays.asList(ELEMENTS));
        } else {
            newCard.getElements().add(jsonCard.getElement());
        }

        return newCard;
    };

    /**
     * Función que convierte una JsonCard en una StrongholdCard
     */
    public ThreeParametersLambda<JsonCard,Gson, StrongholdCard> jsonCardToStrongholdCard = (jsonCard,gson) -> {
        StrongholdCard newCard = new StrongholdCard();

        Type type = new TypeToken<List<JsonPackCards>>() {
        }.getType();
        List<JsonPackCards> packCards = gson.fromJson(jsonCard.getPack_cards(), type);

        utils.setNameCard(packCards, newCard);

        newCard.setIdFiveRingsDB(jsonCard.getId());
        newCard.setDeckLimit(jsonCard.getDeck_limit());
        newCard.setName(jsonCard.getName());
        newCard.setClan(jsonCard.getClan());
        newCard.setIsRestricted(jsonCard.getIs_restricted());
        newCard.setUnicity(jsonCard.getUnicity());
        newCard.setName_extra(jsonCard.getName_extra());

        newCard.setTraits(converter.jsonArrayToList.apply(jsonCard.getTraits()));

        newCard.setInfluence(jsonCard.getInfluence_pool());

        return newCard;
    };

    /**
     * Función que convierte una JsonCard en una RoleCard
     */
    public ThreeParametersLambda<JsonCard,Gson, RoleCard> jsonCardToRoleCard = (jsonCard,gson) -> {
        RoleCard newCard = new RoleCard();

        Type type = new TypeToken<List<JsonPackCards>>() {
        }.getType();
        List<JsonPackCards> packCards = gson.fromJson(jsonCard.getPack_cards(), type);

        utils.setNameCard(packCards, newCard);

        newCard.setIdFiveRingsDB(jsonCard.getId());

        String name = jsonCard.getName();
        newCard.setName(name);
        newCard.setIsRestricted(jsonCard.getIs_restricted());
        newCard.setUnicity(jsonCard.getUnicity());
        newCard.setName_extra(jsonCard.getName_extra());

        newCard.setTraits(converter.jsonArrayToList.apply(jsonCard.getTraits()));

        JsonArray traits = jsonCard.getTraits();

        if (name.contains("Support")) {
            newCard.setClan(traits.get(0).toString().substring(1, traits.get(0).toString().length() - 1));
            newCard.setRole(Constants.SUPPORT);
        } else {
            newCard.setRole(traits.get(0).toString().substring(1, traits.get(0).toString().length() - 1));
            newCard.getElements().add(traits.get(1).toString().substring(1, traits.get(1).toString().length() - 1));
            newCard.setClan(Constants.NEUTRAL);
        }

        return newCard;
    };

    /**
     * Función que convierte una JsonCard en una ConflictCard
     */
    public ThreeParametersLambda<JsonCard,Gson, ConflictCard> jsonCardToConflictCard = (jsonCard,gson) -> {
        ConflictCard newCard = new ConflictCard();
        Type type = new TypeToken<List<JsonPackCards>>() {
        }.getType();
        List<JsonPackCards> packCards = gson.fromJson(jsonCard.getPack_cards(), type);

        utils.setNameCard(packCards, newCard);

        newCard.setIdFiveRingsDB(jsonCard.getId());
        newCard.setDeckLimit(jsonCard.getDeck_limit());
        newCard.setName(jsonCard.getName());
        newCard.setClan(jsonCard.getClan());
        newCard.setIsRestricted(jsonCard.getIs_restricted());
        newCard.setUnicity(jsonCard.getUnicity());
        newCard.setName_extra(jsonCard.getName_extra());

        newCard.setTraits(converter.jsonArrayToList.apply(jsonCard.getTraits()));
        newCard.setAllowed_clans(converter.jsonArrayToList.apply(jsonCard.getAllowed_clans()));


        if (jsonCard.getRole_restriction() != null)
            utils.elementAndRoleRestrictions(newCard, jsonCard.getRole_restriction());

        if (Constants.CHARACTER.equalsIgnoreCase(jsonCard.getType()))
            newCard.setCharacter(Boolean.TRUE);
        else
            newCard.setCharacter(Boolean.FALSE);


        if (jsonCard.getInfluence_cost() != null)
            newCard.setInfluence(jsonCard.getInfluence_cost());


        return newCard;
    };

}
