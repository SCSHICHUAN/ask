package Models;

public class User {
    public String id;
    public String name;
    public String tell;
    public String yzm;
    public String conpany;
    public String post;

    public User(String id,String name, String tell, String yzm, String conpany, String post) {
        this.id = id;
        this.name = name;
        this.tell = tell;
        this.yzm = yzm;
        this.conpany = conpany;
        this.post = post;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", tell='" + tell + '\'' +
                ", yzm='" + yzm + '\'' +
                ", conpany='" + conpany + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
