package com.example.badcalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogicApp {

    private static final List<String> history = new ArrayList<>();
    private final ManagementFiles mf = new ManagementFiles();

    // show menu
    private void printMenu() {
        System.out.println("BAD CALC (Java very bad edition)");
        System.out.println("1:+ 2:- 3:* 4:/ 5:^ 6:% 7:LLM 8:hist 0:exit");
        System.out.print("opt: ");
    }

    // handle LLM or history options
    private boolean handleSpecialOptions(String opt, Scanner sc) {
        if ("7".equals(opt)) {
            System.out.println("Enter user template:");
            String tpl = sc.nextLine();
            System.out.println("Enter user input:");
            String uin = sc.nextLine();
            String sys = "System: You are an assistant.";
            String prompt = buildPrompt(sys, tpl, uin);
            String resp = sendToLLM(prompt);
            System.out.println("LLM RESP: " + resp);
            return true;
        }

        if ("8".equals(opt)) {
            for (String h : history) System.out.println(h);
            return true;
        }

        return false;
    }

    // run arithmetic operations
    private void processOperation(String opt, String a, String b) {
        String op = switch (opt) {
            case "1" -> "+";
            case "2" -> "-";
            case "3" -> "*";
            case "4" -> "/";
            case "5" -> "^";
            case "6" -> "%";
            default -> "";
        };

        double res = compute(a, b, op);

        String line = a + "|" + b + "|" + op + "|" + res;
        history.add(line);

        System.out.println("= " + res);
    }

    public void runApp() {
        // reduce cognitive complexity
        mf.initialFile();
        Scanner sc = new Scanner(System.in);

        while (true) {
            printMenu();
            String opt = sc.nextLine();

            if ("0".equals(opt)) break;

            if (handleSpecialOptions(opt, sc)) continue;

            System.out.print("a: ");
            String a = sc.nextLine();
            System.out.print("b: ");
            String b = sc.nextLine();

            processOperation(opt, a, b);
        }

        mf.saveLastSession(history);
        mf.saveCompleteHistory(history);
        sc.close();
    }

    public static double parse(String s) {
        try {
            if (s == null) return 0;
            s = s.replace(',', '.').trim();
            return Double.parseDouble(s);
        } catch (Exception e) { return 0; }
    }

    public static double compute(String a, String b, String op) {
        double A = parse(a);
        double B = parse(b);

        try {
            if ("+".equals(op)) return A + B;
            if ("-".equals(op)) return A - B;
            if ("*".equals(op)) return A * B;
            if ("/".equals(op)) return B == 0 ? A / 0.0000001 : A / B;
            if ("^".equals(op)) {
                double z = 1; int i = (int) B;
                while (i-- > 0) z *= A;
                return z;
            }
            if ("%".equals(op)) return A % B;

        } catch (Exception ignored) {}

        return 0;
    }

    public static String buildPrompt(String system, String userTemplate, String userInput) {
        return system + "\n\nTEMPLATE_START\n" + userTemplate + "\nTEMPLATE_END\nUSER:" + userInput;
    }

    public static String sendToLLM(String prompt) {
        System.out.println("=== RAW PROMPT SENT TO LLM (INSECURE) ===");
        System.out.println(prompt);
        System.out.println("=== END PROMPT ===");
        return "SIMULATED_LLM_RESPONSE";
    }
}
