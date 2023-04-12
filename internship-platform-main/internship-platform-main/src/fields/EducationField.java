package fields;

import services.CSVConvertible;

import java.time.YearMonth;

public class EducationField extends TimelineField implements CSVConvertible {
    private String institutionName;
    private String specializationName;

    public EducationField() {}
    public EducationField(YearMonth startDate, YearMonth finishDate, String description, String institutionName, String specializationName){
        super(startDate, finishDate, description);
        this.institutionName = institutionName;
        this.specializationName = specializationName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    @Override
    public String toString() {
        return "EducationField{" +
                super.toString() +
                ", institutionName='" + institutionName + '\'' +
                ", specializationName='" + specializationName + '\'' +
                '}';
    }

    @Override
    public String convertToCSV() {
        return super.convertToCSV() + ',' +
                String.join(",",
                        institutionName, specializationName);
    }

}
