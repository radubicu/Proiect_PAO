package fields;

import services.CSVConvertible;

import java.sql.Time;
import java.time.YearMonth;

public abstract class TimelineField implements Comparable<TimelineField>, Cloneable, CSVConvertible {
    private YearMonth startDate;
    private YearMonth finishDate;
    private String description;

    public TimelineField() {}
    public TimelineField(YearMonth startDate, YearMonth finishDate, String description) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.description = description;
    }

    public YearMonth getStartDate() {
        return startDate;
    }

    public YearMonth getFinishDate() {
        return finishDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(TimelineField o) {
        int comp = this.startDate.compareTo(o.startDate);
        if (comp != 0) {
            return comp;
        }
        return this.finishDate.compareTo(o.finishDate);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setStartDate(YearMonth startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(YearMonth finishDate) {
        this.finishDate = finishDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", description='" + description + '\'';
    }

    @Override
    public String convertToCSV() {
        return String.join(",",
                startDate.toString(), finishDate.toString(), description);
    }
}

