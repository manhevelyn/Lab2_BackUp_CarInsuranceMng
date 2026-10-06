package core;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;
import tool.ConsoleInputter;

/* Lớp mô tả cho 1 chiếc xe ô tô */
public class Car implements Serializable {
    private String licensePlate;   // Biển số xe, duy nhất
    private String carOwner;      // Tên chủ xe, độ dài 2..35 ký tự
    private String carBrand;      // Dòng xe / Hãng xe
    private double carValue;      // Trị giá xe (> 999)
    private Date regDate;         // Ngày đăng ký (trước ngày hôm nay)
    private String regPlace;      // Nơi đăng ký
    private int vehicleType;      // Loại xe (5, 7 hoặc 9 chỗ ngồi)

    public Car() {
    }

    // Constructor dùng cho thao tác tìm kiếm và so sánh dựa trên licensePlate
    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    // Constructor đầy đủ tham số
    public Car(String licensePlate, String carOwner, String carBrand, double carValue, Date regDate, String regPlace, int vehicleType) {
        this.licensePlate = licensePlate;
        this.carOwner = carOwner;
        this.carBrand = carBrand;
        this.carValue = carValue;
        this.regDate = regDate;
        this.regPlace = regPlace;
        this.vehicleType = vehicleType;
    }

    // Hành vi kiểm tra bằng nhau phục vụ quá trình tìm kiếm trong ArrayList
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car other = (Car) obj;
        return Objects.equals(this.licensePlate != null ? this.licensePlate.toUpperCase() : null,
                              other.licensePlate != null ? other.licensePlate.toUpperCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.licensePlate != null ? this.licensePlate.toUpperCase() : null);
    }

    // Getters và Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public double getCarValue() {
        return carValue;
    }

    public void setCarValue(double carValue) {
        this.carValue = carValue;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getRegPlace() {
        return regPlace;
    }

    public void setRegPlace(String regPlace) {
        this.regPlace = regPlace;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    // Xuất thông tin chi tiết một xe ra màn hình console
    public String toStringScreen() {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = "------------------------------------------\n"
                 + "License plate    : " + licensePlate + "\n"
                 + "Vehicle Owner    : " + carOwner + "\n"
                 + "Brand            : " + carBrand + "\n"
                 + "Value            : $ " + df.format(carValue) + "\n"
                 + "Registration Date: " + ConsoleInputter.dateStr(regDate, "MM/dd/yyyy") + "\n"
                 + "Reg. Place       : " + regPlace + "\n"
                 + "Vehicle Type     : " + vehicleType + "\n"
                 + "------------------------------------------";
        return s;
    }

    // Xuất thông tin một dòng dữ liệu cho bảng báo cáo xe chưa bảo hiểm
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0");
        return String.format("| %-13s | %-17s | %-18s | %-12s | %-12d | $ %-11s |",
                licensePlate,
                ConsoleInputter.dateStr(regDate, "MM/dd/yyyy"),
                carOwner,
                carBrand,
                vehicleType,
                df.format(carValue));
    }
}