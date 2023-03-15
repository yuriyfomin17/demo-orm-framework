package org.nimofy;

import org.nimofy.model.Product;
import org.nimofy.session.Session;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = initializeDataSource();
        Session session = new Session(dataSource);

        Product product = session.findById(Product.class, 80L);
        System.out.println(product);
    }

    private static DataSource initializeDataSource(){
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        return pgSimpleDataSource;
    }
}