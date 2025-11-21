package com.example.badcalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LogicApp {

    private static final Logger logger = Logger.getLogger(LogicApp.class.getName());
    private static final List<String> history = new ArrayList<>();
    private final ManagementFiles mf = new ManagementFiles();

    // show menu
    private void printMenu() {
        logger.info("BAD CALC (Java very bad edition)");
        logger.info("1:+ 2:- 3:* 4:/ 5:^ 6:% 7:LLM 8:hist 0:exit");
        logger.info("opt: ");
    }

    // handle LLM or history options
    private boolean handleSpecialOptions(String opt, Scanner sc) {
        if ("7".equals(opt)) {
            logger.info("Enter user template:");
            String tpl = sc.nextLine();
            logger.info("Enter user input:");
            String uin = sc.nextLine();
            String sys = "System: You are an assistant.";
            String prompt = buildPrompt(sys, tpl, uin);
            String resp = sendToLLM(prompt);
            logger.log(Level.INFO, "LLM RESP:{0} " + resp);
            return true;
        }

        if ("8".equals(opt)) {
            for (String h : history) {
                logger.info(h);
            }
            return true;
        }

        return false;
    }

    // run arithmetic operations
    private void processOperation(String opt, String a, String b) {
        String op = switch (opt) {
            case "1" ->
                "+";
            case "2" ->
                "-";
            case "3" ->
                "*";
            case "4" ->
                "/";
            case "5" ->
                "^";
            case "6" ->
                "%";
            default ->
                "";
        };

        double res = compute(a, b, op);

        String line = a + "|" + b + "|" + op + "|" + res;
        history.add(line);

        logger.log(Level.INFO, "= {}" + res);
    }

    public boolean runApp() {
        mf.initialFile();
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running) {
            printMenu();
            String opt = sc.nextLine();

            if ("0".equals(opt)) {
                running = false;
            }

            if (handleSpecialOptions(opt, sc)) {
                logger.info("a: ");
                String a = sc.nextLine();
                logger.info("b: ");
                String b = sc.nextLine();

                processOperation(opt, a, b);
            }

        }

        mf.saveLastSession(history);
        mf.saveCompleteHistory(history);
        sc.close();
        return true;
    }

    public static double parse(String s) {
        try {
            if (s == null) {
                return 0;
            }
            s = s.replace(',', '.').trim();
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double compute(String a, String b, String op) {
        double parseA = parse(a);
        double parseB = parse(b);

        try {
            if ("+".equals(op)) {
                return parseA + parseB;
            }
            if ("-".equals(op)) {
                return parseA - parseB;
            }
            if ("*".equals(op)) {
                return parseA * parseB;
            }
            if ("/".equals(op)) {
                return parseA == 0 ? parseA / 0.0000001 : parseA / parseB;
            }
            if ("^".equals(op)) {
                double z = 1;
                int i = (int) parseB;
                while (i-- > 0) {
                    z *= parseA;
                }
                return z;
            }
            if ("%".equals(op)) {
                return parseA % parseB;
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE , "Errot convertir", e);
        }

        return 0;
    }

    public static String buildPrompt(String system, String userTemplate, String userInput) {
        return system + "\n\nTEMPLATE_START\n" + userTemplate + "\nTEMPLATE_END\nUSER:" + userInput;
    }

    public static String sendToLLM(String prompt) {
        logger.info("=== RAW PROMPT SENT TO LLM (INSECURE) ===");
        logger.info(prompt);
        logger.info("=== END PROMPT ===");
        return "SIMULATED_LLM_RESPONSE";
    }
}
