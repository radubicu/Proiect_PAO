import exceptions.EmployerNotFoundException;
import exceptions.StudentNotFoundException;
import fields.EducationField;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IOService;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

public class Main {

    public static void testStudent(String[] args) throws IOException {

        IOService<Student> studentIOService = new IOService<>(Student::new);

        List<Student> studentList = studentIOService.retrieveObjects("students.csv");

        Student student = studentList.get(0);

        System.out.println( student.toString() );

        studentIOService.saveObjects("students2.csv", studentList);

    }

    public static void main(String[] args)
            throws CloneNotSupportedException, EmployerNotFoundException, StudentNotFoundException {


        InternshipService service = InternshipService.getInstance();

        service.addEmployer("Google");

        service.addJob("Google", "DevOps Intern", 3, 3000);
        service.addJob("Google", "Java Developer Intern", 3, 3200);
        service.addJob("Google", "FrontEnd Developer Intern", 3, 2800);

        service.getEmployerList().get(0)
                .getEmployerJobs().get(0).addJobRequirement("req1");


        String test = service.getEmployerList().stream().findFirst().get().convertToCSV();

        System.out.println(test);
    }

    public static void abc(String[] args)
            throws CloneNotSupportedException, EmployerNotFoundException, StudentNotFoundException {

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        fields.TimelineField t = context.getBean("educationField", EducationField.class);

        System.out.println(t.getDescription());

        context.close();

        InternshipService service = InternshipService.getInstance();


        service.addEmployer("Google");
        service.addEmployer("Amazon");
        service.addEmployer("Microsoft");

        service.addJob("Google", "DevOps Intern", 3, 3000);
        service.addJob("Google", "Java Developer Intern", 3, 3200);
        service.addJob("Google", "FrontEnd Developer Intern", 3, 2800);

        service.addJob("Amazon", "Junior Data Scientist", 6, 4000);
        service.addJob("Amazon", "Software Development Intern", 3, 3200);
        service.addJob("Amazon", "AWS Cloud Support Associate", 6, 3500);

        service.addJob("Microsoft", "Junior Data Scientist", 6, 4000);
        service.addJob("Microsoft", "Software Development Intern", 3, 3200);
        service.addJob("Microsoft", "Technical Engineer Support", 6, 4000);

        service.addStudent("Bicu", "Radu",
                "2000-06-12", "radubicu@gmail.com",
                "0712-123-123",
                "Faculty of Mathematics and Computer Science, University of Bucharest",
                "Second year Computer Science student");
        service.addStudent("Andreea", "Ciurescu",
                "2001-09-25", "andreea.ciurescu@sunibuc.ro",
                "0723-234-234",
                "Faculty of Mathematics and Computer Science, University of Bucharest",
                "First year Computer Science student"
                );

        service.studentApply("Radu", "Bicu", "Google", "Java Developer Intern");

        service.removeJobApplication("Radu", "Bicu", "Google", "Java Developer Intern");

        service.addEducationFieldToStudent("Radu", "Bicu", YearMonth.parse("2023-10"), YearMonth.parse("2023-06"),
                "Relevant courses:", "Faculty of Mathematics and Computer Science", "Computer Science");

        service.addProjectFieldToStudent("Radu","Bicu", YearMonth.parse("2023-01"), YearMonth.parse("2023-02"),
                "C++ OOP code that imitates basic SQL DDL, DML and DQL functionality.", "SQL in C++");


    }
}
