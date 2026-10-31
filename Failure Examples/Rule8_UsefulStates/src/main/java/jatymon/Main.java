package jatymon;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Rule8_FailProductive prodProto = new Rule8_FailProductive();
        Rule8_FailReachable reachProto = new Rule8_FailReachable();

        prodProto.m1();
        prodProto.m2();

        reachProto.m1();
        reachProto.m2();
    }
}