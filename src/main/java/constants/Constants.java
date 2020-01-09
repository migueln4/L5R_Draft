package constants;

public class Constants {
/* Constants about the api */
    public static final String RRG_VERSION = "rrg-version";
    public static final String RECORDS = "records";
    public static final String SUCCESS = "success";
    public static final String SIZE = "size";

/* Constants about elements */
    public static final String VOID = "void";
    public static final String EARTH = "earth";
    public static final String WATER = "water";
    public static final String AIR  = "air";
    public static final String FIRE = "fire";
    public static final String[] ELEMENTS = {"void", "air", "water", "fire", "earth"};
    public static final String ALL_ELEMENTS = "all";

/* Constants about clans */
    public static final String LION = "lion";
    public static final String SCORPION = "scorpion";
    public static final String PHOENIX = "phoenix";
    public static final String CRANE = "crane";
    public static final String CRAB = "crab";
    public static final String UNICORN = "unicorn";
    public static final String DRAGON = "dragon";
    public static final String NEUTRAL = "neutral";
    public static final String[] CLANS = {"lion", "scorpion", "phoenix", "crane", "crab", "unicorn", "dragon"};
    public static final String[] FAMILIES = {};

/* Constants about roles */
    public static final String SUPPORT = "support";
    public static final String KEEPER = "keeper";
    public static final String SEEKER = "seeker";
    public static final String ROLE = "role";

/* Constants about the game */
    public static final Integer NUMBER_OPTIONS = 3;
    public static final Integer MAX_CONFLICT_CARDS = 45;
    public static final Integer MIN_CONFLICT_CARDS = 40;
    public static final Integer MAX_CONFLICT_CHARACTERS = 10;
    public static final Integer MAX_PROVINCE_CARDS = 5;
    public static final Integer MAX_CARD_COPIES = 3;
    public static final String CONFLICT = "Conflict";
    public static final String DYNASTY = "Dynasty";
    public static final String PROVINCE = "Province";
    public static final String STRONGHOLD = "Stronghold";
    public static final String CHARACTER = "character";

    /* Discard packs */
    public static final String[] DISCARD_PACKS = {};

    /* Others */
    public static final String NULL = "null";
    public static final String TRAIT = "Trait";
    public static final String TRAITS = "Traits";
    public static final String EM_OPEN = "<em>";
    public static final String EM_CLOSE = "</em>";
    public static final String CONFLICT_OPEN = "[conflict-";
    public static final String ELEMENT_OPEN = "[element-";
    public static final String CLAN_OPEN = "[clan-";
}
