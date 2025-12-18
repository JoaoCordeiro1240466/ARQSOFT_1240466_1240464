package pt.psoft.g1.psoftg1.shared.model;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class StringUtilsCustom {

    public static String sanitizeHtml(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean(html, Safelist.none());
    }
}
