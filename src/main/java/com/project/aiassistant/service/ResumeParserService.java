package com.project.aiassistant.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    /**
     * Parses the content of an uploaded file to extract raw text.
     *
     * @param inputStream The input stream of the file.
     * @return The extracted text content.
     * @throws IOException   if there is an issue reading the stream.
     * @throws TikaException if there is an error during parsing.
     */
    public String parse(InputStream inputStream) throws IOException, TikaException {
        // The Tika facade automatically detects the file type and parses the text.
        // It's powerful and simple to use for this common case.
        return tika.parseToString(inputStream);
    }
}
