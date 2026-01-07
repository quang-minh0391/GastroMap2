package model;

import java.sql.Timestamp;

/**
 * Entity class for qr_scan_history table
 * Represents scan history records for QR codes
 */
public class QRScanHistory {
    private Integer id;
    private Integer qrId;
    private Timestamp scanTime;
    private String scanLocation;
    private String scanActor;
    private String note;

    public QRScanHistory() {
    }

    public QRScanHistory(Integer id, Integer qrId, Timestamp scanTime,
                         String scanLocation, String scanActor, String note) {
        this.id = id;
        this.qrId = qrId;
        this.scanTime = scanTime;
        this.scanLocation = scanLocation;
        this.scanActor = scanActor;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQrId() {
        return qrId;
    }

    public void setQrId(Integer qrId) {
        this.qrId = qrId;
    }

    public Timestamp getScanTime() {
        return scanTime;
    }

    public void setScanTime(Timestamp scanTime) {
        this.scanTime = scanTime;
    }

    public String getScanLocation() {
        return scanLocation;
    }

    public void setScanLocation(String scanLocation) {
        this.scanLocation = scanLocation;
    }

    public String getScanActor() {
        return scanActor;
    }

    public void setScanActor(String scanActor) {
        this.scanActor = scanActor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

