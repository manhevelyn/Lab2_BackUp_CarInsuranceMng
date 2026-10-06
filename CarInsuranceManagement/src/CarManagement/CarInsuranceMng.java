import core.CarList;
import core.StmtList;
import tool.ConsoleInputter;

/* Chương trình chính quản lý bảo hiểm xe hơi */
public class CarInsuranceMng {
    public static void main(String[] args) {
        String carFile = "CarInfo.dat";
        String insFile = "insurances.dat";

        CarList carList = new CarList();
        StmtList stmtList = new StmtList(carList);

        // Nạp sẵn dữ liệu ban đầu từ file nhị phân nếu file đã tồn tại
        carList.readFile(carFile);
        stmtList.readFile(insFile);

        Object[] mnuOptions = {
            "Add car information",
            "Find a car",
            "Update a car",
            "Delete a car",
            "Add an insurance statement",
            "List of insurance statements",
            "Report on uninsured cars",
            "Save data",
            "Load data",
            "Quit"
        };

        boolean carChanged = false;
        boolean stmtChanged = false;
        int choice;

        do {
            System.out.println("\nCAR INSURANCE MANAGEMENT");
            System.out.println("------------------------------------------");
            choice = ConsoleInputter.intMenu(mnuOptions);

            switch (choice) {
                case 1:
                    carList.addCar();
                    carChanged = true;
                    break;
                case 2:
                    carList.findCar();
                    break;
                case 3:
                    carList.updateCar();
                    carChanged = true;
                    break;
                case 4:
                    carList.deleteCar(stmtList);
                    carChanged = true;
                    break;
                case 5:
                    stmtList.addStatement();
                    stmtChanged = true;
                    break;
                case 6:
                    stmtList.listStatements();
                    break;
                case 7:
                    carList.reportUninsuredCars(stmtList);
                    break;
                case 8:
                    carList.writeFile(carFile);
                    stmtList.writeFile(insFile);
                    carChanged = false;
                    stmtChanged = false;
                    break;
                case 9:
                    carList.readFile(carFile);
                    stmtList.readFile(insFile);
                    carChanged = false;
                    stmtChanged = false;
                    break;
                case 10:
                    boolean confirm = ConsoleInputter.getBoolean("Do you want to exit the program?");
                    if (confirm) {
                        if (carChanged || stmtChanged) {
                            boolean saveResp = ConsoleInputter.getBoolean("Data has been changed. Do you want to save before exiting?");
                            if (saveResp) {
                                carList.writeFile(carFile);
                                stmtList.writeFile(insFile);
                                System.out.println("Saved. Good bye!");
                            }
                        }
                        System.out.println("Good bye!");
                    } else {
                        choice = 0; // Giữ vòng lặp tiếp tục chạy nếu người dùng chọn No
                    }
                    break;
            }
        } while (choice != 10);
    }
}