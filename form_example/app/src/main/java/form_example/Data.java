package form_example;

public class Data {
    public final String uname;
    public final Status status;
    public Data(String name, Status status){
        this.uname = name;
        this.status = status;
    }
    public Data changeStatus(Status status){
        return new Data(uname, status);
    }
}
