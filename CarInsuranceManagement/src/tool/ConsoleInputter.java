package tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/* Bộ công cụ hỗ trợ nhập liệu có kiểm soát qua Console */
public class ConsoleInputter {
    public static Scanner sc = new Scanner(System.in);

    // Nhập chuỗi ký tự tự do (được cắt tỉa khoảng trắng thừa)
    public static String getStr(String msg) {
        System.out.print(msg + ": ");
        return sc.nextLine().trim();
    }

    // Nhập chuỗi ký tự có kiểm tra theo biểu thức chính quy (Pattern)
    public static String getStr(String msg, String pattern, String errorMsg) {
        String data;
        do {
            System.out.print(msg + ": ");
            data = sc.nextLine().trim();
            if (data.matches(pattern)) {
                return data;
            }
            System.out.println(">> Error: " + errorMsg);
        } while (true);
    }

    // Nhập số nguyên có chặn khoảng giá trị [min, max]
    public static int getInt(String msg, int min, int max) {
        int val;
        do {
            try {
                System.out.print(msg + ": ");
                val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println(">> Value must be between " + min + " and " + max + "!");
            } catch (Exception e) {
                System.out.println(">> Please enter a valid integer!");
            }
        } while (true);
    }

    // Nhập số thực có chặn giá trị nhỏ nhất
    public static double getDouble(String msg, double min) {
        double val;
        do {
            try {
                System.out.print(msg + ": ");
                val = Double.parseDouble(sc.nextLine().trim());
                if (val > min) {
                    return val;
                }
                System.out.println(">> Value must be greater than " + min + "!");
            } catch (Exception e) {
                System.out.println(">> Please enter a valid real number!");
            }
        } while (true);
    }

    // Nhập ngày tháng theo định dạng format được chỉ định
    public static Date getDate(String msg, String format) {
        DateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        do {
            try {
                System.out.print(msg + " (" + format + "): ");
                return df.parse(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println(">> Invalid date or incorrect format (" + format + ")!");
            }
        } while (true);
    }

    // Chuyển đối tượng Date thành chuỗi định dạng
    public static String dateStr(Date date, String format) {
        if (date == null) return "";
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    // Nhập xác nhận dạng Y/N
    public static boolean getBoolean(String msg) {
        System.out.print(msg + " (Y/N): ");
        String resp = sc.nextLine().trim();
        return resp.equalsIgnoreCase("Y");
    }

    // Hiển thị danh sách tùy chọn và nhận lựa chọn hợp lệ từ người dùng
    public static int intMenu(Object[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        return getInt("Your choice", 1, options.length);
    }
}