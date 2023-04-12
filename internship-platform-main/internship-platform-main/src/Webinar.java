import services.CSVConvertible;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Webinar implements Observable, Comparable<Webinar>, Cloneable, CSVConvertible {

    private String name;
    private LocalDate startDate;
    private LocalDate finishDate;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String platform;
    private boolean reminderSent = false;
    private boolean finished = false;

    private List<Observer> observerList = new ArrayList<>();

    @Override
    public void subscribe(Observer profile) {
        observerList.add(profile);
    }
    @Override
    public void unsubscribe(Observer profile) {
        observerList.remove(profile);
    }
    @Override
    public void notifyProfiles() {
        for(Observer o : observerList){
            o.updateObserver("Webinar update: \"" + this.name + "\"");
        }
    }

    public Webinar(String name, LocalDate startDate, LocalDate finishDate, LocalTime startTime, LocalTime finishTime, String platform) {
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isFinished() {
        return finished;
    }

    public void sendUpdate() {
        if (!reminderSent
                && startDate.equals(LocalDate.now())
                && LocalTime.now().isAfter(startTime.minusHours(1))) {
            for(Observer o : observerList) {
                o.updateObserver("Webinar \"" + this.name + "\" starts in less than an hour!");
            }
            reminderSent = true;
        }
    }

    private void setFinished() {
        finished = true;
    }

    @Override
    public int compareTo(Webinar o) {
        int comp = this.startDate.compareTo(o.startDate);
        if(comp != 0) {
            return comp;
        }
        return this.startTime.compareTo(o.startTime);
    }

    public Object clone()throws CloneNotSupportedException{
        return (Webinar)super.clone();
    }

    @Override
    public String convertToCSV() {
        return String.join(",",
                name,
                startDate.toString(),
                startTime.toString(),
                finishDate.toString(),
                finishTime.toString(),
                platform,
                String.valueOf(reminderSent),
                String.valueOf(finished));
    }
}
