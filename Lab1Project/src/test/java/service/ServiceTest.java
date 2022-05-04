package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    static Service service;

    @BeforeAll
    static void init() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    void findAllStudentsShouldNotReturnNull() {
        assertNotNull(service.findAllStudents());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 109, 939})
    void saveStudentShouldReturn1IfGroupIsNotBetween110And938(int group) {
        assertEquals(1, service.saveStudent("Test", "Test", group));
    }

    @Test
    void saveHomeworkShouldNotReturn0IfStartlineIsGreaterThanDeadline() {
        assertNotEquals(0, service.saveHomework("Test", "Test", 1, 3));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 16, 3})
    void saveGradeShouldNotReturn0IfGradeIsNotBetween0And10(double grade) {
        service.saveStudent("Test", "Test", 111);
        service.saveHomework("Test", "Test", 3, 1);
        assertNotEquals(0, service.saveGrade("Test", "Test", grade, 5, "Test"));
        service.deleteStudent("Test");
        service.deleteHomework("Test");
    }

    @Test
    void saveGradeShouldReturn1IfDeliveryIsBeforeStartline() {
        service.saveStudent("Test", "Test", 111);
        service.saveHomework("Test", "Test", 3, 2);
        assertEquals(1, service.saveGrade("Test", "Test", 10, 1, "Test"));
        service.deleteStudent("Test");
        service.deleteHomework("Test");
    }

}