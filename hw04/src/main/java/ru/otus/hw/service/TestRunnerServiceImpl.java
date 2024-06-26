package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.LoginContext;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final ResultService resultService;

    private final LoginContext loginContext;

    @Override
    public void run() {
        var testResult = testService.executeTestFor(loginContext.getStudent());
        resultService.showResult(testResult);
    }
}
