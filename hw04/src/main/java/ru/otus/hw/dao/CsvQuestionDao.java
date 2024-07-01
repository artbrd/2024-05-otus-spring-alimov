package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        try (var inputStreamReader = readToInputStreamReader(fileNameProvider.getTestFileName())) {
            var csvToBean = new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build();

            return csvToBean.parse()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException("Error reading file " + fileNameProvider.getTestFileName(), e);
        }
    }

    private InputStreamReader readToInputStreamReader(String fileName) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        return new InputStreamReader(new ClassPathResource(fileName).getInputStream());
    }
}
