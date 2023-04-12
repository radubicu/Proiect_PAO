import services.CSVConvertible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Job implements Observable, Cloneable, CSVConvertible {
    private boolean jobActive;
    private String jobTitle;
    private int periodInMonths;
    private int salary;
    private List<String> jobRequirements = new ArrayList<>();
    private List<String> jobAttributes = new ArrayList<>();

    public Job() {
        this.jobActive = false;
        this.jobTitle = "";
        this.periodInMonths = this.salary = 0;
    }

    public Job(String jobTitle, int periodInMonths, int salary){
        jobActive = true;
        this.jobTitle = jobTitle;
        this.periodInMonths = periodInMonths;
        this.salary = salary;
    }

    public Job(String jobTitle, int periodInMonths, int salary,
               ArrayList<String> jobRequirements, ArrayList<String> jobAttributes){
        this(jobTitle, periodInMonths, salary);
        Collections.copy(this.jobRequirements, jobRequirements);
        Collections.copy(this.jobAttributes, jobAttributes);
    }

    private List<Observer> observerList = new ArrayList<>();
    public void subscribe(Observer profile) {
        observerList.add(profile);
    }
    public void unsubscribe(Observer profile) {
        observerList.remove(profile);
    }
    public void notifyProfiles() {
        for(Observer o : observerList){
            o.updateObserver("Job update: \"" + this.jobTitle + "\"");
        }
    }

    public boolean isJobActive() {
        return jobActive;
    }

    public void setJobActive(boolean jobActive) {
        this.jobActive = jobActive;
        this.notifyProfiles();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        this.notifyProfiles();
    }

    public int getPeriodInMonths() {
        return periodInMonths;
    }

    public void setPeriodInMonths(int periodInMonths) {
        this.periodInMonths = periodInMonths;
        this.notifyProfiles();
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
        this.notifyProfiles();
    }

    public void addJobRequirement(String requirement) {
        jobRequirements.add(requirement);
    }

    public void addJobAttributes(String attribute) {
        jobAttributes.add(attribute);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Job) super.clone();
    }

    @Override
    public String convertToCSV() {
        StringBuilder stringBuilder = new StringBuilder(
                String.join(",",
                String.valueOf(jobActive),
                jobTitle,
                String.valueOf(salary),
                String.valueOf(periodInMonths),
                String.valueOf(jobRequirements.size()),
                String.valueOf(jobAttributes.size())
                ));

        if (jobRequirements.size() > 0) {
            stringBuilder.append(',');
            stringBuilder.append(
                    String.join(",", jobRequirements)
            );
        }

        if (jobAttributes.size() > 0) {
            stringBuilder.append(',');
            stringBuilder.append(
                    String.join(",", jobAttributes)
            );
        }

        return stringBuilder.toString();
    }
}
