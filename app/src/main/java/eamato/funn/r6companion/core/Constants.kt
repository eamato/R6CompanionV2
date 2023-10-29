package eamato.funn.r6companion.core

const val LOGGER_TAG = "R6CompanionTAG"
const val IMAGE_LOGGER_TAG = "ImageRequest"

const val OPERATORS_ASSETS_FILE_NAME = "operators.json"
const val OPERATORS_LOCAL_FILE_NAME = "operators.txt"

const val FIREBASE_REMOTE_CONFIG_OPERATORS = "operators"

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
const val NEWS_TAG_PARAM_R6_VALUE = "BR-rainbow-six GA-siege"

const val NEWS_PREFETCH_DISTANCE = NEWS_COUNT_DEFAULT_VALUE / 4
const val NEWS_MAX_PAGE_SIZE = 3 * NEWS_COUNT_DEFAULT_VALUE

const val AD_INSERTION_COUNT = 15

const val NEWS_SOURCE_PATTERN_1 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)"
const val NEWS_SOURCE_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val NEWS_RESULT_PATTEN = "dd-MM-yyyy"
