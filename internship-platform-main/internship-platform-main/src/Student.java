import exceptions.IncorrectEmailException;
import exceptions.IncorrectPhoneNumberException;
import fields.EducationField;
import fields.ExperienceField;
import fields.ProjectField;
import services.CSVConvertible;
import services.Loggable;
import services.LoggingService;
import services.Parsable;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

//Clasa modeleaza un profil de utilizator student care contine
//campuri atat obligatorii cat si neobligatorii
//ca sa treaca validarea, cele obligatorii trebuie sa nu fie null
public class Student implements Observer, Parsable<Student>, CSVConvertible, Loggable {
    public void updateObserver(String message){
        inbox.add(new InboxMessage(message));
    }

    public void seeUnreadMessages() {
        for (InboxMessage inboxMessage : inbox) {
            System.out.println(inboxMessage.getMessage());
        }
    }

    //obligatorii
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;

    //optionale
    private String university;
    private String headline;
    private Set<EducationField> educationFields = new TreeSet<>();
    private Set<ExperienceField> experienceFields = new TreeSet<>();
    private Set<ProjectField> projectFields = new TreeSet<>();
    private List<InboxMessage> inbox = new ArrayList<>();

    LoggingService<Student> loggingService = new LoggingService<>(Student.class);

    private Student(ProfileBuilder profileBuilder){
        this.firstName = profileBuilder.firstName;
        this.lastName = profileBuilder.lastName;
        this.birthday = profileBuilder.birthday;
        this.email = profileBuilder.email;
        this.university = profileBuilder.university;
        this.headline = profileBuilder.headline;
        this.educationFields = profileBuilder.educationFields;
        this.experienceFields = profileBuilder.experienceFields;
        this.projectFields = profileBuilder.projectFields;
    }

    public void applyForJob(Job job) {
        job.subscribe(this);
        loggingService.logAction("applyForJob", LocalDateTime.now());
    }

    public void removeJobApplication(Job job) {
        job.unsubscribe(this);
        loggingService.logAction("removeJobApplication", LocalDateTime.now());
    }

    @Override
    public Student parseCSVLine(String line) {

        String[] fields = line.split(",");

        if (line.equals("test")) {
            Arrays.stream(fields)
                    .forEach(System.out::println);


            return null;
        }

        int fieldsIndex = 0;

        String firstName = fields[fieldsIndex];
        String lastName = fields[++fieldsIndex];
        String birthday = fields[++fieldsIndex];
        String email = fields[++fieldsIndex];

        Student.ProfileBuilder profileBuilder;

        try {
            profileBuilder = new ProfileBuilder(firstName, lastName, birthday, email);
        } catch (IncorrectEmailException incorrectEmailException) {
            incorrectEmailException.printStackTrace();
            return null;
        }

        String university = fields[++fieldsIndex];
        String headline = fields[++fieldsIndex];

        if (!university.equals("null")) {
            profileBuilder.setUniversity(university);
        }

        if (!headline.equals("null")) {
            profileBuilder.setHeadline(headline);
        }

        int noOfEducationFields = Integer.parseInt(fields[++fieldsIndex]);
        int noOfExperienceFields = Integer.parseInt(fields[++fieldsIndex]);
        int noOfProjectFields = Integer.parseInt(fields[++fieldsIndex]);

        int noOfInboxMessages = Integer.parseInt(fields[++fieldsIndex]);
        

        // pentru fiecare EducationField
        // fieldsIndex va creste cu 5
        for(int i = 0; i < noOfEducationFields; i++) {

            String startDateString = fields[++fieldsIndex];
            YearMonth startDate = YearMonth.parse(startDateString);

            String finishDateString = fields[++fieldsIndex];
            YearMonth finishDate = YearMonth.parse(finishDateString);

            String description = fields[++fieldsIndex];

            String institutionName = fields[++fieldsIndex];

            String specializationName = fields[++fieldsIndex];

            profileBuilder.addEducationField(startDate, finishDate, description, institutionName, specializationName);
        }

        // pentru fiecare ExperienceField
        // fieldsIndex va creste cu 5
        for (int i = 0; i < noOfExperienceFields; i++) {

            String startDateString = fields[++fieldsIndex];
            YearMonth startDate = YearMonth.parse(startDateString);

            String finishDateString = fields[++fieldsIndex];
            YearMonth finishDate = YearMonth.parse(finishDateString);

            String description = fields[++fieldsIndex];

            String positionName = fields[++fieldsIndex];

            String institutionName = fields[++fieldsIndex];

            profileBuilder.addExperienceField(startDate, finishDate, description, positionName, institutionName);
        }

        for (int i = 0; i < noOfProjectFields; i++) {

            String startDateString = fields[++fieldsIndex];
            YearMonth startDate = YearMonth.parse(startDateString);

            String finishDateString = fields[++fieldsIndex];
            YearMonth finishDate = YearMonth.parse(finishDateString);

            String description = fields[++fieldsIndex];

            String projectName = fields[++fieldsIndex];

            profileBuilder.addProjectField(startDate, finishDate, description, projectName);
        }

        Student student = profileBuilder.build();
        for (int i = 0; i < noOfInboxMessages; i++) {

            String message = fields[++fieldsIndex];

            String readString = fields[++fieldsIndex];
            boolean read = Boolean.parseBoolean(readString);

            student.addInboxMessage(new InboxMessage(message, read));
        }


        return student;
    }

    @Override
    public String convertToCSV() {
        StringBuilder stringBuilder = new StringBuilder();
        String essentials = String.join(",",
                Arrays.asList(firstName, lastName, birthday.toString(), email, university, headline,
                        String.valueOf(educationFields.size()),
                        String.valueOf(experienceFields.size()),
                        String.valueOf(projectFields.size()),
                        String.valueOf(inbox.size())
                            ));

        String educationFieldsJoin = educationFields
                .stream()
                .map(EducationField::convertToCSV)
                .collect(Collectors.joining(","));

        String experienceFieldsJoin = experienceFields
                .stream()
                .map(ExperienceField::convertToCSV)
                .collect(Collectors.joining(","));

        String projectFieldsJoin = projectFields
                .stream()
                .map(ProjectField::convertToCSV)
                .collect(Collectors.joining(","));

        String inboxJoin = inbox
                .stream()
                .map(InboxMessage::convertToCSV)
                .collect(Collectors.joining(","));

        stringBuilder.append(String.join(",",
                essentials, educationFieldsJoin, experienceFieldsJoin, projectFieldsJoin, inboxJoin));

        return stringBuilder.toString();

    }

    static public class ProfileBuilder{
        //obligatorii
        private String firstName;
        private String lastName;
        private LocalDate birthday;
        private String email;

        //optionale
        private String university;
        private String headline;
        private Set<EducationField> educationFields = new TreeSet<>();
        private Set<ExperienceField> experienceFields = new TreeSet<>();
        private Set<ProjectField> projectFields = new TreeSet<>();
        private char[] phoneNumber = new char[10];

        public ProfileBuilder(Student student) throws CloneNotSupportedException {
            this.firstName = student.firstName;
            this.lastName = student.lastName;
            this.birthday = student.birthday;
            this.email = student.email;
            this.university = student.university;
            this.headline = student.headline;
            this.educationFields = student.educationFields;
            this.experienceFields = student.experienceFields;
            this.projectFields = student.projectFields;
        }

        public ProfileBuilder(String firstName, String lastName, String birthday, String email) throws IncorrectEmailException {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(emailRegex, email)) {
                throw new IncorrectEmailException("Incorrect email: " + email);
            }


            try {
                //Locale.setDefault(Locale.FRANCE);
                this.birthday = LocalDate.parse(birthday);
            } catch (DateTimeParseException e){
                System.out.println("error formatting birthday");
            }
        }

        public ProfileBuilder setUniversity(String university){
            this.university = university;
            return this;
        }

        public ProfileBuilder setHeadline(String headline){
            this.headline = headline;
            return this;
        }

        public ProfileBuilder setPhoneNumber(String phoneNumber) throws IncorrectPhoneNumberException {
            String phoneRegex = "^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}$";
            if (!Pattern.matches(phoneRegex, phoneNumber)) {
                throw new IncorrectPhoneNumberException("Incorrect phone number: " + phoneNumber);
            }
            return this;
        }


        public ProfileBuilder addEducationField(YearMonth startDate, YearMonth finishDate, String description, String institutionName, String specializationName){
            this.educationFields.add(new EducationField(startDate, finishDate, description, institutionName, specializationName));
            return this;
        }

        public ProfileBuilder addExperienceField(YearMonth startDate, YearMonth finishDate, String description, String positionName, String institutionName){
            this.experienceFields.add(new ExperienceField(startDate, finishDate, description, positionName, institutionName));
            return this;
        }

        public ProfileBuilder addProjectField(YearMonth startDate, YearMonth finishDate, String description, String projectName){
            this.projectFields.add(new ProjectField(startDate, finishDate, description, projectName));
            return this;
        }

        public Student build(){
            return new Student(this);
        }

    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", email='" + email + '\'' +
                ", university='" + university + '\'' +
                ", headline='" + headline + '\'' + '\n' +
                ", educationFields=" + educationFields +
                ", experienceFields=" + experienceFields +
                ", projectFields=" + projectFields +
                ", inbox=" + inbox +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void addInboxMessage(InboxMessage inboxMessage) {
        inbox.add(inboxMessage);
    }

    public void update(Student student) {
        this.firstName = student.firstName;
        this.lastName = student.lastName;
        this.birthday = student.birthday;
        this.university = student.university;
        this.headline = student.headline;
        this.educationFields = student.educationFields;
        this.experienceFields = student.experienceFields;
        this.projectFields = student.projectFields;
    }

    public Student() { }

    public Set<EducationField> getEducationFields() {
        return educationFields;
    }

    public Set<ExperienceField> getExperienceFields() {
        return experienceFields;
    }

    public Set<ProjectField> getProjectFields() {
        return projectFields;
    }

    public List<InboxMessage> getInbox() {
        return inbox;
    }
}
