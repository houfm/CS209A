package cse.java2.project.dataCollect;

import com.alibaba.fastjson.JSONObject;


import java.sql.*;

public class databaseService {
    private Connection connection;
    private String user;
    private String password;
    private String database;

    private int tag_id;

    public databaseService(String user, String password, String database) {
        this.user = user;
        this.password = password;
        this.database = database;
        connection = null;
        tag_id = 0;
    }

    public boolean connect() {
        // 连接数据库
        try {
            connection = java.sql.DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database,
                    user,
                    password
            );
            return true;
        } catch (java.sql.SQLException e) {
            System.out.println("Cannot connect to the database.");
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        // 关闭数据库连接
        try {
            connection.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Cannot close the connection.");
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(String sql) {
        // 准备 SQL 语句
        try {
            return connection.prepareStatement(sql);
        } catch (java.sql.SQLException e) {
            System.out.println("Cannot prepare the statement.");
            e.printStackTrace();
            return null;
        }
    }

    public void createTable() throws SQLException {
        // 创建数据库表
        Statement statement = this.connection.createStatement();
        statement.executeUpdate("drop table if exists comment;\n" +
                "drop table if exists answer;\n" +
                "drop table if exists tag;\n" +
                "drop table if exists question;\n" +
                "create table question (\n" +
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
                ")");
    }

    public void disableForeignKeyCheck() throws SQLException {
        // 关闭外键约束
        Statement statement = connection.createStatement();
        statement.execute("SET session_replication_role = replica;");
        statement.close();
    }

    public void enableForeignKeyCheck() throws SQLException {
        // 开启外键约束
        Statement statement = connection.createStatement();
        statement.execute("SET session_replication_role = DEFAULT;");
        statement.close();
    }

    public void insertQuestion(int question_id, Timestamp last_activity_date, Timestamp last_edit_date, Timestamp creation_date, int score, String link, int answer_count, int view_count, String content_license, String title, int account_id, String body) throws SQLException {
        PreparedStatement statement = this.prepareStatement("insert into question values (?,?,?,?,?,?,?,?,?,?,?,?);");
        statement.setInt(1, question_id);
        statement.setInt(2, score);
        statement.setString(3, link);
        statement.setInt(4, answer_count);
        statement.setInt(5, view_count);
        statement.setString(6, content_license);
        statement.setString(7, title);
        statement.setTimestamp(8, last_activity_date);
        statement.setTimestamp(9, last_edit_date);
        statement.setTimestamp(10, creation_date);
        statement.setInt(11, account_id);
        statement.setString(12, body);
        statement.executeUpdate();
    }

    public void insertTag(String tag_name, int question_id) throws SQLException {
        PreparedStatement statement = this.prepareStatement("insert into tag values (?,?,?)");
        statement.setInt(1, tag_id++);
        statement.setString(2, tag_name);
        statement.setInt(3, question_id);
        statement.executeUpdate();
    }

    public void insertAnswer(int answer_id, Timestamp last_activity_date, Timestamp last_edit_date, Timestamp creation_date, int score, boolean is_accepted, String content_license, int question_id, String body, int account_id) throws SQLException {
        PreparedStatement statement = this.prepareStatement("insert into answer values (?,?,?,?,?,?,?,?,?,?)");
        statement.setInt(1, answer_id);
        statement.setTimestamp(2, last_activity_date);
        statement.setTimestamp(3, last_edit_date);
        statement.setTimestamp(4, creation_date);
        statement.setInt(5, score);
        statement.setBoolean(6, is_accepted);
        statement.setString(7, content_license);
        statement.setInt(8, question_id);
        statement.setString(9, body);
        statement.setInt(10, account_id);
        statement.executeUpdate();
    }

    public void insertOwner(int account_id, int user_id, String profile_image, String link, String user_type, String display_name, int reputation) throws SQLException {
        // 在Owner表中插入一条记录
        PreparedStatement statement = this.prepareStatement("select count(*) from owner where account_id = ?");
        statement.setInt(1, account_id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) > 0) {
            return;
        }
        statement = this.prepareStatement("insert into owner values (?,?,?,?,?,?,?)");
        statement.setInt(1, account_id);
        statement.setInt(2, user_id);
        statement.setString(3, profile_image);
        statement.setString(4, link);
        statement.setString(5, user_type);
        statement.setString(6, account_id == -1 ? "does_not_exist" : display_name);
        statement.setInt(7, reputation);
        statement.executeUpdate();
    }

    public void insertComment(int comment_id, boolean edited, int post_id, String body, Timestamp creation_date, int score, String content_license, int account_id) throws SQLException {
        // 在Comment表中插入一条记录
        PreparedStatement statement = this.prepareStatement("insert into comment values (?,?,?,?,?,?,?,?)");
        statement.setInt(1, comment_id);
        statement.setBoolean(2, edited);
        statement.setInt(3, post_id);
        statement.setString(4, body);
        statement.setTimestamp(5, creation_date);
        statement.setInt(6, score);
        statement.setString(7, content_license);
        statement.setInt(8, account_id);
        statement.execute();
    }

    public void insertConnectionTagAndQuestion(String tag_name, int question_id) throws SQLException {
        // 在ConnectionTagAndQuestion表中插入一条记录（为了多对多关系额外创建的数据表，以符合第三范式）
        PreparedStatement statement = this.prepareStatement("insert into connection_tag_and_question values (?,?)");
        statement.setString(1, tag_name);
        statement.setInt(2, question_id);
        statement.executeUpdate();
    }

    private Timestamp convertDate(Integer date) {
        // 将Unix时间戳转换为Timestamp
        if (date == null) {
            return null;
        }
        return new Timestamp(date * 1000L);
    }

    public void insertQuestionRecord(JSONObject questionJSON) throws SQLException {
        JSONObject ownerJson = questionJSON.getJSONObject("owner");
        insertQuestion(
                questionJSON.getInteger("question_id"),
                convertDate(questionJSON.getInteger("last_activity_date")),
                convertDate(questionJSON.getInteger("last_edit_date")),
                convertDate(questionJSON.getInteger("creation_date")),
                questionJSON.getInteger("score"),
                questionJSON.getString("link"),
                questionJSON.getInteger("answer_count"),
                questionJSON.getInteger("view_count"),
                questionJSON.getString("content_license"),
                questionJSON.getString("title"),
                ownerJson.getInteger("account_id") == null ? -1 : ownerJson.getInteger("account_id"),
                questionJSON.getString("body")
        );
        for (Object tag : questionJSON.getJSONArray("tags")) {
            insertTag((String) tag, questionJSON.getInteger("question_id"));
        }
    }

    public void insertAnswerRecord(JSONObject answerJSON) throws SQLException {
        JSONObject ownerJson = answerJSON.getJSONObject("owner");
        insertAnswer(
                answerJSON.getInteger("answer_id"),
                convertDate(answerJSON.getInteger("last_activity_date")),
                convertDate(answerJSON.getInteger("last_edit_date")),
                convertDate(answerJSON.getInteger("creation_date")),
                answerJSON.getInteger("score"),
                answerJSON.getBoolean("is_accepted"),
                answerJSON.getString("content_license"),
                answerJSON.getInteger("question_id"),
                answerJSON.getString("body"),
                ownerJson.getInteger("account_id") == null ? -1 : ownerJson.getInteger("account_id")
        );
    }

    public void insertCommentRecord(JSONObject commentJSON) throws SQLException {
        JSONObject ownerJson = commentJSON.getJSONObject("owner");
        insertComment(
                commentJSON.getInteger("comment_id"),
                commentJSON.getBoolean("edited"),
                commentJSON.getInteger("post_id"),
                commentJSON.getString("body"),
                convertDate(commentJSON.getInteger("creation_date")),
                commentJSON.getInteger("score"),
                commentJSON.getString("content_license"),
                ownerJson.getInteger("account_id") == null ? -1 : ownerJson.getInteger("account_id")
        );
    }

}

