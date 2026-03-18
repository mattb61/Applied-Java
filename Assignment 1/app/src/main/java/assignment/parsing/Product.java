package assignment.parsing;

import java.sql.Timestamp;

import org.apache.commons.csv.CSVRecord;

public class Product {
    private int productId;
    private Timestamp createdAt;
    private String productName;

    private Product(
        int productId,
        Timestamp createdAt,
        String productName
    ) {
        this.productId = productId;
        this.createdAt = createdAt;
        this.productName = productName;
    }
    
    private static class Builder {
        private int productId;
        private Timestamp createdAt;
        private String productName;
        public Product create(){
            return new Product(
                productId,
                createdAt,
                productName
            );
        }
        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }
        public Builder setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }
        
    }

    private static class ProductCsvTable extends CsvTable<Product>{
        public ProductCsvTable(String path) {
            super(path);
        }

        @Override
        protected Product createObject(CSVRecord record) {
            Builder builder = new Builder();
            builder.setProductId(Integer.parseInt(record.get("product_id")));
            builder.setCreatedAt(Timestamp.valueOf(record.get("created_at")));
            builder.setProductName(record.get("product_name"));
            return builder.create();
        }
    }

    public static final CsvTable<Product> csvTable = new ProductCsvTable("products.csv");

    public int getProductId() {
        return productId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getProductName() {
        return productName;
    }

    public static CsvTable<Product> getCsvtable() {
        return csvTable;
    }
}
