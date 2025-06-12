package org.slavik;


import org.slavik.OCS.GettingInformationByProductsCategories;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConnectionManager connectionManager = new ConnectionManager(
                "jdbc:mysql://localhost:3306/",
                "root",
                "221633"
        );
        connectionManager.connection();
//        Проблема с name(вместо названия показывает �������)
//        GetInformationByCategory gettingInformationAboutProductCategories =
//                new GetInformationByCategory(connectionManager.getConnection());
//        gettingInformationAboutProductCategories.receiving();

//        Опять проблемы со знаками �������
//        GettingInformationByProductCategory gettingInformationAboutTheStatusOfTheWarehouseAndPricesByProductCategories =
//                new GettingInformationByProductCategory(connectionManager.getConnection());
//        gettingInformationAboutTheStatusOfTheWarehouseAndPricesByProductCategories.receiving("V07");

//        Не получается написать правильно запрос в виде ["2000066229"]
        GettingInformationByProductsCategories gettingInformationByProductsCategories =
                new GettingInformationByProductsCategories(connectionManager.getConnection());



        Thread.sleep(25000);
    }
}