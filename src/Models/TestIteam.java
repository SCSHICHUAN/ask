package Models;

public class TestIteam {
    public String id;
    public String category;
    public String title;
    public String A;
    public String B;
    public String C;
    public String D;
    public String answer;

    public TestIteam(String id, String category, String title, String a, String b, String c, String d, String answer) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.A = a;
        this.B = b;
        this.C = c;
        this.D = d;
        this.answer = answer;
    }


    public void showString() {

        System.out.println("id:" + id
                + " \ncategory:" + category
                + " \ntitle:" + title
                + " \nA:" + A
                + " \nB:" + B
                + " \nC:" + C
                + " \nD:" + D
                + " \nanswer:" + answer
        );


    }
}
