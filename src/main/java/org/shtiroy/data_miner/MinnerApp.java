package org.shtiroy.data_miner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.datemd.Menu;
import org.shtiroy.data_miner.datemd.entity.Company;
import org.shtiroy.data_miner.datemd.entity.CompanyJSON;
import org.shtiroy.data_miner.datemd.repository.CompanyJSONRepository;
import org.shtiroy.data_miner.datemd.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class MinnerApp implements CommandLineRunner {
    private static final Logger LOGGER = LogManager.getLogger(MinnerApp.class.getName());
    @Autowired
    private Menu menu;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyJSONRepository companyJSONRepository;
    public static void main(String[] args) {
        SpringApplication.run(MinnerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("start project");
        menu.startMenu();
        //List<Company> companies = companyRepository.findAll();
        //List<CompanyJSON> companyJSONS = companyJSONRepository.findAll();
        LOGGER.info("end project");
    }
}
