package com.example.sholatki.responses;

import com.example.sholatki.models.PrayerSchedule;

public class PrayerScheduleResponse {
    private boolean status;
    private Request request;
    private Data data;

    // Getters and Setters
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Request {
        private String path;

        // Getters and Setters
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public class Data {
        private int id;
        private String lokasi;
        private String daerah;
        private PrayerSchedule jadwal;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLokasi() {
            return lokasi;
        }

        public void setLokasi(String lokasi) {
            this.lokasi = lokasi;
        }

        public String getDaerah() {
            return daerah;
        }

        public void setDaerah(String daerah) {
            this.daerah = daerah;
        }

        public PrayerSchedule getJadwal() {
            return jadwal;
        }

        public void setJadwal(PrayerSchedule jadwal) {
            this.jadwal = jadwal;
        }
    }
}
