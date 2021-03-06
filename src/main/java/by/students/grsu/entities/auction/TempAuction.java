package by.students.grsu.entities.auction;

import java.time.LocalTime;

public class TempAuction {
    private LocalTime startTime;
    private int maxDuration;
    private String description;
    private int maxLots;

    public TempAuction() {
        startTime = LocalTime.now();
        maxDuration = 60;
        maxLots = 10;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime);
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxLots() {
        return maxLots;
    }

    public void setMaxLots(int maxLots) {
        this.maxLots = maxLots;
    }
}
