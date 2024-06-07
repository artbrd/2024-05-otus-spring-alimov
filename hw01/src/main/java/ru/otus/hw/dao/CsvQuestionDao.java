package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        var csvToBean = new CsvToBeanBuilder<QuestionDto>(readToInputStreamReader(fileNameProvider.getTestFileName()))
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .build();

        return csvToBean.parse()
                .stream()
                .map(QuestionDto::toDomainObject)
                .toList();
    }

    private InputStreamReader readToInputStreamReader(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        try {
            return new InputStreamReader(new ClassPathResource(fileName).getInputStream());
        } catch (IOException e) {
            throw new QuestionReadException("Error reading file " + fileName, e);
        }
    }
}
