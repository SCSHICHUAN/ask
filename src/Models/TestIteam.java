package Models;

public class TestIteam {
    public String category;
    public String title;
    public String A;
    public String B;
    public String C;
    public String D;
    public String answer;

    public TestIteam(String category, String title, String a, String b, String c, String d, String answer) {
        this.category = category;
        this.title = title;
        this.A = a;
        this.B = b;
        this.C = c;
        this.D = d;
        this.answer = answer;
    }


    public void showString() {

        System.out.println(
                "category:" + category
                        + "  \ntitle:" + title
                        + " \nA:" + A
                        + " \nB:" + B
                        + " \nC:" + C
                        + " \nD:" + D
                        + " \nanswer:" + answer
        );


    }
}
