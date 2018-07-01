package model;

import java.time.LocalDate;

public class DataPackage {

    private User user;
    private LocalDate startDate, endDate;

    public DataPackage(User selectedItem, LocalDate startDate, LocalDate endDate) {
        this.user = selectedItem;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}