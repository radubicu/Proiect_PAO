import services.CSVConvertible;
import services.Loggable;
import services.LoggingService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Employer implements CSVConvertible, Loggable {
    private String employerName;
    private List<Job> employerJobs = new ArrayList<>();
    private Set<Webinar> employerWebinars = new TreeSet<>();
    LoggingService<Employer> loggingService = new LoggingService<>(Employer.class);

    public void addJob(Job job) throws CloneNotSupportedException {
        employerJobs.add((Job)job.clone());
        loggingService.logAction("addJob", LocalDateTime.now());
    }

    public void removeJob(Job job) {
        job.notifyProfiles();
        employerJobs.remove(job);
        loggingService.logAction("removeJob", LocalDateTime.now());
    }

    public void addWebinar(Webinar webinar) throws CloneNotSupportedException {
        employerWebinars.add((Webinar)webinar.clone());
    }

    public void removeWebinar(Webinar webinar) {
        webinar.notifyProfiles();
        employerWebinars.remove(webinar);
    }

    public Employer(String employerName) {
        this.employerName = employerName;
    }

    public Employer(String employerName, ArrayList<Job> employerJobs) {
        this.employerName = employerName;
        Collections.copy(this.employerJobs, employerJobs);
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public List<Job> getEmployerJobs() {
        return employerJobs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Employer ").append(employerName).append("\nInternships:");
        for(Job job : employerJobs) {
            sb.append(job).append(" - ");
            sb.append(job.isJobActive() ? "active" : "inactive").append('\n');
        }

        return sb.toString();
    }

    @Override
    public String convertToCSV() {

        StringBuilder stringBuilder = new StringBuilder(
                String.join(",",
                employerName,
                String.valueOf(employerJobs.size()),
                String.valueOf(employerWebinars.size())

        ));

        if (employerJobs.size() > 0) {
            stringBuilder.append(',');
            stringBuilder.append(
                    employerJobs.stream()
                            .map(Job::convertToCSV)
                            .collect(Collectors.joining(","))
            );
        }

        if (employerWebinars.size() > 0) {
            stringBuilder.append(',');
            stringBuilder.append(
                    employerWebinars.stream()
                            .map(Webinar::convertToCSV)
                            .collect(Collectors.joining(","))
            );
        }

        return stringBuilder.toString();
    }

}
