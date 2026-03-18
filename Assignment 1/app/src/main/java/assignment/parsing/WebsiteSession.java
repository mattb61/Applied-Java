package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class WebsiteSession {

    private int websiteSessionId;
    private int userId;
    private int isRepeatSession;
    private String utmSource;
    private String utmCampaign;
    private String utmContent;
    private String deviceType;
    private String httpReferer;
    private Timestamp createdAt;

    private WebsiteSession(int websiteSessionId, int userId, int isRepeatSession, String utmSource, String utmCampaign,
            String utmContent, String deviceType, String httpReferer, Timestamp createdAt) {
        this.websiteSessionId = websiteSessionId;
        this.userId = userId;
        this.isRepeatSession = isRepeatSession;
        this.utmSource = utmSource;
        this.utmCampaign = utmCampaign;
        this.utmContent = utmContent;
        this.deviceType = deviceType;
        this.httpReferer = httpReferer;
        this.createdAt = createdAt;
    }

    private static class Builder {

        private int websiteSessionId;
        private int userId;
        private int isRepeatSession;
        private String utmSource;
        private String utmCampaign;
        private String utmContent;
        private String deviceType;
        private String httpReferer;
        private Timestamp createdAt;

        public Builder setWebsiteSessionId(int websiteSessionId) {
            this.websiteSessionId = websiteSessionId;
            return this;
        }
        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }
        public Builder setIsRepeatSession(int isRepeatSession) {
            this.isRepeatSession = isRepeatSession;
            return this;
        }
        public Builder setUtmSource(String utmSource) {
            this.utmSource = utmSource;
            return this;
        }
        public Builder setUtmCampaign(String utmCampaign) {
            this.utmCampaign = utmCampaign;
            return this;
        }
        public Builder setUtmContent(String utmContent) {
            this.utmContent = utmContent;
            return this;
        }
        public Builder setDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }
        public Builder setHttpReferer(String httpReferer) {
            this.httpReferer = httpReferer;
            return this;
        }
        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WebsiteSession create() {
            return new WebsiteSession(
                websiteSessionId, 
                userId, 
                isRepeatSession, 
                utmSource, 
                utmCampaign, 
                utmContent, 
                deviceType, 
                httpReferer, 
                createdAt
            );
        }
    }

    private static class WebsiteSessionCsvTable extends CsvTable<WebsiteSession>{

        public WebsiteSessionCsvTable(String path) {
            super(path);
        }

        @Override
        protected WebsiteSession createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            builder.setWebsiteSessionId(Integer.parseInt(record.get("website_session_id")));
            builder.setUserId(Integer.parseInt(record.get("user_id")));
            builder.setIsRepeatSession(Integer.parseInt(record.get("is_repeat_session")));
            builder.setUtmSource(record.get("utm_source"));
            builder.setUtmCampaign(record.get("utm_campaign"));
            builder.setUtmContent(record.get("utm_content"));
            builder.setDeviceType(record.get("device_type"));
            builder.setHttpReferer(record.get("http_referer"));
            return builder.create();
        }
    }

    public int getWebsiteSessionId() {
        return websiteSessionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getIsRepeatSession() {
        return isRepeatSession;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public String getUtmContent() {
        return utmContent;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getHttpReferer() {
        return httpReferer;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public static final CsvTable<WebsiteSession> csvTable = new WebsiteSessionCsvTable("website_sessions.csv");
}
