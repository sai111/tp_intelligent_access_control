package tpson.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuandong on 2017/6/29.
 * 计算每个阶段的平均值
 */
public class ReCountList {
    private int startTime;
    private int allowanceTime = 3600; //默认1小时
    private double sum=0;
    private int times=0;
    private boolean isContinue = true;
    private int arrayLength = 0;
    private double[][] array; //0time 1sum 2count

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

    public ReCountList(int startTime, int allowanceTime, boolean isContinue){
        this(startTime, allowanceTime);
        this.isContinue = isContinue;
    }

    public ReCountList(int startTime){
        this(startTime, 3600);
    }

    public ReCountList(int startTime,int allowanceTime){
        this.startTime = startTime;
        this.allowanceTime = allowanceTime;
        arrayLength = 3600 * 24 / allowanceTime;
        int time = 0;
        array = new double[arrayLength][4];

        for(int i=0;i<arrayLength;i++){
            array[i][0] = time;
            time += allowanceTime;
            array[i][1] = 0;
            array[i][2] = 0;
        }
    }

    public void add(int time,double value){
        time = time - startTime;
        for (int i=0;i<arrayLength;i++){
            if (time >= array[i][0] && time < array[i][0]+ allowanceTime){
                sum += value;
                times ++;
                array[i][1] = array[i][1] + value;
                array[i][2] ++;
                return;
            }
        }
    }

    public List<Double> getResultArray(){
        List<Double> result = new ArrayList<Double>();
        double lastValue = 0;
        for (int i=0;i<arrayLength;i++){
            if (array[i][2]==0){
                if (isContinue && i>0){
                    result.add(lastValue);
                }else{
                    result.add(0.0);
                }
            }else{
                lastValue = array[i][1] / array[i][2];
                result.add(lastValue);
            }
        }
        return result;
    }

    public String getResultStr(){
        return StringUtils.join(getResultArray(),",");
    }

    public static void main(String[] args) {
        ReCountList list = new ReCountList(11233,3600);
        list.add(11233,12);
        list.add(12233,10);
        list.add(13233,1);
        list.add(14233,14);
        list.add(18233,22);
        list.add(20233,12);
        list.add(22233,14);
        list.add(30233,99);

        System.out.println(list.getResultStr());
    }

}
