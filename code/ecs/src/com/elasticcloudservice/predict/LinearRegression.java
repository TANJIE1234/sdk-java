package com.elasticcloudservice.predict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//linearRegression
class LinearRegression {
    private static final double BANDWIDTH = 100;
    private static final double INIT_THETA = 0.0;
    private double [][] trainData;//训练数据，一行一个数据，每一行最后一个数据为 y
    private int row;//训练数据  行数
    private int column;//训练数据 列数

    private double [] theta;//参数theta
    private double [] weight;//参数权重


    public double[][] getTrainData() {
        return trainData;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public double[] getTheta() {
        return theta;
    }

    private double alpha;//训练步长
    private int iteration;//迭代次数

    public LinearRegression(double[][] trainData, double alpha, int iteration) {
        int rowoffile = trainData.length;
        int columnoffile = trainData[0].length;
        this.trainData = trainData;
        this.row = rowoffile;
        this.column = columnoffile;
        this.alpha = alpha;
        this.iteration = iteration;
        theta = new double[column - 1];
        initialize_theta();

        weight = new double[column - 1];
        initialize_weight();
    }

    private void initialize_weight() {
        for (int i=0;i<weight.length;i++) {
            weight[i] = Math.pow(Math.E,-(Math.pow(weight.length-i,2)/BANDWIDTH));
        }
    }

    private void initialize_theta()//将theta各个参数全部初始化为1.0
    {
        for(int i=0;i<theta.length;i++)
            theta[i]=INIT_THETA;
    }

    public void trainTheta()
    {
        int iteration = this.iteration;
        while( (iteration--)>0 )
        {
            //对每个theta i 求 偏导数
            double [] partial_derivative = compute_partial_derivative();//偏导数
            //更新每个theta
            for(int i =0; i< theta.length;i++)
                theta[i]-= weight[i] * alpha * partial_derivative[i];
        }
    }

    private double [] compute_partial_derivative()
    {
        double [] partial_derivative = new double[theta.length];
        for(int j =0;j<theta.length;j++)//遍历，对每个theta求偏导数
        {
            partial_derivative[j]= compute_partial_derivative_for_theta(j);//对 theta i 求 偏导
        }
        return partial_derivative;
    }
    private double compute_partial_derivative_for_theta(int j)
    {
        double sum=0.0;
        for(int i=0;i<row;i++)//遍历 每一行数据
        {
            sum+=h_theta_x_i_minus_y_i_times_x_j_i(i,j);
        }
        return sum/row;
    }
    private double h_theta_x_i_minus_y_i_times_x_j_i(int i,int j)
    {
        double[] oneRow = getRow(i);//取一行数据，前面是feature，最后一个是y
        double result = 0.0;

        for(int k=0;k< (oneRow.length-1);k++)
            result+=theta[k]*oneRow[k];
        result-=oneRow[oneRow.length-1];
        result*=oneRow[j];
        return result;
    }
    private double [] getRow(int i)//从训练数据中取出第i行，i=0，1，2，。。。，（row-1）
    {
        return trainData[i];
    }


    private void loadTrainDataFromFile(String fileName,int row, int column)
    {
        for(int i=0;i< row;i++)//trainData的第一列全部置为1.0（feature x0）
            trainData[i][0]=1.0;

        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int counter = 0;
            while ( (counter<row) && (tempString = reader.readLine()) != null) {
                String [] tempData = tempString.split(" ");
                for(int i=0;i<column;i++)
                    trainData[counter][i+1]=Double.parseDouble(tempData[i]);
                counter++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}