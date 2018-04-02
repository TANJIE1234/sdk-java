//package com.elasticcloudservice.predict.arima;
//
//import com.elasticcloudservice.predict.DataUtil;
//import com.filetool.util.FileUtil;
//
//import java.io.*;
//import java.lang.reflect.Array;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main
//{
//	public static void main(String args[])
//	{
//		List<int[]> resultList = new ArrayList<>();
//		int days = 7;
//		for (int i=0;i<16;i++) {
//			resultList.add(new int[days+1]);
//		}
//
//		ArrayList<double[]> dataList = new ArrayList<>();
//		dataList.add(new double[]{});
//		for (int i=1;i<16;i++) {
//			dataList.add(DataUtil.getFlavorArrayFromDataList(i,DataUtil.loadDataFromStringArray(FileUtil.
//					read("/home/tanjie/IdeaProjects/sdk-java/code/ecs/src/data/TrainData.txt",null))));
//		}
//
//		for (int i=0;i<days;i++) {
//			for (int j=1;j<16;j++) {
//				ARIMAModel arima = new ARIMAModel(dataList.get(j));
//				ArrayList<int []> list = new ArrayList<>();
//				int period = 7;
//				int modelCnt = 1, cnt = 0;			//?????????????????????
//				int [] tmpPredict = new int [modelCnt];
//				for (int k = 0; k < modelCnt; ++k)			//???????????????????????????????
//				{
//					int [] bestModel = arima.getARIMAModel(period, list, (k == 0) ? false : true);
//					if (bestModel.length == 0)
//					{
//						tmpPredict[k] = (int)dataList.get(j)[dataList.get(j).length - period];
//						cnt++;
//						break;
//					}
//					else
//					{
//						int predictDiff = arima.predictValue(bestModel[0], bestModel[1], period);
//						tmpPredict[k] = arima.aftDeal(predictDiff, period);
//						cnt++;
//					}
////				System.out.println("BestModel is " + bestModel[0] + " " + bestModel[1]);
////				list.add(bestModel);
//				}
//				double sumPredict = 0.0;
//				for (int k = 0; k < cnt; ++k)
//				{
//					sumPredict += (double)tmpPredict[k] / (double)cnt;
//				}
//				int predict = (int)Math.round(sumPredict);
//				if (predict<0)
//					predict = 0;
//				System.out.println("Predict value="+predict);
//				resultList.get(j)[i+1]=predict;
//				double[] temp = new double[dataList.get(j).length + 1];
//				System.arraycopy(dataList.get(j),0,temp,0,dataList.get(j).length);
//				temp[temp.length-1] = predict;
//				dataList.set(j, temp);
//			}
//		}
//		System.out.println("gdf ");
//	}
//
//}
