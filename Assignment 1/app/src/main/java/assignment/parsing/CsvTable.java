package assignment.parsing;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public abstract class CsvTable<O> {
    private final String path;
    private List<O> contents;
    private boolean loaded;

    public CsvTable(String path){
        this.path = path;
        this.contents = new ArrayList<>();
        this.loaded = false;
    }

    protected abstract O createObject(CSVRecord record);

    public boolean readTable(){
        try (
            Reader reader = new InputStreamReader(CsvTable.class.getClassLoader().getResourceAsStream(path));
            CSVParser parser = CSVParser.builder().setReader(reader).setFormat(CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).get()).get();
        ) {
            for(CSVRecord record : parser){
                contents.add(createObject(record));
            }
            this.loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.loaded;
    }

    public void forEach(SQLInserter<O> consumer) throws IllegalStateException, SQLException {
        if (!loaded) 
            throw new IllegalStateException("Cannot process table before reading file");
        int i = 0;
        for(O object : contents){
            consumer.accept(object, i++);
        }
    }
}
