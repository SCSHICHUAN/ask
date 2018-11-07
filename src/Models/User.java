package Models;

public class User {
    public String name;
    public String tell;
    public String yzm;
    public String conpany;
    public String post;

    public User(String name, String tell, String yzm, String conpany, String post) {
        this.name = name;
        this.tell = tell;
        this.yzm = yzm;
        this.conpany = conpany;
        this.post = post;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", tell='" + tell + '\'' +
                ", yzm='" + yzm + '\'' +
                ", conpany='" + conpany + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
