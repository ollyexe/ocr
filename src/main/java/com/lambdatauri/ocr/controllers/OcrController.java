package com.lambdatauri.ocr.controllers;

import com.lambdatauri.ocr.dto.PdfDto;
import com.lambdatauri.ocr.utils.OcrMode;
import com.lambdatauri.ocr.utils.PageSegmentationMode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequiredArgsConstructor
public class OcrController {


    private final Logger LOG = LoggerFactory.getLogger(OcrController.class);

    private final Tesseract tesseract;

    final Environment environment;


    @PostMapping(value = "ocr", consumes = {"multipart/form-data"})
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> ocr( @RequestPart("file") MultipartFile multipartFile,
                      @RequestParam(defaultValue = "eng") String[] lang
                      )
            throws IOException, TesseractException {
        File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
        try {
            tesseract.setDatapath(environment.getProperty("tesseract.data.path"));

            tesseract.setLanguage(String.join("+", lang));
            tesseract.setPageSegMode(PageSegmentationMode.PSM_AUTO.ordinal());
            tesseract.setOcrEngineMode(OcrMode.OEM_TESSERACT_LSTM_COMBINED.ordinal());
            multipartFile.transferTo(file);
            if (!file.exists()){
                LOG.info(file.getAbsolutePath()+"File not found");
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }
            String result = tesseract.doOCR(file);
            LOG.info("Ocr's been here");
            return new ResponseEntity<>(PdfDto.builder().fileName(multipartFile.getOriginalFilename()).text(result).build(),HttpStatus.OK);
        } catch (TesseractException e) {
            LOG.error("Error in ocr :"+e.getMessage());
        }

        return new ResponseEntity<>("Error",HttpStatus.NOT_FOUND);
    }




    @Controller
    public class WebController {

        @RequestMapping(value = "/", method = RequestMethod.GET)
        public String index(HttpServletRequest request) {
            return "redirect:/swagger-ui.html";
        }

    }
}
