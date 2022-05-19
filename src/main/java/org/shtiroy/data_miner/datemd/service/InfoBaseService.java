package org.shtiroy.data_miner.datemd.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public boolean getJson(int count){
        LOGGER.info("get json from InfoBase");
        List<Company> companies;
        try {
            companies = companyRepository.findAllActiveCompany(count);
        } catch (DataAccessException ex){
            LOGGER.error("Error get companies: " + ex.getMessage());
            return false;
        }
        UrlParse urlParse = new UrlParse();
        for (Company company : companies){
            LOGGER.info("get company: " + company.toString());
            if (company.getIdno().length() > 11){
                String url = "https://www.data2b.md/api/companies/" + company.getIdno() + "/";
                String strJson = (String) urlParse.methodGet(url);
                try{
                    Thread.sleep(30000);
                } catch (InterruptedException ex){
                    LOGGER.error(ex.getMessage());
                }
                if (strJson == null){
                    break;
                }
                CompanyJSON companyJSON = new CompanyJSON();
                companyJSON.setIdno(company.getIdno());
                companyJSON.setCompanyData(strJson);
                companyJSON.setCreateTs(new Timestamp(System.currentTimeMillis()));
                try {
                    //companyJSONRepository.save(companyJSON);
                    companyJSONRepository.saveCompany(companyJSON.getIdno(),companyJSON.getCreateTs(),companyJSON.getCompanyData());
                } catch (DataAccessException ex){
                    LOGGER.error("Error insert company json:" + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
}
