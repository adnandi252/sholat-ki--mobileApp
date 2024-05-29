package com.example.sholatki.responses;

public class HadithResponse {
    private Info info;
    private Data data;

    // Getter and setter methods
    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Nested classes for Info and Data

    public static class Info {
        private Perawi perawi;

        // Getter and setter methods
        public Perawi getPerawi() {
            return perawi;
        }

        public void setPerawi(Perawi perawi) {
            this.perawi = perawi;
        }

        public static class Perawi {
            private String name;
            private String slug;
            private int total;

            // Getter and setter methods
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }

    public static class Data {
        private int number;
        private String arab;
        private String id;

        // Getter and setter methods
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getArab() {
            return arab;
        }

        public void setArab(String arab) {
            this.arab = arab;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
