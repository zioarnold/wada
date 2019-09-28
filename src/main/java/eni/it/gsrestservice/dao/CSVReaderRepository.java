package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.CSVReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CSVReaderRepository implements Dao<CSVReader> {

    private List<CSVReader> dataFromCSV = new ArrayList<>();

    @Override
    public Optional<CSVReader> get(long id) {
        return Optional.ofNullable(dataFromCSV.get((int) id));
    }

    @Override
    public List<CSVReader> getAll() {
        return dataFromCSV;
    }

    @Override
    public void save(CSVReader csvReader) {
        dataFromCSV.add(csvReader);
    }

    @Override
    public void update(CSVReader csvReader, String[] params) {

    }

    @Override
    public void delete(CSVReader csvReader) {

    }
}
