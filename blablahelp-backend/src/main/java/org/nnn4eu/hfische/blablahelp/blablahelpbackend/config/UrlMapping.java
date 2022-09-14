package org.nnn4eu.hfische.blablahelp.blablahelpbackend.config;

public final class UrlMapping {
    private UrlMapping() {
        throw new IllegalStateException("UrlMapping Utility class");
    }

    public static final String PUBLIC = "/api/sp";
    public static final String BASIC = "/api/sb";
    public static final String USERDATA = "/api/sb/userdata";
    public static final String ADMIN = "/api/sa";

}
