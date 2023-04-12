import exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InternshipService
{
    private static InternshipService instance = null;

    List<Employer> employerList;
    List<Student> studentList;

    private InternshipService() {
        employerList = new ArrayList<>();
        studentList = new ArrayList<>();
    }

    public static InternshipService getInstance() {
        if (instance == null)
            instance = new InternshipService();
        return instance;
    }

    public void listEmployers() {
        employerList.forEach(System.out::println);
    }

    public void listStudents() {
        studentList.forEach(System.out::println);
    }

    public void addEmployer(String name) {
        employerList.add(new Employer(name));
    }

    public void addStudent(String firstName, String lastName, String birthday, String email,
                           String phoneNumber, String university, String headline) {
        Student student;
        try {
            student = new Student.ProfileBuilder(firstName, lastName, birthday, email)
                    .setUniversity(university)
                    .setHeadline(headline)
                    .setPhoneNumber(phoneNumber)
                    .build();
            studentList.add(student);
        } catch (IncorrectEmailException e) {
            System.out.println("Incorrect email");
        } catch (IncorrectPhoneNumberException e) {
            System.out.println("Incorrect phone");
        }
    }

    public void addJob(String employerName, String jobTitle, int periodInMonths, int salary)
            throws EmployerNotFoundException, CloneNotSupportedException {
        Employer employer = findEmployer(employerName);

        employer.addJob(new Job(jobTitle, periodInMonths, salary));
    }

    private Student findStudent(String firstName, String lastName) throws StudentNotFoundException {
        Optional<Student> student = studentList.stream()
                .filter(s -> s.getFirstName().equals(firstName) && s.getLastName().equals(lastName))
                .findFirst();
        if(student.isEmpty()) {
            throw new StudentNotFoundException(firstName + " " + lastName + " not found!");
        }
        return student.get();
    }

    private Employer findEmployer(String employerName) throws EmployerNotFoundException {
        Optional<Employer> employer = employerList.stream()
                .filter(e -> e.getEmployerName().equals(employerName))
                .findFirst();
        if (employer.isEmpty()) {
            throw new EmployerNotFoundException(employerName + " not found!");
        }
        return employer.get();
    }

    private Job findJob(String companyName, String jobName) throws EmployerNotFoundException, JobNotFoundException {
        Employer employer = findEmployer(companyName);

        Optional<Job> job = employer.getEmployerJobs().stream()
                .filter(j -> j.getJobTitle().equals(jobName))
                .findFirst();
        if (job.isEmpty()) {
            throw new JobNotFoundException(jobName + " not found!");
        }
        return job.get();
    }

    public void studentApply (String firstName, String lastName, String companyName, String jobName) {
        try {
            Job job = findJob(companyName, jobName);
            Student student = findStudent(firstName, lastName);

            studentApply(student, job);
        } catch (EmployerNotFoundException | JobNotFoundException | StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void studentApply(Student student, Job job) {
        student.applyForJob(job);
    }

    public void removeJobApplication (String firstName, String lastName, String companyName, String jobName) {
        try {
            Job job = findJob(companyName, jobName);
            Student student = findStudent(firstName, lastName);

            removeJobApplication(student, job);
        } catch (EmployerNotFoundException | JobNotFoundException | StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeJobApplication(Student student, Job job) {
        student.removeJobApplication(job);
    }

    public void addEducationFieldToStudent (String firstName, String lastName, YearMonth startDate,
                                            YearMonth finishDate, String description, String institutionName, String specializationName)
            throws StudentNotFoundException, CloneNotSupportedException {
        Student student = findStudent(firstName, lastName);

        Student.ProfileBuilder profileBuilder = new Student.ProfileBuilder(student);
        profileBuilder.addEducationField(startDate, finishDate, description, institutionName, specializationName);
        student.update(profileBuilder.build());
    }

    public void addExperienceFieldToStudent (String firstName, String lastName, YearMonth startDate,
                                             YearMonth finishDate, String description, String positionName, String institutionName)
            throws StudentNotFoundException, CloneNotSupportedException {
        Student student = findStudent(firstName, lastName);

        Student.ProfileBuilder profileBuilder = new Student.ProfileBuilder(student);
        profileBuilder.addExperienceField(startDate, finishDate, description, positionName, institutionName);
        student.update(profileBuilder.build());
    }

    public void addProjectFieldToStudent (String firstName, String lastName, YearMonth startDate,
                                          YearMonth finishDate, String description, String projectName)
            throws StudentNotFoundException, CloneNotSupportedException {
        Student student = findStudent(firstName, lastName);

        Student.ProfileBuilder profileBuilder = new Student.ProfileBuilder(student);
        profileBuilder.addProjectField(startDate, finishDate, description, projectName);
        student.update(profileBuilder.build());
    }

    public List<Employer> getEmployerList() {
        return employerList;
    }

    public List<Student> getStudentList() {
        return studentList;
    }
}
