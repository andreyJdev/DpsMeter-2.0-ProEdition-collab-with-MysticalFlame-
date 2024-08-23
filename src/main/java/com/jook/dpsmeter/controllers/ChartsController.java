package com.jook.dpsmeter.controllers;

import com.jook.dpsmeter.logics.UserClass;
import javafx.scene.chart.PieChart;

import java.util.Map;

public class ChartsController {
    public PieChart pieChart;

    public void initialize() {
      /*  PieChart.Data slice1 = new PieChart.Data("Волш", 555);
        PieChart.Data slice2 = new PieChart.Data("Закл", 444);
        PieChart.Data slice3 = new PieChart.Data("Лук", 333);
        PieChart.Data slice4 = new PieChart.Data("Глад", 222);
        PieChart.Data slice5 = new PieChart.Data("Others", 111);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);
        pieChart.getData().add(slice4);
        pieChart.getData().add(slice5);
        */
    }


    public void setPieChart (Map<String, UserClass> classesDamage) {
        pieChart.getData().clear();
        for (Map.Entry<String, UserClass> entry: classesDamage.entrySet()) {
            if (entry.getValue().dmgSum!=0)
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue().dmgSum));
        }
    }
}
