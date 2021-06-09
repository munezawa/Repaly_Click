package com.company1.floating_window;

import org.litepal.crud.DataSupport;

import java.util.List;

public class allDates  extends DataSupport {
    public String name;
    public List<Integer> time;
    public List<Integer> x;
    public List<Integer> y;
    public allDates(String name,List<Integer> x,List<Integer> y,List<Integer> time){
        this.name=name;
        this.x=x;
        this.y=y;
        this.time=time;
    }
}
