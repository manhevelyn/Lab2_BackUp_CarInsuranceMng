package core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import tool.ConsoleInputter;

/* Lớp mô tả cho danh sách các hợp đồng bảo hiểm */
public class StmtList extends ArrayList<I_Statement> {
    public static final String DATE_PAT = "dd/MM/yyyy";
    private CarList carList; // Tham chiếu đến danh sách xe để kiểm tra và đối soát

    public StmtList(CarList carList) {
        super();
        this.carList = carList;
    }

    public void setCarList(CarList carList) {
        this.carList = carList;
    }

    // Kiểm tra xe đã từng có hợp đồng bảo hiểm trong danh sách hay chưa
    public boolean isCarInsured(String licensePlate) {
        if (licensePlate == null) return false;
        for (I_Statement stmt : this) {
            if (stmt.getLicensePlate() != null && stmt.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return true;
            }
        }
        return false;
    }

    /* Thêm 1 hợp đồng bảo hiểm mới, tự động tính phí bảo hiểm
       Input: Nhập từ bàn phím
       Output: Thêm hợp đồng vào stmtList
    */
    public void addStatement() {
        if (carList == null || carList.isEmpty()) {
            System.out.println(">> The car list is empty! Car information must be registered first.");
            return;
        }

        boolean cont = true;
        do {
            String inID;
            int pos;
            do {
                inID = ConsoleInputter.getStr("Enter insurance ID");
                if (inID.isEmpty()) {
                    System.out.println(">> Insurance ID cannot be empty!");
                    pos = 0;
                    continue;
                }
                pos = this.indexOf(new I_Statement(inID));
                if (pos >= 0) {
                    System.out.println(">> The insurance ID is duplicated!");
                }
            } while (pos >= 0 || inID.isEmpty());

            Date esDate;
            Date today = new Date();
            do {
                esDate = ConsoleInputter.getDate("Enter established date", DATE_PAT);
                if (!esDate.after(today)) {
                    System.out.println(">> Established date must be after today!");
                }
            } while (!esDate.after(today));

            String licensePlate;
            int carPos;
            do {
                licensePlate = ConsoleInputter.getStr("Enter license plate");
                carPos = carList.indexOf(new Car(licensePlate));
                if (carPos < 0) {
                    System.out.println(">> Unregistered vehicle! Please enter again.");
                }
            } while (carPos < 0);

            Car targetCar = carList.get(carPos);
            String cusName = targetCar.getCarOwner();
            System.out.println(">> Customer name: " + cusName);

            // Chọn kỳ hạn bảo hiểm qua menu để không phải kiểm tra regex
            System.out.println("Select insurance period:");
            Object[] periodOptions = {"12 months", "24 months", "36 months"};
            int pChoice = ConsoleInputter.intMenu(periodOptions);

            int inPeriod = 12;
            double inFee = 0;
            double vehicleValue = targetCar.getCarValue();

            // Tính phí bảo hiểm theo công thức quy định
            if (pChoice == 1) {
                inPeriod = 12;
                inFee = 0.25 * vehicleValue;
            } else if (pChoice == 2) {
                inPeriod = 24;
                inFee = 0.20 * vehicleValue * 2;
            } else if (pChoice == 3) {
                inPeriod = 36;
                inFee = 0.15 * vehicleValue * 3;
            }

            I_Statement newStmt = new I_Statement(inID, esDate, targetCar.getLicensePlate(), cusName, inPeriod, inFee);
            System.out.println("\nCustomer order Information:");
            System.out.println(newStmt.toStringScreen());

            boolean confirm = ConsoleInputter.getBoolean("Do you want to save this insurance statement?");
            if (confirm) {
                this.add(newStmt);
                System.out.println(">> New insurance statement was added successfully.");
            } else {
                System.out.println(">> Saving canceled.");
            }

            cont = ConsoleInputter.getBoolean("Do you want to continue adding a new insurance statement?");
        } while (cont);
    }

    /* Liệt kê các hợp đồng bảo hiểm theo năm và hỗ trợ sắp xếp
       Input: Năm khảo sát, trường sắp xếp và chiều sắp xếp
       Output: Báo cáo INSURANCE STATEMENTS
    */
    public void listStatements() {
        if (this.isEmpty()) {
            System.out.println(">> The insurance statement list is empty!");
            return;
        }

        int year = ConsoleInputter.getInt("Enter year to display insurance statements", 1900, 2100);

        ArrayList<I_Statement> yearList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (I_Statement stmt : this) {
            if (stmt.getEsDate() != null) {
                cal.setTime(stmt.getEsDate());
                if (cal.get(Calendar.YEAR) == year) {
                    yearList.add(stmt);
                }
            }
        }

        if (yearList.isEmpty()) {
            System.out.println(">> No insurance statement found for the year " + year + "!");
            return;
        }

        System.out.println("\nSelect field to sort:");
        Object[] fields = {"Insurance Id", "Established Date", "License plate", "Insurance period"};
        int fieldChoice = ConsoleInputter.intMenu(fields);

        System.out.println("Select sort type:");
        Object[] orders = {"Ascending (ASC)", "Descending (DESC)"};
        int orderChoice = ConsoleInputter.intMenu(orders);
        boolean isAsc = (orderChoice == 1);

        Comparator<I_Statement> comp = null;
        String sortedByName = "";
        switch (fieldChoice) {
            case 1:
                comp = Comparator.comparing(I_Statement::getInID, String.CASE_INSENSITIVE_ORDER);
                sortedByName = "Insurance Id";
                break;
            case 2:
                comp = Comparator.comparing(I_Statement::getEsDate);
                sortedByName = "Established Date";
                break;
            case 3:
                comp = Comparator.comparing(I_Statement::getLicensePlate, String.CASE_INSENSITIVE_ORDER);
                sortedByName = "License plate";
                break;
            case 4:
                comp = Comparator.comparingInt(I_Statement::getInPeriod);
                sortedByName = "Insurance period";
                break;
        }

        if (comp != null) {
            if (!isAsc) {
                comp = comp.reversed();
            }
            Collections.sort(yearList, comp);
        }

        System.out.println("\nReport: INSURANCE STATEMENTS");
        System.out.printf("From: 01/01/%d To: 12/31/%d\n", year, year);
        System.out.println("Sorted by: " + sortedByName);
        System.out.println("Sort type: " + (isAsc ? "ASC" : "DESC"));
        String header = "+-----+--------------+------------------+----------------+--------------------+------------------+---------------+";
        System.out.println(header);
        System.out.println("| No. | Insurance Id | Established Date | License plate  | Customer           | Insurance period | Insurance fees|");
        System.out.println(header);

        int no = 1;
        for (I_Statement s : yearList) {
            System.out.printf("| %-3d %s\n", no++, s.toString());
        }
        System.out.println(header + "\n");
    }

    /* Đọc file nhị phân vào danh sách */
    public void readFile(String fName) {
        File f = new File(fName);
        if (!f.exists()) {
            System.out.println("The file " + fName + " does not exist. Ignored.");
            return;
        }
        this.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            while (true) {
                try {
                    I_Statement stmt = (I_Statement) ois.readObject();
                    this.add(stmt);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("All data in the file were read.");
        } catch (Exception e) {
            System.err.println(">> Error reading file: " + e.getMessage());
        }
    }

    /* Ghi danh sách HĐBH vào file nhị phân */
    public void writeFile(String fName) {
        if (this.isEmpty()) {
            System.out.println("Empty list.");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName))) {
            for (I_Statement stmt : this) {
                oos.writeObject(stmt);
            }
            System.out.println("Data are saved to file.");
        } catch (IOException e) {
            System.err.println(">> Error writing file: " + e.getMessage());
        }
    }
}