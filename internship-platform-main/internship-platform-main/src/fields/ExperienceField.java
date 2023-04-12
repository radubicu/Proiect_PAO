package fields;

import java.time.YearMonth;

public class ExperienceField extends TimelineField {
    private String positionName;
    private String institutionName;

    public ExperienceField(YearMonth startDate, YearMonth finishDate, String description, String positionName, String institutionName) {
        super(startDate, finishDate, description);
        this.positionName = positionName;
        this.institutionName = institutionName;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return  "ExperienceField{" +
                super.toString() +
                ", positionName='" + positionName + '\'' +
                ", institutionName='" + institutionName + '\'' +
                '}';
    }

    @Override
    public String convertToCSV() {
        return super.convertToCSV() + ',' +
                String.join(",",
                        positionName, institutionName);
    }
}

