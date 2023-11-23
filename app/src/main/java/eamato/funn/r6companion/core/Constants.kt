package eamato.funn.r6companion.core

const val LOGGER_TAG = "R6CompanionTAG"
const val IMAGE_LOGGER_TAG = "ImageRequest"

const val OPERATORS_ASSETS_FILE_NAME = "operators.json"
const val OPERATORS_LOCAL_FILE_NAME = "operators.txt"

const val FIREBASE_REMOTE_CONFIG_OPERATORS = "operators"
const val FIREBASE_REMOTE_CONFIG_OUR_MISSION = "our_mission"
const val FIREBASE_REMOTE_CONFIG_OUR_TEAM = "our_team"
const val FIREBASE_REMOTE_CONFIG_COMING_SOON = "coming_soon"

const val CONNECTION_TIME_OUT = 5 * 1000L
const val READ_TIME_OUT = 5 * 1000L
const val WRITE_TIME_OUT = 5 * 1000L

const val ROULETTE_OPERATOR_IMAGE_WIDTH = 300
const val ROULETTE_OPERATOR_IMAGE_HEIGHT = 300

const val ROULETTE_OPERATOR_ICON_WIDTH = 75
const val ROULETTE_OPERATOR_ICON_HEIGHT = 75

val NEGATIVE = floatArrayOf(
    -1.0f, 0f, 0f, 0f, 255f,    // red
    0f, -1.0f, 0f, 0f, 255f,    // green
    0f, 0f, -1.0f, 0f, 255f,    // blue
    0f, 0f, 0f, 1.0f, 0f        // alpha
)

const val NEWS_HOST = "https://nimbus.ubisoft.com/"
const val NEWS_PATH = "api/v1/items"

const val UBISOFT_GRAPH_URL = "https://cms-cache.ubisoft.com/GraphQL/content/v1/spaces/p0f8o8d25gmk"
const val UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER =
    "Authorization"
const val UBISOFT_GRAPH_AUTHORIZATION_TOKEN_HEADER_VALUE =
    "Bearer pYflq4nCMNCwz7yxsA1k7UNg1E7SQCRjI4PtDUpsOeg"
const val UBISOFT_GRAPH_APP_NAME_HEADER = "Ubi-Appname"
const val UBISOFT_GRAPH_APP_NAME_HEADER_VALUE = "RainbowSixSiege"
const val UBISOFT_GRAPH_APP_ID_HEADER = "Ubi-Appid"
const val UBISOFT_GRAPH_APP_ID_HEADER_VALUE = "685a3038-2b04-47ee-9c5a-6403381a46aa"

const val NEWS_AUTHORIZATION_TOKEN_HEADER =
    "authorization: 3u0FfSBUaTSew-2NVfAOSYWevVQHWtY9q3VM8Xx9Lto"

const val NEWS_SKIP_PARAM_KEY = "skip"
const val NEWS_COUNT_PARAM_KEY = "limit"
const val NEWS_LOCALE_PARAM_KEY = "locale"
const val NEWS_TAG_PARAM_KEY = "tags"
const val NEWS_CATEGORIES_FILTER_PARAM_KEY = "categoriesFilter"

const val NEWS_COUNT_DEFAULT_VALUE = 60
const val ENGLISH_NEWS_LOCALE = "en-us"
const val RUSSIAN_NEWS_LOCALE = "ru-ru"
const val DEFAULT_NEWS_LOCALE = ENGLISH_NEWS_LOCALE
val NEWS_LOCALES = listOf(
    ENGLISH_NEWS_LOCALE,
    RUSSIAN_NEWS_LOCALE
)
const val NEWS_TAG_PARAM_R6_VALUE = "BR-rainbow-six GA-siege"

const val NEWS_PREFETCH_DISTANCE = NEWS_COUNT_DEFAULT_VALUE / 4
const val NEWS_MAX_PAGE_SIZE = 3 * NEWS_COUNT_DEFAULT_VALUE

const val AD_INSERTION_COUNT = 15

const val NEWS_SOURCE_PATTERN_1 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)"
const val NEWS_SOURCE_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val NEWS_RESULT_PATTEN = "dd-MM-yyyy"

const val PREFERENCE_KEY_USER_SETTINGS = "user_settings"

const val OPERATORS_LIST_GRID_COUNT_PORTRAIT = 3
const val OPERATORS_LIST_GRID_COUNT_LANDSCAPE = 5

const val NEWS_LIST_GRID_COUNT_PORTRAIT = 1
const val NEWS_LIST_GRID_COUNT_LANDSCAPE = 2

const val SETTINGS_ITEM_SCREEN_FRAGMENT_TAG = "settings_item_screen_fragment_tag"
const val SETTINGS_ITEM_SCREEN_ROUTE_NAME = "settings_item_screen_fragment_rote"

const val SETTINGS_ITEM_APP_LANGUAGE_ID = "app_language"
const val SETTINGS_ITEM_NEWS_LANGUAGE_ID = "news_language"
const val SETTINGS_ITEM_SAME_LANGUAGE_ID = "same_language"
const val SETTINGS_ITEM_USE_MOBILE_NETWORK_ID = "use_mobile_network"
const val SETTINGS_ITEM_ABOUT_SCREEN_ID = "about"

const val PROPERTY_NAME_TEXT_SIZE = "textSize"

const val COMPANION_SCREEN_ID_OPERATORS = 1
const val COMPANION_SCREEN_ID_WEAPONS = 2
const val COMPANION_SCREEN_ID_MAPS = 3

const val COMPANION_BUTTONS_ANIMATION_DURATION = 300L

const val MAPS_COUNT_DEFAULT_VALUE = 60
const val MAPS_PREFETCH_DISTANCE = MAPS_COUNT_DEFAULT_VALUE / 4
const val MAPS_MAX_PAGE_SIZE = 3 * MAPS_COUNT_DEFAULT_VALUE

const val OUR_TEAM_IMAGE_WIDTH = 75
const val OUR_TEAM_IMAGE_HEIGHT = 75

const val WINNER_OPERATOR_IMAGE_WIDTH = 600
const val WINNER_OPERATOR_IMAGE_HEIGHT = 600