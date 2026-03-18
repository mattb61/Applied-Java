package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class OrderItem {
    private int orderItemId;
    private Timestamp createdAt;
    private int orderId;
    private int productId;
    private int isPrimaryItem;
    private double priceUsd;
    private double cogsUsd;
    
    private OrderItem(int orderItemId, Timestamp createdAt, int orderId, int productId, int isPrimaryItem,
            double priceUsd, double cogsUsd) {
        this.orderItemId = orderItemId;
        this.createdAt = createdAt;
        this.orderId = orderId;
        this.productId = productId;
        this.isPrimaryItem = isPrimaryItem;
        this.priceUsd = priceUsd;
        this.cogsUsd = cogsUsd;
    }

    private static class Builder {
        private int orderItemId;
        private Timestamp createdAt;
        private int orderId;
        private int productId;
        private int isPrimaryItem;
        private double priceUsd;
        private double cogsUsd;

        public OrderItem create(){
            return new OrderItem(
                orderItemId,
                createdAt,
                orderId,
                productId,
                isPrimaryItem,
                priceUsd,
                cogsUsd
            );
        }

        public Builder setOrderItemId(int orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder setOrderId(int orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setIsPrimaryItem(int isPrimaryItem) {
            this.isPrimaryItem = isPrimaryItem;
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
    }

    

    private static class OrderItemCsvTable extends CsvTable<OrderItem>{
        public OrderItemCsvTable(String path) {
            super(path);
        }

        @Override
        protected OrderItem createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setOrderItemId(Integer.parseInt(record.get("order_item_id")));
            builder.setOrderId(Integer.parseInt(record.get("order_id")));
            builder.setProductId(Integer.parseInt(record.get("product_id")));
            builder.setIsPrimaryItem(Integer.parseInt(record.get("is_primary_item")));
            builder.setPriceUsd(Double.parseDouble(record.get("price_usd")));
            builder.setCogsUsd(Double.parseDouble(record.get("cogs_usd")));
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            return builder.create();
        }
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getIsPrimaryItem() {
        return isPrimaryItem;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public double getCogsUsd() {
        return cogsUsd;
    }

    public static final CsvTable<OrderItem> csvTable = new OrderItemCsvTable("order_items.csv");
    
}
