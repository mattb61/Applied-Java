package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class OrderItemRefund {
    private int orderItemRefundId;
    private int orderItemId;
    private int orderId;
    private double refundAmountUsd;
    private Timestamp createdAt;

    private OrderItemRefund(int order_item_refund_id, int order_item_id, int order_id, double refund_amount_usd,
            Timestamp created_at) {
        this.orderItemRefundId = order_item_refund_id;
        this.orderItemId = order_item_id;
        this.orderId = order_id;
        this.refundAmountUsd = refund_amount_usd;
        this.createdAt = created_at;
    }

    private static class Builder {
        private int orderItemRefundId;
        private int orderItemId;
        private int orderId;
        private double refundAmountUsd;
        private Timestamp createdAt;

        public Builder setOrderItemRefundId(int order_item_refund_id) {
            this.orderItemRefundId = order_item_refund_id;
            return this;
        }
        public Builder setOrderItemId(int order_item_id) {
            this.orderItemId = order_item_id;
            return this;
        }
        public Builder setOrderId(int order_id) {
            this.orderId = order_id;
            return this;
        }
        public Builder setRefundAmountUsd(double refund_amount_usd) {
            this.refundAmountUsd = refund_amount_usd;
            return this;
        }
        public Builder setCreatedAt(Timestamp created_at) {
            this.createdAt = created_at;
            return this;
        }
        
        public OrderItemRefund create() {
            return new OrderItemRefund(
                orderItemRefundId, 
                orderItemId, 
                orderId, 
                refundAmountUsd, 
                createdAt
            );
        }
    }

    private static class OrderItemRefundCsvTable extends CsvTable<OrderItemRefund> {

        public OrderItemRefundCsvTable(String path) {
            super(path);
        }

        @Override
        protected OrderItemRefund createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setOrderItemRefundId(Integer.parseInt(record.get("order_item_refund_id")));
            builder.setOrderItemId(Integer.parseInt(record.get("order_item_id")));
            builder.setOrderId(Integer.parseInt(record.get("order_id")));
            builder.setRefundAmountUsd(Double.parseDouble(record.get("refund_amount_usd")));
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            return builder.create();
        }
        
    }

    public int getOrderItemRefundId() {
        return orderItemRefundId;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getRefundAmountUsd() {
        return refundAmountUsd;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public static final CsvTable<OrderItemRefund> csvTable = new OrderItemRefundCsvTable("order_item_refunds.csv");
}
