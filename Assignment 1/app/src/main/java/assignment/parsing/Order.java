package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class Order {
    private int orderId;
    private Timestamp createdAt;
    private int websiteSessionId;
    private int userId;
    private int primaryProductId;
    private int itemsPurchased;
    private double priceUsd;
    private double cogsUsd;

    private Order(int orderId, Timestamp createdAt, int websiteSessionId, int userId, int primaryProductId,
            int itemsPurchased, double priceUsd, double cogsUsd) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.websiteSessionId = websiteSessionId;
        this.userId = userId;
        this.primaryProductId = primaryProductId;
        this.itemsPurchased = itemsPurchased;
        this.priceUsd = priceUsd;
        this.cogsUsd = cogsUsd;
    }
    
    private static class Builder {
        private int orderId;
        private Timestamp createdAt;
        private int websiteSessionId;
        private int userId;
        private int primaryProductId;
        private int itemsPurchased;
        private double priceUsd;
        private double cogsUsd;

        public Builder setOrderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setWebsiteSessionId(int websiteSessionId) {
            this.websiteSessionId = websiteSessionId;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPrimaryProductId(int primaryProductId) {
            this.primaryProductId = primaryProductId;
            return this;
        }

        public Builder setItemsPurchased(int itemsPurchased) {
            this.itemsPurchased = itemsPurchased;
            return this;
        }

        public Builder setPriceUsd(double priceUsd) {
            this.priceUsd = priceUsd;
            return this;
        }

        public Builder setCogsUsd(double cogsUsd) {
            this.cogsUsd = cogsUsd;
            return this;
        }
        
        public Order create(){
            return new Order(
                this.orderId,
                this.createdAt,
                this.websiteSessionId,
                this.userId,
                this.primaryProductId,
                this.itemsPurchased,
                this.priceUsd,
                this.cogsUsd
            );
        }
    }

    private static class OrderCsvTable extends CsvTable<Order>{
        public OrderCsvTable(String path) {
            super(path);
        }

        @Override
        protected Order createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setOrderId(Integer.parseInt(record.get("order_id")));
            builder.setWebsiteSessionId(Integer.parseInt(record.get("website_session_id")));
            builder.setUserId(Integer.parseInt(record.get("user_id")));
            builder.setPrimaryProductId(Integer.parseInt(record.get("primary_product_id")));
            builder.setItemsPurchased(Integer.parseInt(record.get("items_purchased")));
            builder.setPriceUsd(Double.parseDouble(record.get("price_usd")));
            builder.setCogsUsd(Double.parseDouble(record.get("cogs_usd")));
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            return builder.create();
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getWebsiteSessionId() {
        return websiteSessionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPrimaryProductId() {
        return primaryProductId;
    }

    public int getItemsPurchased() {
        return itemsPurchased;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public double getCogsUsd() {
        return cogsUsd;
    }
    
    public static final CsvTable<Order> csvTable = new OrderCsvTable("orders.csv");

}
