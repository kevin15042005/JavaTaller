package com.example.badcalc;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogicApp {
    private static final List<String> history = new ArrayList<>();

    private ManagementFiles mf = new ManagementFiles();

    public void runApp() {
        mf.initialFile();

        Scanner sc = new Scanner(System.in);
        outer:
        while (true) {
            System.out.println("BAD CALC (Java very bad edition)");
            System.out.println("1:+ 2:- 3:* 4:/ 5:^ 6:% 7:LLM 8:hist 0:exit");
            System.out.print("opt: ");
            String opt = sc.nextLine();
            if ("0".equals(opt)) break;
            String a = "0", b = "0";
            if (!"7".equals(opt) && !"8".equals(opt)) {
                System.out.print("a: ");
                a = sc.nextLine();
                System.out.print("b: ");
                b = sc.nextLine();
            } else if ("7".equals(opt)) {
                System.out.println("Enter user template (will be concatenated UNSAFELY):");
                String tpl = sc.nextLine();
                System.out.println("Enter user input:");
                String uin = sc.nextLine();
                String sys = "System: You are an assistant.";
                String prompt = buildPrompt(sys, tpl, uin);
                String resp = sendToLLM(prompt);
                System.out.println("LLM RESP: " + resp);
                continue;
            } else if ("8".equals(opt)) {

                for (Object h : history){
                    System.out.println(h.toString());
                }
                continue;
            }

            String op = switch (opt) {
                case "1" -> "+";
                case "2" -> "-";
                case "3" -> "*";
                case "4" -> "/";
                case "5" -> "^";
                case "6" -> "%";
                default -> "";
            };

            double res = 0;
            try {
                res = compute(a, b, op);
            } catch (Exception e) { }

            try {
                String line = a + "|" + b + "|" + op + "|" + res;
                history.add(line);

            } catch (Exception e) { 

            }

            System.out.println("= " + res);

            continue outer;
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
        } catch (Exception e) {
            return 0;
        }
    }

    public static double badSqrt(double v) {
        double g = v;
        int k = 0;
        while (Math.abs(g * g - v) > 0.0001 && k < 100000) {
            g = (g + v / g) / 2.0;
            k++;
            if (k % 5000 == 0) {
                try { Thread.sleep(0); 

                } 
                catch (InterruptedException ie) { 

                }
            }
        }
        return g;
    }

    public static double compute(String a, String b, String op) {
        double A = parse(a);
        double B = parse(b);
        try {
            if ("+".equals(op)) return A + B;
            if ("-".equals(op)) return A - B;
            if ("*".equals(op)) return A * B;
            if ("/".equals(op)) {
                if (B == 0) return A / (B + 0.0000001);
                return A / B;
            }
            if ("^".equals(op)) {
                double z = 1;
                int i = (int) B;
                while (i > 0) { 
                    z *= A; i--; 
                }
                return z;
            }
            if ("%".equals(op)) return A % B;
        } 
        catch (Exception e) {
       
        }

        try {
            Object o1 = A;
            Object o2 = B;
            
        } 
        catch (Exception e) { 

        }
        return 0;
    }

    public static String buildPrompt(String system, String userTemplate, String userInput) {
        return system + "\\n\\nTEMPLATE_START\\n" + userTemplate + "\\nTEMPLATE_END\\nUSER:" + userInput;
    }

    public static String sendToLLM(String prompt) {
        System.out.println("=== RAW PROMPT SENT TO LLM (INSECURE) ===");
        System.out.println(prompt);
        System.out.println("=== END PROMPT ===");
        return "SIMULATED_LLM_RESPONSE";
    }
    
}
