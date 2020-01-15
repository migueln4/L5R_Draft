package utils;

import cards.Card;
import cards.JsonPackCards;
import constants.Constants;
import utils.interfaces.ThreeParametersLambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.Constants.*;

public final class Utils {

    private static final Scanner READ_CONSOLE = new Scanner(System.in);
    private static final Utils utils = new Utils();

    private Utils() {
    }

    public static Utils getInstance() {
        return utils;
    }

    public static int readInteger(int min, int max) {
        int n = READ_CONSOLE.nextInt();
        while (n < min || n > max) {
            System.out.println("You must to choose a number between " + min + " and " + max + ".");
            n = READ_CONSOLE.nextInt();
        }
        return n;
    }

    public static int[] createArrayNumbers(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++)
            result[i] = i;
        return result;
    }

    /**
     * Función que construye el nombre de una carta.
     */
    public ThreeParametersLambda<String, String, String> getCardName = (name, number) -> {
        StringBuilder strb = new StringBuilder();
        strb.append(this.getPackName.apply(name));
        strb.append(" - #");
        strb.append(number);
        return strb.toString();
    };

    /**
     * Función que devuelve el nombre del paquete.
     */
    private Function<String, String> getPackName = str -> {
        switch (str) {
            case "core":
                return "CORE";
            case "tears-of-amaterasu":
            case "for-honor-and-glory":
            case "into-the-forbidden-city":
            case "the-chrysanthemum-throne":
            case "fate-has-no-secrets":
            case "meditations-on-the-ephemeral":
                return "IMPERIAL CYCLE";
            case "breath-of-the-kami":
            case "tainted-lands":
            case "the-fires-within":
            case "the-ebb-and-flow":
            case "all-and-nothing":
            case "elements-unbound":
                return "ELEMENTAL CYCLE";
            case "for-the-empire":
            case "bonds-of-blood":
            case "justice-for-satsume":
            case "the-children-of-heaven":
            case "a-champion-s-foresight":
            case "shoju-s-duty":
                return "INHERITANCE CYCLE";
            case "warriors-of-the-wind":
                return "UNICORN CLAN PACK";
            case "masters-of-the-court":
                return "CRANE CLAN PACK";
            case "disciples-of-the-void":
                return "PHOENIX CLAN PACK";
            case "underhand-of-the-emperor":
                return "SCORPION CLAN PACK";
            case "seekers-of-wisdom":
                return "DRAGON CLAN PACK";
            case "defenders-of-rokugan":
                return "CRAB CLAN PACK";
            case "the-emperor-s-legion":
                return "LION CLAN PACK";
            case "children-of-the-empire":
                return "CHILDREN OF THE EMPIRE";
            default:
                return null;
        }
    };

    /**
     * Nombra correctamente una carta
     */
    public void setNameCard(List<JsonPackCards> packCards, Card newCard) {
        String nameCard = "";
        int pos = -1;
        for (JsonPackCards jsonPackCards : packCards) {
            pos++;
            String namePack = jsonPackCards.getPack().get("id").toString();
            if (!"\"gen-con-2017\"".equals(namePack)
                    && !"\"2018-season-one-stronghold-kit\"".equals(namePack)
                    && !"\"battle-for-the-stronghold-kit\"".equals(namePack)) {
                nameCard = namePack;
                break;
            }
        }

        nameCard = nameCard.substring(1, nameCard.length() - 1);

        if ("core".equals(nameCard))
            newCard.setQuantity((packCards.get(pos).getQuantity()) * 3);
        else
            newCard.setQuantity(packCards.get(pos).getQuantity());

        newCard.setId(utils.getCardName.apply(nameCard, packCards.get(pos).getPosition()));
    }

    /**
     * Aporta las restricciones precisas para una carta.
     */
    public void elementAndRoleRestrictions(Card newCard, String role) {
        switch (role) {
            case Constants.KEEPER:
                newCard.setRoleLimit(Constants.KEEPER);
                break;
            case Constants.SEEKER:
                newCard.setRoleLimit(Constants.SEEKER);
                break;
            case Constants.AIR:
                newCard.setElementLimit(Constants.AIR);
                break;
            case Constants.WATER:
                newCard.setElementLimit(Constants.WATER);
                break;
            case Constants.FIRE:
                newCard.setElementLimit(Constants.FIRE);
                break;
            case Constants.VOID:
                newCard.setElementLimit(Constants.VOID);
                break;
            case Constants.EARTH:
                newCard.setElementLimit(Constants.EARTH);
                break;
            default:
                break;
        }
    }

    /**
     * Devuelve una lista con todos los rasgos que debe tener una carta para crear un mazo temático:
     * - Rasgos -> OK
     * - Clan -> OK
     * - Restricciones de elemento -> OK
     * - Restricciones de rol -> OK
     * - Palabras clave en su texto:
     *      - Clan -> OK
     *      - Conflicto  -> OK
     *      - Elemento  -> OK
     *      - Rasgo -> OK
     * - Apellidos de una familia en su nombre. -> Tengo que hacer la lista
     * - Lista de elementos -> OK
     * - Lista de clanes admitidos: Si permite todos, no se añade ninguno -> OK
     */
    public List<String> getAllTraitsFromCard(String cardName, String text,String clan, String role_limit, String element_limit, List<String> current_traits, List<String> elementList, List<String> allowedClans) {
        List<String> traits = new ArrayList<>(current_traits);
        if(strValid(clan) && !clan.equalsIgnoreCase(NEUTRAL) && !traits.contains(clan))
            traits.add(clan);
        if(strValid(role_limit) && !traits.contains(role_limit))
            traits.add(role_limit);
        if(strValid(element_limit) && !traits.contains(element_limit))
            traits.add(element_limit);
        if(!element_limit.isEmpty())
            elementList.stream().filter(elem -> !traits.contains(elem)).forEach(traits::add);
        if(!allowedClans.isEmpty() && allowedClans.size() < 7)
            allowedClans.stream().filter(aClan -> !traits.contains(aClan)).forEach(traits::add);
        checkKeywords(traits,text);
        checkTraitsInText(traits,text);

        return traits;
    }

    private void checkTraitsInText(List<String> traits, String text) {
        Pattern pattern = Pattern.compile(EM_OPEN);
        Matcher matcher = pattern.matcher(text);
        if(matcher.matches()) {
            String[] split = text.split(pattern.pattern());
            for(String piece : split) {
                if(piece.contains(EM_CLOSE)) {
                    int close = piece.indexOf(EM_CLOSE);
                    String newTrait = piece.substring(0, close);
                    if(!traits.contains(newTrait))
                        traits.add(newTrait);
                }
            }
        }
    }

    private void checkKeywords(List<String> traits, String text) {
        Pattern[] patterns = new Pattern[] {Pattern.compile(CLAN_OPEN),Pattern.compile(CONFLICT_OPEN),Pattern.compile(ELEMENT_OPEN)};
        for(Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if(matcher.matches()) {
                String[] split =  text.split(pattern.pattern());
                for(String piece : split) {
                    if(piece.contains(KEYWORD_CLOSE)) {
                        int close = piece.indexOf(KEYWORD_CLOSE);
                        String newTrait = piece.substring(0, close);
                        if(!traits.contains(newTrait))
                            traits.add(newTrait);
                    }
                }
            }
        }
    }

    private boolean strValid(String str) {
        return ( str != null && !str.isEmpty() && !str.equalsIgnoreCase(NULL));
    }

    private void compareTraitList(List<String> traits, List<String> others) {
    others.stream().filter(str -> !traits.contains(str)).forEach(traits::add);
    }

}
