package fields;

import java.time.YearMonth;

public class ProjectField extends TimelineField {
    private String projectName;

    public ProjectField(YearMonth startDate, YearMonth finishDate, String description, String projectName) {
        super(startDate, finishDate, description);
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ProjectField{" +
                super.toString() +
                ", projectName='" + projectName + '\'' +
                '}';
    }

    @Override
    public String convertToCSV() {
        return super.convertToCSV() + ',' + projectName;
    }
}
