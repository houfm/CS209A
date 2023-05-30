package cse.java2.project.dataCollect;

import java.io.IOException;
import java.sql.SQLException;

public class dataCollectMain {
  public static void main(String[] args) throws SQLException, IOException {
    //TODO: drop我给你删了，感觉还是太危险，贴下面了
    // "drop table if exists comment;\n" +
    // "drop table if exists answer;\n" +
    // "drop table if exists tag;\n" +
    // "drop table if exists question;\n" +
    String string = "create table question (\n" +
            "    question_id int primary key,\n" +
            "    score int not null,\n" +
            "    link text not null,\n" +
            "    answer_count int not null,\n" +
            "    view_count int not null,\n" +
            "    content_license text,\n" +
            "    title text not null,\n" +
            "    last_activity_date timestamp not null,\n" +
            "    last_edit_date timestamp,\n" +
            "    creation_date timestamp not null,\n" +
            "    account_id int not null,\n" +
            "    body text not null \n" +
            ");\n" +
            "create table tag (\n" +
            "    id int primary key, \n" +
            "    tag_name text not null, \n" +
            "    question_id int not null \n" +
            ");\n" +
            "create table answer(\n" +
            "    answer_id int primary key,\n" +
            "    last_activity_date timestamp not null,\n" +
            "    last_edit_date timestamp,\n" +
            "    creation_date timestamp not null,\n" +
            "    score int not null,\n" +
            "    is_accepted bool not null,\n" +
            "    content_license text,\n" +
            "    question_id int not null,\n" +
            "    body text not null,\n" +
            "    account_id int not null,\n" +
            "    foreign key (question_id) references question(question_id)\n" +
            ");\n" +
            "create table comment (\n" +
            "    comment_id int primary key,\n" +
            "    edited bool not null,\n" +
            "    post_id int not null,\n" +
            "    body text not null,\n" +
            "    creation_date timestamp not null,\n" +
            "    score int not null,\n" +
            "    content_license text,\n" +
            "    account_id int not null,\n" +
            "    foreign key (post_id) references  answer(answer_id)\n" +
            ")";
    // TODO: change the username and password to your own
    databaseService databaseService = new databaseService("slina", "990522", "cs209a");
    // pageStep is used to manage the step between two pages that are crawled
    // pageSize is the number of questions that are crawled in one page,
    // the maximum is 100
    // pageTotal is the total number of pages that are crawled
    dataCollection dataCollection = new dataCollection(3, 1, 5,
            databaseService);
    databaseService.connect();
    databaseService.createTable();
    dataCollection.collectData();
  }
}
