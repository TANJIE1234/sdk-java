package com.elasticcloudservice.predict;

import java.util.HashMap;
import java.util.Map;

public class Server{//·þÎñÆ÷
    public Map<String,Integer> VirtualList=new HashMap<String,Integer>();//ÓÃ¹þÏ£±íŽæŽ¢Ã¿žö·þÎñÆ÷Àïž÷¿îÐéÄâ»úÊýÁ¿£¬ÏÂ±êÊÇÐéÄâ»úÖÖÀà£¬ÖµÊÇÐéÄâ»úÊýÁ¿
    public int RemainCPU=56;
    public int RemainMemory=128;//µ¥Î»GB
    public Server(int ServerTypeCPU,int ServerTypeMemory){//¹¹Ôì·œ·š¡£ÕâÀïÔÝÇÒÏÈÈÏÎªCPUºÍÄÚŽæÊýÊÇÕâžö£¬ºóÃæÕýÊœÊ¹ÓÃÔÙ×öžÄ¶¯
        for(int i=0;i<15;i++)
            this.VirtualList.put("flavor"+(i+1), 0);
        RemainCPU=ServerTypeCPU;
        RemainMemory=ServerTypeMemory;
    }
}