package org.shtiroy.data_miner.datemd.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PGobject;
import org.shtiroy.data_miner.datemd.exception.ParseExcelError;
import org.shtiroy.data_miner.datemd.entity.Company;
import org.shtiroy.data_miner.datemd.entity.CompanyJSON;
import org.shtiroy.data_miner.datemd.repository.CompanyJSONRepository;
import org.shtiroy.data_miner.datemd.repository.CompanyRepository;
import org.shtiroy.data_miner.datemd.utils.FileParse;
import org.shtiroy.data_miner.datemd.utils.UrlParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfoBaseService {
    private static final Logger LOGGER = LogManager.getLogger(InfoBaseService.class.getName());
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyJSONRepository companyJSONRepository;

    private CompanyJSON getFromData2b(Company company) throws DataAccessException{
        UrlParse urlParse = new UrlParse();
        String url = "https://www.data2b.md/api/companies/" + company.getIdno() + "/";
        String strJson = (String) urlParse.methodGet(url);
        CompanyJSON companyJSON = new CompanyJSON();
        companyJSON.setIdno(company.getIdno());
        companyJSON.setCompanyData(strJson);
        companyJSON.setCreateTs(new Timestamp(System.currentTimeMillis()));
        companyJSON.setResource("data2b.md");
        return companyJSON;
    }

    /**
     * Вставка данных в таблицу с фирмами.
     * @param fileName - имя файла для парсинга.
     * @return - true если све прошло удачно.
     */
    public boolean startParseFile(String fileName){
        LOGGER.info("start insert file info base");
        FileParse fileParse = new FileParse();
        List<Company> companies;
        try {
            companies = fileParse.parseExcel(fileName);
        } catch (ParseExcelError excelError){
            LOGGER.error(excelError.getMessage());
            return false;
        }

        int count = companies.size();
        int countTemp = 0;
        List<Company> temp = new ArrayList<>();
        for (Company company : companies){
            temp.add(company);
            countTemp++;
            count--;
            if (countTemp > 100){
                try {
                    companyRepository.saveAll(temp);
                    companyRepository.save(company);
                } catch (DataAccessException ex){
                    LOGGER.error("Error company " + company.toString());
                    LOGGER.error("Error insert companies:" + ex.getMessage());
                    return false;
                }
                countTemp = 0;
                temp.clear();
                LOGGER.info("insert " + count);
            }
        }
        try {
            companyRepository.saveAll(temp);
        } catch (DataAccessException ex){
            LOGGER.error("Error insert companies:" + ex.getMessage());
            return false;
        }
        LOGGER.info("end parse file " + fileName);
        return true;
    }

    public boolean getJson(int count) {
        LOGGER.info("get json from InfoBase");
        List<Company> companies;
        try {
            companies = companyRepository.findAllActiveCompany(count);
        } catch (DataAccessException ex){
            LOGGER.error("Error get companies: " + ex.getMessage());
            return false;
        }
        for (Company company : companies){
            LOGGER.info("get company: " + company.toString());
            if (company.getIdno().length() > 11){
                CompanyJSON companyJSON = getFromData2b(company);
                companyJSONRepository.saveCompany(companyJSON.getIdno(),companyJSON.getCreateTs(),
                        companyJSON.getCompanyData().toString(),companyJSON.getResource());
                try{
                    Thread.sleep(30000);
                } catch (InterruptedException ex){
                    LOGGER.error(ex.getMessage());
                }
            }
        }
        return true;
    }

    public void parseFromFile(){
        try {
            List<String> idnoList = new ArrayList<>();
            BufferedReader br
                     = new BufferedReader(new FileReader("C:/Users/User/Downloads/kongo/kongo.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                idnoList.add(line);
            }
            int i = 0;
            for (String company : idnoList){
                i++;
                LOGGER.info("get company: " + i + " " + company);
                if (company.length() > 11){
                    CompanyJSON companyJSON = getFromData2b(companyRepository.findByIdno(company));
                    try {
                        companyJSONRepository.saveCompany(companyJSON.getIdno(),companyJSON.getCreateTs(),
                                companyJSON.getCompanyData(),companyJSON.getResource());
                    } catch (DataAccessException ex){
                        LOGGER.error("Error insert company json:" + ex.getMessage());
                    }
                }
            }
        } catch(IOException ex){
            LOGGER.info("Error " + ex.getMessage());
        }
    }
}
