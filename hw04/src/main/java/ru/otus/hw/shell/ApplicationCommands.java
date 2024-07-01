package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestRunnerService testRunnerService;

    private final StudentService studentService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login() {
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
        return String.format("Добро пожаловать: %s", student.getFullName());
    }

    @ShellMethod(value = "Start test", key = {"s", "start"})
    @ShellMethodAvailability(value = "isPublishEventCommandAvailable")
    public void start() {
        testRunnerService.run();
    }

    private Availability isPublishEventCommandAvailable() {
        return loginContext.isStudentLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Сначала залогиньтесь");
    }
}
