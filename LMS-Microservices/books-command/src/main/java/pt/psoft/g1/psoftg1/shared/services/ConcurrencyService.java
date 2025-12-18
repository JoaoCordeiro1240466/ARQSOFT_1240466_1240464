package pt.psoft.g1.psoftg1.shared.services;

import org.springframework.stereotype.Service;

@Service
public class ConcurrencyService {

    public static final String IF_MATCH = "If-Match";

    public Long getVersionFromIfMatchHeader(String ifMatchHeader) {
        if (ifMatchHeader == null || ifMatchHeader.isEmpty()) {
            return null;
        }
        if (ifMatchHeader.startsWith("\"") && ifMatchHeader.endsWith("\"")) {
            ifMatchHeader = ifMatchHeader.substring(1, ifMatchHeader.length() - 1);
        }
        try {
            return Long.parseLong(ifMatchHeader);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid If-Match header value.");
        }
    }
}
