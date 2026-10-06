package core;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;
import tool.ConsoleInputter;

/* Lớp mô tả cho 1 hợp đồng bảo hiểm (HĐBH) */
public class I_Statement implements Serializable {
    private String inID;          // Mã bảo hiểm, duy nhất
    private Date esDate;          // Ngày lập hợp đồng (> ngày hôm nay)
    private String licensePlate;  // Biển số xe được bảo hiểm
    private String cusName;       // Tên khách hàng / chủ xe
    private int inPeriod;         // Thời hạn bảo hiểm (12, 24 hoặc 36 tháng)
    private double inFee;         // Phí bảo hiểm (tự động tính)

    public I_Statement() {
    }

    // Constructor dùng để tìm kiếm theo mã inID
    public I_Statement(String inID) {
        this.inID = inID;
    }

    // Constructor đầy đủ tham số
    public I_Statement(String inID, Date esDate, String licensePlate, String cusName, int inPeriod, double inFee) {
        this.inID = inID;
        this.esDate = esDate;
        this.licensePlate = licensePlate;
        this.cusName = cusName;
        this.inPeriod = inPeriod;
        this.inFee = inFee;
    }

    // Hành vi kiểm tra bằng nhau theo mã bảo hiểm
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        I_Statement other = (I_Statement) obj;
        return Objects.equals(this.inID != null ? this.inID.toUpperCase() : null,
                              other.inID != null ? other.inID.toUpperCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.inID != null ? this.inID.toUpperCase() : null);
    }

    // Getters và Setters
    public String getInID() {
        return inID;
    }

    public void setInID(String inID) {
        this.inID = inID;
    }

    public Date getEsDate() {
        return esDate;
    }

    public void setEsDate(Date esDate) {
        this.esDate = esDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public int getInPeriod() {
        return inPeriod;
    }

    public void setInPeriod(int inPeriod) {
        this.inPeriod = inPeriod;
    }

    public double getInFee() {
        return inFee;
    }

    public void setInFee(double inFee) {
        this.inFee = inFee;
    }

    // Xuất thông tin chi tiết hợp đồng bảo hiểm trước khi lưu
    public String toStringScreen() {
        DecimalFormat df = new DecimalFormat("#,##0");
        String s = "------------------------------------------\n"
                 + "Insurance ID     : " + inID + "\n"
                 + "Established Date : " + ConsoleInputter.dateStr(esDate, "MM/dd/yyyy") + "\n"
                 + "License Plate    : " + licensePlate + "\n"
                 + "Customer         : " + cusName + "\n"
                 + "Insurance Period : " + inPeriod + "\n"
                 + "Insurance Fees   : $ " + df.format(inFee) + "\n"
                 + "------------------------------------------";
        return s;
    }

    // Xuất thông tin một dòng dữ liệu cho bảng danh sách hợp đồng bảo hiểm
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0");
        return String.format("| %-12s | %-16s | %-14s | %-18s | %-16d | $ %-12s |",
                inID,
                ConsoleInputter.dateStr(esDate, "MM/dd/yyyy"),
                licensePlate,
                cusName,
                inPeriod,
                df.format(inFee));
    }
}