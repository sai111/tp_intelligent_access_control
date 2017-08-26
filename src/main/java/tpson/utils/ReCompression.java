package tpson.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by liuandong on 2017/6/29.
 * 压缩一个数列
 */
public class ReCompression {
    private int startTime;
    private double allowanceValue = 0.01; //默认容差
    private double sum=0;
    private int times=0;
    private LinkedList<double[]> array; //0time 1sum 2count

    public ReCompression(int startTime,double allowanceValue){
        this.startTime = startTime;
        this.allowanceValue = allowanceValue;
        array = new LinkedList<double[]>();
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        if (times == 0){
            return 0;
        }else {
            return sum/times;
        }
    }

    public void add(int time,double value){
        time = time-startTime;
        sum += value;
        times ++;
        if(array.isEmpty()){
            array.addLast(new double[]{time,value});
        }else{
            if (allowanceValue <= Math.abs(array.getLast()[1] - value)){
                array.addLast(new double[]{time,value});
            }
        }
    }

    public String getResultStr(){
        return StringUtils.join(getResultArray(),"\n");
    }

    public ArrayList<String> getResultArray() {
        ArrayList<String> list = new ArrayList<String>();
        for (double[] a :array){
            list.add( ((int)a[0]) + ":" + a[1] );
        }
        return list;
    }

    public static void main(String[] args) {
        ReCompression list = new ReCompression(11233,0.01);
        list.add(11233,12);
        list.add(12233,10);
        list.add(12234,10.001);
        list.add(12234,10.002);
        list.add(13233,1);
        list.add(14233,14);
        list.add(18233,22);
        list.add(20233,12);
        list.add(22233,14);
        list.add(30233,1);
        System.out.println(list.getResultStr());
    }

}
