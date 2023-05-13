package cse.java2.project.controller;

import java.io.IOException;
import java.sql.SQLException;

public class dataCollectMain {
    public static void main(String[] args) throws SQLException, IOException {
        // TODO: change the username and password to your own
        databaseService databaseService = new databaseService("slina","990522","cs209a");
        // pageStep is used to manage the step between two pages that are crawled
        // pageSize is the number of questions that are crawled in one page,
        // the maximum is 100
        // pageTotal is the total number of pages that are crawled
        dataCollection dataCollection = new dataCollection(3,1,5,
                databaseService);
        databaseService.connect();
        databaseService.createTable();
        dataCollection.collectData();
    }
}
