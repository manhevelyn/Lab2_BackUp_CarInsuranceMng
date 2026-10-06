package core;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import tool.ConsoleInputter;

/* Lớp quản lý danh sách các xe ô tô */
public class CarList extends ArrayList<Car> {
    public static final String DATE_PAT = "dd/MM/yyyy";
    public static final String NAME_PAT = "^[a-zA-Z\\s]{2,35}$";

    public CarList() {
        super();
    }

    /* Thêm xe ô tô mới từ bàn phím
       Input: Nhập từ bàn phím
       Output: Thêm 1 xe vào carList
    */
    public void addCar() {
        boolean cont = true;
        do {
            String licensePlate;
            int pos;
            do {
                licensePlate = ConsoleInputter.getStr("Enter license plate");
                if (licensePlate.isEmpty()) {
                    System.out.println(">> License plate cannot be empty!");
                    pos = 0;
                    continue;
                }
                pos = this.indexOf(new Car(licensePlate));
                if (pos >= 0) {
                    System.out.println(">> The license plate is duplicated!");
                }
            } while (pos >= 0 || licensePlate.isEmpty());

            String carOwner = ConsoleInputter.getStr("Enter vehicle owner (2-35 characters)", NAME_PAT, "Owner name must be 2 to 35 alphabetic characters!");

            String carBrand;
            do {
                carBrand = ConsoleInputter.getStr("Enter car brand");
                if (carBrand.isEmpty()) {
                    System.out.println(">> Brand cannot be empty!");
                }
            } while (carBrand.isEmpty());

            double carValue = ConsoleInputter.getDouble("Enter vehicle value (> 999)", 999.0);

            Date regDate;
            Date today = new Date();
            do {
                regDate = ConsoleInputter.getDate("Enter registration date", DATE_PAT);
                if (!regDate.before(today)) {
                    System.out.println(">> Registration date must be before today!");
                }
            } while (!regDate.before(today));

            String regPlace;
            do {
                regPlace = ConsoleInputter.getStr("Enter registration place");
                if (regPlace.isEmpty()) {
                    System.out.println(">> Registration place cannot be empty!");
                }
            } while (regPlace.isEmpty());

            // Chọn số chỗ ngồi thông qua menu để loại bỏ lỗi nhập sai
            System.out.println("Select vehicle type:");
            Object[] typeOptions = {"5 seats", "7 seats", "9 seats"};
            int typeChoice = ConsoleInputter.intMenu(typeOptions);
            int vehicleType = (typeChoice == 1) ? 5 : ((typeChoice == 2) ? 7 : 9);

            Car newCar = new Car(licensePlate, carOwner, carBrand, carValue, regDate, regPlace, vehicleType);
            this.add(newCar);
            System.out.println(">> Car added successfully.");

            cont = ConsoleInputter.getBoolean("Do you want to continue adding a new car?");
        } while (cont);
    }

    /* Tìm kiếm xe theo biển số
       Input: Biển số xe
       Output: Xuất thông tin xe hoặc thông báo không tồn tại
    */
    public void findCar() {
        if (this.isEmpty()) {
            System.out.println(">> The car list is empty!");
            return;
        }
        String plate = ConsoleInputter.getStr("Enter license plate to find");
        int pos = this.indexOf(new Car(plate));
        if (pos < 0) {
            System.out.println("Unregistered vehicle");
        } else {
            System.out.println(this.get(pos).toStringScreen());
        }
    }

    /* Cập nhật thông tin xe theo biển số (không cho phép sửa biển số)
       Input: Biển số và các dữ liệu mới
       Output: Cập nhật thông tin xe trong bộ nhớ
    */
    public void updateCar() {
        if (this.isEmpty()) {
            System.out.println(">> The car list is empty!");
            return;
        }
        String plate = ConsoleInputter.getStr("Enter license plate to update");
        int pos = this.indexOf(new Car(plate));
        if (pos < 0) {
            System.out.println("Unregistered vehicle");
            System.out.println(">> Update failed!");
            return;
        }

        Car car = this.get(pos);
        System.out.println(">> Vehicle found. Enter new information:");

        String newOwner = ConsoleInputter.getStr("Enter new vehicle owner", NAME_PAT, "Owner name must be 2 to 35 alphabetic characters!");

        String newBrand;
        do {
            newBrand = ConsoleInputter.getStr("Enter new car brand");
            if (newBrand.isEmpty()) {
                System.out.println(">> Brand cannot be empty!");
            }
        } while (newBrand.isEmpty());

        double newValue = ConsoleInputter.getDouble("Enter new vehicle value (> 999)", 999.0);

        Date newRegDate;
        Date today = new Date();
        do {
            newRegDate = ConsoleInputter.getDate("Enter new registration date", DATE_PAT);
            if (!newRegDate.before(today)) {
                System.out.println(">> Registration date must be before today!");
            }
        } while (!newRegDate.before(today));

        String newPlace;
        do {
            newPlace = ConsoleInputter.getStr("Enter new registration place");
            if (newPlace.isEmpty()) {
                System.out.println(">> Registration place cannot be empty!");
            }
        } while (newPlace.isEmpty());

        System.out.println("Select new vehicle type:");
        Object[] typeOptions = {"5 seats", "7 seats", "9 seats"};
        int typeChoice = ConsoleInputter.intMenu(typeOptions);
        int newType = (typeChoice == 1) ? 5 : ((typeChoice == 2) ? 7 : 9);

        car.setCarOwner(newOwner);
        car.setCarBrand(newBrand);
        car.setCarValue(newValue);
        car.setRegDate(newRegDate);
        car.setRegPlace(newPlace);
        car.setVehicleType(newType);

        System.out.println(">> Update successfully.");
    }

    /* Xóa thông tin xe (chỉ cho xóa nếu xe chưa từng mua bảo hiểm)
       Input: Biển số xe
       Output: Xóa xe khỏi danh sách hoặc thông báo lỗi
    */
    public void deleteCar(StmtList stmtList) {
        if (this.isEmpty()) {
            System.out.println(">> The car list is empty!");
            return;
        }
        String plate = ConsoleInputter.getStr("Enter license plate to delete");
        int pos = this.indexOf(new Car(plate));
        if (pos < 0) {
            System.out.println("Unregistered vehicle");
            System.out.println(">> Delete failed!");
            return;
        }

        // Kiểm tra xem xe đã mua bảo hiểm chưa
        if (stmtList != null && stmtList.isCarInsured(plate)) {
            System.out.println(">> The car information cannot be deleted because it is already registered in insurance.");
            System.out.println(">> Delete failed!");
            return;
        }

        boolean confirm = ConsoleInputter.getBoolean("Are you sure you want to delete this car?");
        if (confirm) {
            this.remove(pos);
            System.out.println(">> Delete successfully.");
        } else {
            System.out.println(">> Deletion canceled.");
        }
    }

    /* Báo cáo danh sách các xe chưa tham gia bảo hiểm
       Input: Chọn trường sắp xếp và chiều sắp xếp
       Output: Bảng báo cáo UNINSURED CARS
    */
    public void reportUninsuredCars(StmtList stmtList) {
        ArrayList<Car> uninsuredList = new ArrayList<>();
        for (Car c : this) {
            if (stmtList == null || !stmtList.isCarInsured(c.getLicensePlate())) {
                uninsuredList.add(c);
            }
        }

        if (uninsuredList.isEmpty()) {
            System.out.println(">> All vehicles have been insured!");
            return;
        }

        System.out.println("\nSelect field to sort:");
        Object[] fields = {"License plate", "Vehicle Owner", "Registration date", "Vehicle type"};
        int fieldChoice = ConsoleInputter.intMenu(fields);

        System.out.println("Select sort type:");
        Object[] orders = {"Ascending (ASC)", "Descending (DESC)"};
        int orderChoice = ConsoleInputter.intMenu(orders);
        boolean isAsc = (orderChoice == 1);

        Comparator<Car> comp = null;
        String sortedByName = "";
        switch (fieldChoice) {
            case 1:
                comp = Comparator.comparing(Car::getLicensePlate, String.CASE_INSENSITIVE_ORDER);
                sortedByName = "License plate";
                break;
            case 2:
                comp = Comparator.comparing(Car::getCarOwner, String.CASE_INSENSITIVE_ORDER);
                sortedByName = "Vehicle Owner";
                break;
            case 3:
                comp = Comparator.comparing(Car::getRegDate);
                sortedByName = "Registration date";
                break;
            case 4:
                comp = Comparator.comparingInt(Car::getVehicleType);
                sortedByName = "Vehicle type";
                break;
        }

        if (comp != null) {
            if (!isAsc) {
                comp = comp.reversed();
            }
            Collections.sort(uninsuredList, comp);
        }

        System.out.println("\nReport: UNINSURED CARS");
        System.out.println("Sorted by: " + sortedByName);
        System.out.println("Sort type: " + (isAsc ? "ASC" : "DESC"));
        String header = "+-----+---------------+-------------------+--------------------+--------------+--------------+---------------+";
        System.out.println(header);
        System.out.println("| No. | License plate | Registration Date | Vehicle Owner      | Brand        | Vehicle type | Value         |");
        System.out.println(header);

        int no = 1;
        for (Car c : uninsuredList) {
            System.out.printf("| %-3d %s\n", no++, c.toString());
        }
        System.out.println(header + "\n");
    }

    /* Đọc danh sách xe từ file nhị phân */
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
                    Car car = (Car) ois.readObject();
                    this.add(car);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("All data in the file were read.");
        } catch (Exception e) {
            System.err.println(">> Error reading file: " + e.getMessage());
        }
    }

    /* Ghi danh sách xe vào file nhị phân */
    public void writeFile(String fName) {
        if (this.isEmpty()) {
            System.out.println("Empty list.");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName))) {
            for (Car car : this) {
                oos.writeObject(car);
            }
            System.out.println("Data are saved to file.");
        } catch (IOException e) {
            System.err.println(">> Error writing file: " + e.getMessage());
        }
    }
}