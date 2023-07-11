package ru.team.backend.export;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.team.backend.entities.Book;
import ru.team.backend.entities.Form;
import ru.team.backend.entities.User;
import ru.team.backend.functional.CountFunctional;
import ru.team.backend.functional.FormFunctional;
import ru.team.backend.repositories.BookRepository;
import ru.team.backend.repositories.FormRepository;
import ru.team.backend.repositories.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Service
public class ExcelExportService {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final FormRepository formRepository;

    @Autowired
    private final BookRepository bookRepository;

    private CountFunctional countFunctional;


    private final XSSFWorkbook wb;
    private final XSSFSheet sheet;

    String[] nameColumns = {
        "Почта пользователя","Название книги","Автор", "Описание", "Дата приобретения", "Дата возврата", "Фактическая дата возврата","Пени", "Дни просрочки"
    };



    public ExcelExportService(UserRepository userRepository, FormRepository formRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.bookRepository = bookRepository;
        this.wb = new XSSFWorkbook();
        this.sheet = wb.createSheet();

    }


    public String exportData(String name, Date dateOfBegin, Date dateOfEnd){


        List<Form> forms = (List<Form>) formRepository.findAllByDatePeriod(dateOfBegin, dateOfEnd);

        countFunctional = new FormFunctional();
        List<Form> newFrom = countFunctional.countUserPenaltiesForPeriod(forms,dateOfBegin, dateOfEnd);


        int rowNum = 1;
        createTitle();
        for(Form form : newFrom) createRow(rowNum++,form);
        try {
            writeWorkbook();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "ok";
    }

    private void createTitle() {
        XSSFRow rowTitle = sheet.createRow(0);
        setCellValue(rowTitle.createCell(0), "Почта пользователя");
        setCellValue(rowTitle.createCell(1), "Название книги");
        setCellValue(rowTitle.createCell(2), "Автор");
        setCellValue(rowTitle.createCell(3), "Описание");
        setCellValue(rowTitle.createCell(4), "Дата приобретения");
        setCellValue(rowTitle.createCell(5), "Дата возврата");
        setCellValue(rowTitle.createCell(6), "Фактическая дата возврата");
        setCellValue(rowTitle.createCell(7), "Пени");
        setCellValue(rowTitle.createCell(8), "Дни просрочки");
    }


    public void createRow(int index, Form form) {
        XSSFRow row = sheet.createRow(index);
        User user = userRepository.findById(form.getUserId()).get();
        Book book = bookRepository.findById(form.getBookId()).get();
        setCellValue(row.createCell(0), user.getEmail());
        setCellValue(row.createCell(1), book.getTitle());
        setCellValue(row.createCell(2), book.getAuthor());
        setCellValue(row.createCell(3), book.getDescription());
        setCellValue(row.createCell(4), form.getDateOfTaking().toString());
        setCellValue(row.createCell(5), form.getTermOfReturning().toString());
        if(form.getDateOfReturning() != null)
            setCellValue(row.createCell(6), form.getDateOfReturning().toString());
        else setCellValue(row.createCell(6), "");
        setCellValue(row.createCell(7), form.getPenalties());
        setCellValue(row.createCell(8), form.getDelay());
    }


    private void setCellValue(XSSFCell cell, String value) {
        cell.setCellValue(value);
    }

    private void setCellValue(XSSFCell cell, int value) {
        cell.setCellValue(value);
    }

    public void writeWorkbook() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(Instant.now().getEpochSecond() + ".xlsx");
        wb.write(fileOut);
        fileOut.close();
    }


    }

