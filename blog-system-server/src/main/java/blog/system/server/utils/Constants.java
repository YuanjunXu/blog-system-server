package blog.system.server.utils;

public interface Constants {

    //pc端
    String FROM_PC = "p_";
    //mobile移动端
    String FROM_MOTILE = "m_";

    //app的下载路径
    String APP_DOWNLOAD_PATH = "/portal/app/";

    interface User {
        String ROLE_ADMIN = "role_admin";
        String ROLE_NORMAL = "role_normal";
        String DEFAULT_AVATAR = "https://cdn.sunofbeaches.com/images/default_avatar.png";
        String DEFAULT_STATE = "1";
        String COOKIE_TOKE_KEY = "sob_blog_token";
        //redis的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        String KEY_TOKEN = "key_token_";
        String KEY_COMMIT_TOKEN_RECORD = "key_commit_token_record_";
        String KEY_PC_LOGIN_ID = "key_pc_login_id_";
        String KEY_PC_LOGIN_STATE_FALSE = "false";
        String LAST_REQUEST_LOGIN_ID = "l_r_l_i";
        String LAST_CAPTCHA_ID = "l_c_i";
        int QR_CODE_STATE_CHECK_WAITING_TIME = 30;
    }

    interface ImageType {
        String PREFIX = "image/";
        String TYPE_JPG = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPG_WITH_PREFIX = PREFIX + "jpg";
        String TYPE_PNG_WITH_PREFIX = PREFIX + "png";
        String TYPE_GIF_WITH_PREFIX = PREFIX + "gif";
    }

    interface Settings {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
        String WEB_SIZE_TITLE = "web_size_title";
        String WEB_SIZE_DESCRIPTION = "web_size_description";
        String WEB_SIZE_KEYWORDS = "web_size_keywords";
        String WEB_SIZE_VIEW_COUNT = "web_size_view_count";
    }

    interface Page {
        int DEFAULT_PAGE = 1;
        int DEFAULT_SIZE = 1;
        int MAX_SIZE = 30;
    }

    /**
     * 单位是毫秒
     */
    interface TimeValueInMillions {
        long MIN = 60 * 1000;
        long HOUR = 60 * MIN;
        long HOUR_2 = 60 * MIN * 2;
        long DAY = 24 * HOUR;
        long WEEK = 7 * DAY;
        long MONTH = 30 * DAY;
    }

    /**
     * 单位是秒
     */
    interface TimeValueInSecond {
        int SECOND_10 = 10;
        int HALF_MIN = 30;
        int MIN = 60;
        int MIN_5 = 60 * 5;
        int MIN_15 = 60 * 15;
        int HOUR = 60 * MIN;
        int HOUR_2 = 60 * MIN * 2;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
    }

    interface Article {
        String TYPE_MARKDOWN = "1";
        String TYPE_RICH_TEXT = "0";
        int TITLE_MAX_LENGTH = 128;
        int SUMMARY_MAX_LENGTH = 256;
        //0表示删除、1表示已经发布、2表示草稿、3、表示置顶
        String STATE_DELETE = "0";
        String STATE_PUBLISH = "1";
        String STATE_DRAFT = "2";
        String STATE_TOP = "3";
        String KEY_ARTICLE_CACHE = "key_article_cache_";
        String KEY_ARTICLE_VIEW_COUNT = "key_article_view_count_";
        String KEY_ARTICLE_LIST_FIRST_PAGE = "key_article_list_first_page";
    }

    interface Comment {
        //1表示已经发布、3、表示置顶
        String STATE_PUBLISH = "1";
        String STATE_TOP = "3";
        String KEY_COMMENT_FIRST_PAGE_CACHE = "key_comment_first_page_cache_";
    }


}
