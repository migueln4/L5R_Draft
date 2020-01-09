package utils;

import cards.Card;
import cards.JsonPackCards;
import constants.Constants;
import utils.interfaces.ThreeParametersLambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import static constants.Constants.NEUTRAL;
import static constants.Constants.NULL;

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
     *      - Clan
     *      - Conflicto
     *      - Elemento
     *      - Rasgo
     * - Apellidos de una familia en su nombre.
     * - Cosas concretas de la carta, en el Array (elementos, allowed clans) --> Falta meterlo en la llamada original.
     */
    public List<String> getAllTraitsFromCard(String text,String clan, String role_limit, String element_limit, List<String> current_traits, List<String> others) {
        List<String> traits = new ArrayList<>(current_traits);
        if(strValid(clan) && clan != NEUTRAL && !isPresentInList(traits,clan))
            traits.add(clan);
        if(strValid(role_limit) && !isPresentInList(traits,role_limit))
            traits.add(role_limit);
        if(strValid(element_limit) && !isPresentInList(traits,element_limit))
            traits.add(element_limit);
        compareTraitList(traits,others);
        return null;
    }

    private boolean isPresentInList(List<String> list, String str) {
        return list.contains(str);
    }

    private boolean strValid(String str) {
        return (!str.isEmpty() && str != null && !str.equalsIgnoreCase(NULL));
    }

    private void compareTraitList(List<String> traits, List<String> others) {
        others.stream().filter(str -> !isPresentInList(traits,str)).forEach(str -> traits.add(str));
    }

}
