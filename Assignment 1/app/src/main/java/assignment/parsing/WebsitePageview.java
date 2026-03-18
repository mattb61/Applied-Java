package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class WebsitePageview {
    private int websitePageviewId;
    private int websiteSessionId;
    private String pageviewUrl;
    private Timestamp createdAt;

    public WebsitePageview(int websitePageviewId, int websiteSessionId, String pageviewUrl, Timestamp createdAt) {
        this.websitePageviewId = websitePageviewId;
        this.websiteSessionId = websiteSessionId;
        this.pageviewUrl = pageviewUrl;
        this.createdAt = createdAt;
    }

    private static class Builder {
        private int websitePageviewId;
        private int websiteSessionId;
        private String pageviewUrl;
        private Timestamp createdAt;

        public Builder setWebsitePageviewId(int websitePageviewId) {
            this.websitePageviewId = websitePageviewId;
            return this;
        }
        public Builder setWebsiteSessionId(int websiteSessionId) {
            this.websiteSessionId = websiteSessionId;
            return this;
        }
        public Builder setPageviewUrl(String pageviewUrl) {
            this.pageviewUrl = pageviewUrl;
            return this;
        }
        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public WebsitePageview create(){
            return new WebsitePageview(
                websitePageviewId, 
                websiteSessionId, 
                pageviewUrl, 
                createdAt
            );
        }
    }

    private static class WebsitePageviewCsvTable extends CsvTable<WebsitePageview>{

        public WebsitePageviewCsvTable(String path) {
            super(path);
        }

        @Override
        protected WebsitePageview createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setWebsitePageviewId(Integer.parseInt(record.get("website_pageview_id")));
            builder.setWebsiteSessionId(Integer.parseInt(record.get("website_session_id")));
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            builder.setPageviewUrl(record.get("pageview_url"));
            return builder.create();
        }
    }

    public int getWebsitePageviewId() {
        return websitePageviewId;
    }

    public int getWebsiteSessionId() {
        return websiteSessionId;
    }

    public String getPageviewUrl() {
        return pageviewUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public static final CsvTable<WebsitePageview> csvTable = new WebsitePageviewCsvTable("website_pageviews.csv");
}
