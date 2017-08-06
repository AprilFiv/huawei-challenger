package com.cacheserverdeploy.deploy;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Deploy 
{
  
	
	public static   int totalPoint;
	public static   int totalLine;
	public static   int totalServicePoint;
	public static   int spend;
	public static   int step=0;
	public static   List<Line> line = new ArrayList<Line>();
	public static   List<Line> linetemp = new ArrayList<Line>();
	public static   String[] outStrings = new String[9999];
	public static   List<List<Integer>> route = new ArrayList<List<Integer>>();
	public static   List<Integer> sl = new ArrayList<Integer>();//存放当前的服务器位置
	public static   List<int[][]> resultLine = new ArrayList<int[][]>();
	public static   List<int[]> resultbasic = new ArrayList<int[]>();
    public static String[] deployServer(String[] graphContent)
    {
    	
        /**do your work here**/	
    	long current = System.currentTimeMillis();
    	int result = 99999999;
    	getDate(graphContent);//字符串数组转成int,保存在List<Line>
		if (totalLine<2500){
    		Calculate2(result,sl);
			route.clear();
			line.clear();
			line.addAll(linetemp);
			
			if(totalLine>1000){
    		for (int i=0;i<20;i++){
    			sl.remove(0);
    		}
			 int temp = Calculate(result, sl);//计算得到所有服务器为消费节点的初始解
			}
			if (totalLine<1000){
				for (int i=0;i<10;i++){
    			sl.remove(0);
				}
    		boolean flag = true;
    		while (flag)
    		{
    			flag = false; 
    			 int temp = Calculate(result, sl);//计算得到所有服务器为消费节点的初始解
        		System.out.println(result);
        		if (result>temp){
        			result = temp;
        			sl.remove(0);
        			flag=true;
        		}
				if ((System.currentTimeMillis()-current)/1000>60){
					flag=false;
				}
    			
    		}
				}
			
			int[][] resultroute=resultLine.get(0);
			int []  basic = resultbasic.get(0);
			outStrings = getOut(resultroute,line.size(),line.size() + totalPoint - sl.size(),basic,sl);
			return outStrings;
		}
    	else{
		
	
	int temp = Calculate2(result, sl);
    		
			route.clear();
			line.clear();
			int cc=0;
			line.addAll(linetemp);
			for (int i=0;i<20;i++){
    			sl.remove(0);
    		}
			for (int i =0;i<line.size()-totalServicePoint;i++){
				if (sl.indexOf(line.get(i).start)==-1 && sl.indexOf(line.get(i).end)==-1 && line.get(i).cost==10){
					line.remove(i);
					i--;
					System.out.println(cc++);
				}
			}
			  temp = Calculate(result, sl);//计算得到所有服务器为消费节点的初始解
		System.out.print(result);
		outStrings = getOut2(route,sl);
		return outStrings;
    	}
	}
	/*
	 * 获得输入String[]中的数据，放在List<Line>内
	 */
	public static void getDate(String[] graphContent) {
		line.clear();
		StringBuffer sb = new StringBuffer();
    	for(int i = 0; i <graphContent.length; i++){
    	 sb. append(" "+graphContent[i]);
    	}
       	String s = sb.toString();
       	Scanner sc =  new Scanner(s);
       	totalPoint = sc.nextInt();
		totalLine = sc.nextInt();
		totalServicePoint = sc.nextInt();
		spend = sc.nextInt();
		
		for (int i =0;i<totalLine;i++){
			int tempInt1 = sc.nextInt();
			int tempInt2 = sc.nextInt();
			int tempInt3 = sc.nextInt();
			int tempint4 = sc.nextInt();
			Line e1 = new Line(tempInt1,tempInt2, tempInt3, tempint4,tempInt3);
			line.add(e1); 
			linetemp.add(e1);
			
			Line e2 = new Line(tempInt2,tempInt1, tempInt3, tempint4,tempInt3);
			linetemp.add(e2); 
			}
		
		for (int i =0;i<totalServicePoint;i++){
			int temp=sc.nextInt();
			int temp2=sc.nextInt();
			int temp3=sc.nextInt();
			Line e =new Line(temp2, temp+totalPoint, temp3,0,temp3);
			line.add(e);
			linetemp.add(e);
		}

	}

	public static int Calculate2(int result,List<Integer> sl){
		for (int i =line.size();i>totalLine;i--){
		List<Integer> e = new ArrayList<Integer>();
		e.add(line.get(i-1).start);
		e.add(line.get(i-1).end-totalPoint);
		e.add(line.get(i-1).broad);
		e.add(line.get(i-1).broad);
		route.add(e);
	}
		List<Integer> pointtosl = new ArrayList<Integer>();
	List<Integer> slneed = new ArrayList<Integer>();
	List<Integer> sltemp = new ArrayList<Integer>();
	List<Integer> chooseslcount = new ArrayList<Integer>();
	List<Integer> choosesl = new ArrayList<Integer>();
	sl.clear();
	for (int c =0;c<route.size();c++){
		sl.add(route.get(c).get(0)); 
	}
	//合并简单项
	for (int i =0;i<line.size()-totalServicePoint;i=i+1){
		int countli=0;
		int flagstart = sl.indexOf(line.get(i).start);
		int flagend = sl.indexOf(line.get(i).end);
		int cost1=9999;
		int cost2=9999;
		int cost;
		if ((flagstart!=-1) && (flagend!=-1) ){
			if (route.get(flagstart).get(0).equals(line.get(i).start))
				if (line.get(i).broadReserve>=route.get(flagstart).get(route.get(flagstart).size()-1))
					cost1= route.get(flagstart).get(route.get(flagstart).size()-1)*line.get(i).cost;
			if (route.get(flagend).get(0).equals(line.get(i).end))
				if (line.get(i).broadReserve>=route.get(flagend).get(route.get(flagend).size()-1))
					cost2= route.get(flagend).get(route.get(flagend).size()-1)*line.get(i).cost;
		}
		if(cost1!=9999 || cost2!=9999){
			cost = cost1>=cost2?cost2:cost1;
			if (cost<spend){
				if (cost==cost1){
					route.get(flagstart).add(0,line.get(i).end);
					int cc = route.get(flagstart).get(route.get(flagstart).size()-1);
					int dd = route.get(flagend).get(route.get(flagend).size()-1);
					sl.set(flagstart,999999);
					if (route.get(flagstart).get(route.get(flagstart).size()-1) >route.get(flagstart).get(route.get(flagstart).size()-2) ){
						for (int q=0;q<route.size();q++){
							if (route.get(q).get(0).equals(route.get(flagstart).get(1)) && q!=flagstart){
								route.get(q).add(0,line.get(i).end);
								
							}
						}
					}
					for (int u =0;u<route.size();u++){ 
					if (route.get(flagend).get(0).equals(route.get(u).get(0))){
					   route.get(u).set(route.get(u).size()-1, cc+dd);	
					}
					}
					
					line.remove(i);
					i--;
					countli++;
				}
				else if (cost==cost2){
					route.get(flagend).add(0,line.get(i).start);
					int cc = route.get(flagstart).get(route.get(flagstart).size()-1);
					int dd = route.get(flagend).get(route.get(flagend).size()-1);
					sl.set(flagend,999999);
					if (route.get(flagend).get(route.get(flagend).size()-1) >route.get(flagend).get(route.get(flagend).size()-2) ){
						for (int q=0;q<route.size();q++){
							if (route.get(q).get(0).equals(route.get(flagend).get(1))){
								route.get(q).add(0,line.get(i).start);
								
							}
						}
					}
					for (int u =0;u<route.size();u++){ 
						if (route.get(flagstart).get(0).equals(route.get(u).get(0))){
						   route.get(u).set(route.get(u).size()-1, cc+dd);	
						}
					}
						
					line.remove(i);
					i--;
					countli++;
				}
			}
		}
	}
	//存放所有服务器的流量需
	slneed.clear();
	sltemp.clear();
	//2
	for (int i = route.size() - 1; i >0; i--) {
		for (int j = i - 1; j >=0; j--) {
			if (route.get(i).get(0) < route.get(j).get(0)) {
				List<Integer> e = new ArrayList<Integer>();
				e.addAll(route.get(i));
				List<Integer> e1 = route.set(j, e);
				route.set(i, e1);
			}
		}
	}
	sltemp.clear();
	for (int c =0;c<route.size();c++){
		if (sltemp.indexOf(route.get(c).get(0))==-1){
		sltemp.add(route.get(c).get(0));
		slneed.add(route.get(c).get(route.get(c).size()-1));
		}
	}
	boolean flag=true;
	while(flag){flag=false;
	for (int i = route.size() - 1; i >0; i--) {
		for (int j = i - 1; j >=0; j--) {
			if (route.get(i).get(0) < route.get(j).get(0)) {
				List<Integer> e = new ArrayList<Integer>();
				e.addAll(route.get(i));
				List<Integer> e1 = route.set(j, e);
				route.set(i, e1);
			}
		}
	}
	sl.clear();
	for (int c =0;c<route.size();c++){
		sl.add(route.get(c).get(0)); 
	}
	for (int j=0;j<route.size();j++){
		int cost1=0;
		List<Line> templ = new ArrayList<Line>();
		List<Integer> num = new ArrayList<Integer>();
		//done 寻找链路
		for (int i=0;i<line.size()-totalServicePoint;i++){
			int start = sl.indexOf(line.get(i).start);
			int end = sl.indexOf(line.get(i).end);
			if (start!=-1 && end !=-1){
				if (start==j || end == j){
					cost1+=line.get(i).broadReserve;
					templ.add(line.get(i));
					num.add(i);
				}	
			}
		}
		//done 链路按COST排序
		for (int i = templ.size() - 1; i > 0; i--) {
		for (int q = i - 1; q >= 0; q--) {
			if (templ.get(i).cost < templ.get(q).cost) {
				Line e = new Line(templ.get(q).start, templ.get(q).end,
						templ.get(q).broad, templ.get(q).cost,
						templ.get(q).broadReserve);
				Line e1 = templ.set(i, e);
				templ.set(q, e1);
				int rc = num.set(i,num.get(q) );
				num.set(q,rc);
				}
			}
		}
		int needflow = 0;
		needflow =  route.get(j).get(route.get(j).size()-1);
		//CHECK 问题多发地  
		if (cost1>=route.get(j).get(route.get(j).size()-1)){
			int cost2=0;
			for (int i=0;i<templ.size();i++){
				if (needflow>templ.get(i).broadReserve)
				{
					cost2+=templ.get(i).broadReserve*templ.get(i).cost;
				     needflow -=templ.get(i).broadReserve;
				}
				else{
					int temp= templ.size();
					cost2+=needflow*templ.get(i).cost;
					int cha = templ.get(i).broadReserve-needflow;
					if (cha !=0){
					Line e = new Line(templ.get(i).start, templ.get(i).end,
							cha, templ.get(i).cost,
							cha);
					line.add(e);
					}
					templ.get(i).broadReserve  = needflow;
					for (int o=i+1;o<temp;o++){
						templ.remove(i+1);
						num.remove(i+1);
					}
					break;
				}
			}
			if (cost2<spend){
				
			    int tempflow = route.get(j).get(route.get(j).size()-2);
			    int te = sltemp.indexOf(sl.get(j));
			    if (slneed.get(te) == tempflow){
			    	sltemp.remove(te);
			    	slneed.remove(te);}
			    else {slneed.set(te, slneed.get(te)-tempflow);}
			    sl.set(j,999999);
				for (int i =0;i<templ.size();i++){
					if (tempflow>templ.get(i).broadReserve){
						List<Integer> e= new ArrayList<Integer>();
						e.addAll(route.get(j));
						if (e.get(0)!=(templ.get(i).start)){
							e.add(0,templ.get(i).start);
							e.set(e.size()-2, templ.get(i).broadReserve);
							slneed.set(sltemp.indexOf(templ.get(i).start),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).start)));
						}
						else {
							e.add(0,templ.get(i).end);
							e.set(e.size()-2, templ.get(i).broadReserve);		
							slneed.set(sltemp.indexOf(templ.get(i).end),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).end)));
						}
						route.add(e);
						for (int w =0;w<sltemp.size();w++){
							for (int r =0;r<route.size();r++){
								if (route.get(r).get(0).equals(sltemp.get(w))){
									route.get(r).set(route.get(r).size()-1, slneed.get(w));
								}
							   
							}
						}
						tempflow-=templ.get(i).broadReserve;
						line.get(num.get(i)).start=-1;
						line.get(num.get(i)).end=-1;
					}
					else{
						if (route.get(j).get(0)!=templ.get(i).start){
							slneed.set(sltemp.indexOf(templ.get(i).start), tempflow+slneed.get(sltemp.indexOf(templ.get(i).start)));	
							route.get(j).add(0,templ.get(i).start);
							route.get(j).set(route.get(j).size()-2, tempflow);
						
							for (int w =0;w<sltemp.size();w++){
								for (int r =0;r<route.size();r++){
									if (route.get(r).get(0).equals(sltemp.get(w))){
										route.get(r).set(route.get(r).size()-1, slneed.get(w));
									}
								}
							}
							flag=true;
							if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
							else{
							line.get(num.get(i)).start=-1;
							line.get(num.get(i)).end=-1;
							}
							break;
							}
						else{
						
							slneed.set(sltemp.indexOf(templ.get(i).end),  tempflow+slneed.get(sltemp.indexOf(templ.get(i).end)));
							route.get(j).add(0,templ.get(i).end);
							route.get(j).set(route.get(j).size()-2, tempflow);
							
							for (int w =0;w<sltemp.size();w++){
								for (int r =0;r<route.size();r++){
									if (route.get(r).get(0).equals(sltemp.get(w))){
										route.get(r).set(route.get(r).size()-1, slneed.get(w));
									}
								}
							}
							flag=true;
							if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
							else{
							line.get(num.get(i)).start=-1;
							line.get(num.get(i)).end=-1;
							}
							break;
						}
					}
				}
			}
		}
	}
	}

	//清空无用line
	for (int i=0;i<line.size();i++){
		if (line.get(i).start==-1 && line.get(i).end==-1){
			line.remove(i);
			i--;
		}
	}
	
		for (int cishu=15;cishu>=5;cishu--){
			
	
		//当前服务器以及需求流量
		sl.clear();
		slneed.clear();
		sltemp.clear();
		for (int c =0;c<route.size();c++){
			if (sl.indexOf(route.get(c).get(0))==-1){
			sl.add(route.get(c).get(0));
			slneed.add(route.get(c).get(route.get(c).size()-1));
			}
		}
		for (int i=line.size()-1;i>line.size()-totalServicePoint;i--){
			sltemp.add(line.get(i).start);
		}
		//第三次，寻找与服务器直连的非服务器点，判断是否可以取代。
		pointtosl.clear();
		for (int i =0;i<totalPoint;i++){
			if (sltemp.indexOf(i)==-1){
				int countneed=0;
				int cost3=spend;
				pointtosl.clear();
				for (int j =0;j<line.size()-totalServicePoint;j++){
					if ((line.get(j).start == i && sl.indexOf(line.get(j).end)!=-1)){
						int temp = sl.indexOf(line.get(j).end);
						if (line.get(j).broadReserve>=slneed.get(temp)){
							cost3+=slneed.get(temp)*line.get(j).cost;
							pointtosl.add(line.get(j).end);
							countneed+=slneed.get(temp);
						}
					}
					else if (line.get(j).end == i && sl.indexOf(line.get(j).start)!=-1){
						int temp = sl.indexOf(line.get(j).start);
						if (line.get(j).broadReserve>=slneed.get(temp)){
							cost3+=slneed.get(temp)*line.get(j).cost;
							pointtosl.add(line.get(j).start);
							countneed+=slneed.get(temp);
						}
					}
					
				}
				if (cost3<spend*pointtosl.size()){
					
				   System.out.println(i);
					for (int h=0;h<pointtosl.size();h++){
						System.out.println(pointtosl.get(h));
					}
					for (int j =0;j<line.size()-totalServicePoint;j++){
						if ((line.get(j).start == i && sl.indexOf(line.get(j).end)!=-1)){
							int temp = sl.indexOf(line.get(j).end);
							if (line.get(j).broadReserve>=slneed.get(temp)){
								for (int u =0;u<route.size();u++){
									if (route.get(u).get(0).equals(line.get(j).end)){
										route.get(u).add(0,i);
										route.get(u).set(route.get(u).size()-1, countneed);
										
									}
								}
								line.remove(j);
								j--;
								sl.remove(temp);
								slneed.remove(temp);
							}
						}
						else if (line.get(j).end == i && sl.indexOf(line.get(j).start)!=-1){
							int temp = sl.indexOf(line.get(j).start);
							if (line.get(j).broadReserve>=slneed.get(temp)){
								for (int u =0;u<route.size();u++){
									if (route.get(u).get(0).equals(line.get(j).start)){
										route.get(u).add(0,i);
										route.get(u).set(route.get(u).size()-1, countneed);
									}
								}
								line.remove(j);
								j--;
								sl.remove(temp);
								slneed.remove(temp);
							}
						}
						
					}
					sl.add(i);
					slneed.add(countneed);
					i=0;
				}
			}
		}
		//第四轮 假设服务器
		sltemp.clear();
		for (int i=line.size()-1;i>line.size()-totalServicePoint;i--){
			sltemp.add(line.get(i).start);
		}
	
		sl.clear();
		for (int c =0;c<route.size();c++){
			sl.add(route.get(c).get(0)); 
		}
		
		chooseslcount.clear();
		choosesl.clear();
	
		for (int i =0;i<totalPoint;i++){
			if (sltemp.indexOf(i)==-1){
				pointtosl.clear();
			
				for (int j =0;j<line.size()-totalServicePoint;j++){
					if ((line.get(j).start == i && sl.indexOf(line.get(j).end)!=-1)){
					
							pointtosl.add(line.get(j).end);
						
					}
					else if (line.get(j).end == i && sl.indexOf(line.get(j).start)!=-1){
							pointtosl.add(line.get(j).start);
					}
				}
			
			if (pointtosl.size()>cishu){
			
				choosesl.add(i);
				chooseslcount.add(pointtosl.size());
			}
			}
		}
//		for (int i=0;i<choosesl.size()-1;i++){
//			for (int j =i+1;j<choosesl.size();j++){
//				if (chooseslcount.get(i)<chooseslcount.get(j)){
//				
//					 int temp = choosesl.set(i, choosesl.get(j));
//					 choosesl.set(j, temp);
//					 int temp1 = chooseslcount.set(i, chooseslcount.get(j));
//					 chooseslcount.set(j, temp1);
//				}
//			}
//		}
		for (int i =0;i<choosesl.size();i++){
			System.out.println(choosesl.get(i));
		}
		//假设服务器进ROUTE
		for (int i =0;i<choosesl.size();i++){
			List<Integer> e = new ArrayList<Integer>();
			e.add(choosesl.get(i));
			e.add(100000+i);
			e.add(100000);
			e.add(100000);
			route.add(e);
		}	
		if (choosesl.size()!=0){
		sl.clear();
		for (int c =0;c<route.size();c++){
			sl.add(route.get(c).get(0)); 
		}
		//合并简单项
		for (int i =0;i<line.size()-totalServicePoint;i=i+1){
			int countli=0;
			int flagstart = sl.indexOf(line.get(i).start);
			int flagend = sl.indexOf(line.get(i).end);
			int cost1=9999;
			int cost2=9999;
			int cost;
			if ((flagstart!=-1) && (flagend!=-1) ){
				if (route.get(flagstart).get(0).equals(line.get(i).start))
					if (line.get(i).broadReserve>=route.get(flagstart).get(route.get(flagstart).size()-1))
						cost1= route.get(flagstart).get(route.get(flagstart).size()-1)*line.get(i).cost;
				if (route.get(flagend).get(0).equals(line.get(i).end))
					if (line.get(i).broadReserve>=route.get(flagend).get(route.get(flagend).size()-1))
						cost2= route.get(flagend).get(route.get(flagend).size()-1)*line.get(i).cost;
			}
			if(cost1!=9999 || cost2!=9999){
				cost = cost1>=cost2?cost2:cost1;
				if (cost<spend){
					if (cost==cost1){
						route.get(flagstart).add(0,line.get(i).end);
						int cc = route.get(flagstart).get(route.get(flagstart).size()-1);
						int dd = route.get(flagend).get(route.get(flagend).size()-1);
						sl.set(flagstart,999999);
						if (route.get(flagstart).get(route.get(flagstart).size()-1) >route.get(flagstart).get(route.get(flagstart).size()-2) ){
							for (int q=0;q<route.size();q++){
								if (route.get(q).get(0).equals(route.get(flagstart).get(1)) && q!=flagstart){
									route.get(q).add(0,line.get(i).end);
									
								}
							}
						}
						for (int u =0;u<route.size();u++){ 
						if (route.get(flagend).get(0).equals(route.get(u).get(0))){
						   route.get(u).set(route.get(u).size()-1, cc+dd);	
						}
						}
						
						line.remove(i);
						i--;
						countli++;
					}
					else if (cost==cost2){
						route.get(flagend).add(0,line.get(i).start);
						int cc = route.get(flagstart).get(route.get(flagstart).size()-1);
						int dd = route.get(flagend).get(route.get(flagend).size()-1);
						sl.set(flagend,999999);
						if (route.get(flagend).get(route.get(flagend).size()-1) >route.get(flagend).get(route.get(flagend).size()-2) ){
							for (int q=0;q<route.size();q++){
								if (route.get(q).get(0).equals(route.get(flagend).get(1))){
									route.get(q).add(0,line.get(i).start);
									
								}
							}
						}
						for (int u =0;u<route.size();u++){ 
							if (route.get(flagstart).get(0).equals(route.get(u).get(0))){
							   route.get(u).set(route.get(u).size()-1, cc+dd);	
							}
						}
							
						line.remove(i);
						i--;
						countli++;
					}
				}
			}
		}
		
		
		
		slneed.clear();
		sltemp.clear();
		for (int c =0;c<route.size();c++){
			if (sltemp.indexOf(route.get(c).get(0))==-1){
			sltemp.add(route.get(c).get(0));
			slneed.add(route.get(c).get(route.get(c).size()-1));
			}
		}
		
		
		
		
		flag=true;
		while(flag){flag=false;
		for (int i = route.size() - 1; i >0; i--) {
			for (int j = i - 1; j >=0; j--) {
				if (route.get(i).get(0) < route.get(j).get(0)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer> e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		sl.clear();
		for (int c =0;c<route.size();c++){
			sl.add(route.get(c).get(0)); 
		}
		for (int j=0;j<route.size();j++){
		
			int cost1=0;
			List<Line> templ = new ArrayList<Line>();
			List<Integer> num = new ArrayList<Integer>();
			//done 寻找链路
			for (int i=0;i<line.size()-totalServicePoint;i++){
				int start = sl.indexOf(line.get(i).start);
				int end = sl.indexOf(line.get(i).end);
				if (start!=-1 && end !=-1){
					if (start==j || end == j){
						cost1+=line.get(i).broadReserve;
						templ.add(line.get(i));
						num.add(i);
					}
						
				}
			}
			//done 链路按COST排序
			for (int i = templ.size() - 1; i > 0; i--) {
			for (int q = i - 1; q >= 0; q--) {
				if (templ.get(i).cost < templ.get(q).cost) {
					Line e = new Line(templ.get(q).start, templ.get(q).end,
							templ.get(q).broad, templ.get(q).cost,
							templ.get(q).broadReserve);
					Line e1 = templ.set(i, e);
					templ.set(q, e1);
					int rc = num.set(i,num.get(q) );
					num.set(q,rc);
					}
				}
			}
			int needflow = 0;
			needflow =  route.get(j).get(route.get(j).size()-1);
			//CHECK 问题多发地  
			if (cost1>=route.get(j).get(route.get(j).size()-1)){
				int cost2=0;
				for (int i=0;i<templ.size();i++){
					if (needflow>templ.get(i).broadReserve)
					{
						cost2+=templ.get(i).broadReserve*templ.get(i).cost;
					     needflow -=templ.get(i).broadReserve;
					}
					else{
						int temp= templ.size();
						cost2+=needflow*templ.get(i).cost;
						int cha = templ.get(i).broadReserve-needflow;
						if (cha !=0){
						Line e = new Line(templ.get(i).start, templ.get(i).end,
								cha, templ.get(i).cost,
								cha);
						line.add(e);
						}
						templ.get(i).broadReserve  = needflow;
						for (int o=i+1;o<temp;o++){
							templ.remove(i+1);
							num.remove(i+1);
						}
						break;
					}
				}
				if (cost2<spend){
					
				    int tempflow = route.get(j).get(route.get(j).size()-2);
				    System.out.println(sl.get(j));
				    int te = sltemp.indexOf(sl.get(j));
				    if (slneed.get(te) == tempflow){
				    	sltemp.remove(te);
				    	slneed.remove(te);}
				    else {slneed.set(te, slneed.get(te)-tempflow);}
				    sl.set(j,999999);
					for (int i =0;i<templ.size();i++){
						if (tempflow>templ.get(i).broadReserve){
							List<Integer> e= new ArrayList<Integer>();
							e.addAll(route.get(j));
							if (e.get(0)!=(templ.get(i).start)){
								e.add(0,templ.get(i).start);
								e.set(e.size()-2, templ.get(i).broadReserve);
								slneed.set(sltemp.indexOf(templ.get(i).start),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).start)));
							}
							else {
								e.add(0,templ.get(i).end);
								e.set(e.size()-2, templ.get(i).broadReserve);		
								slneed.set(sltemp.indexOf(templ.get(i).end),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).end)));
							}
							route.add(e);
							for (int w =0;w<sltemp.size();w++){
								for (int r =0;r<route.size();r++){
									if (route.get(r).get(0).equals(sltemp.get(w))){
										route.get(r).set(route.get(r).size()-1, slneed.get(w));
									}
								   
								}
							}
							tempflow-=templ.get(i).broadReserve;
							line.get(num.get(i)).start=-1;
							line.get(num.get(i)).end=-1;
						}
						else{
							if (route.get(j).get(0)!=templ.get(i).start){
								slneed.set(sltemp.indexOf(templ.get(i).start), tempflow+slneed.get(sltemp.indexOf(templ.get(i).start)));	
								route.get(j).add(0,templ.get(i).start);
								route.get(j).set(route.get(j).size()-2, tempflow);
							
								for (int w =0;w<sltemp.size();w++){
									for (int r =0;r<route.size();r++){
										if (route.get(r).get(0).equals(sltemp.get(w))){
											route.get(r).set(route.get(r).size()-1, slneed.get(w));
										}
									}
								}
								flag=true;
								if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
								else{
								line.get(num.get(i)).start=-1;
								line.get(num.get(i)).end=-1;
								}
								break;
								}
							else{
							
								slneed.set(sltemp.indexOf(templ.get(i).end),  tempflow+slneed.get(sltemp.indexOf(templ.get(i).end)));
								route.get(j).add(0,templ.get(i).end);
								route.get(j).set(route.get(j).size()-2, tempflow);
								
								for (int w =0;w<sltemp.size();w++){
									for (int r =0;r<route.size();r++){
										if (route.get(r).get(0).equals(sltemp.get(w))){
											route.get(r).set(route.get(r).size()-1, slneed.get(w));
										}
									}
								}
								flag=true;
								if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
								else{
								line.get(num.get(i)).start=-1;
								line.get(num.get(i)).end=-1;
								}
								break;
							}
						}
					}
				
				}
			}
		}
		}
		//移除没有添加路线的服务器
		for (int i =0;i<route.size();i++){
			if (route.get(i).get(2)==100000){
				route.remove(i);
				i--;
			}
		}
		//重设添加路线服务器的需求流量
		for (int i =0;i<route.size();i++){
			if (route.get(i).get(route.get(i).size()-1)>10000){
				int temp =0;
				for (int j=0;;j++){
					if (route.get(i+j).get(route.get(i+j).size()-1)>10000){
						temp += route.get(i+j).get(route.get(i+j).size()-2);
						
					}
					else break;
				}
				for (int j=0;;j++){
					if (route.get(i+j).get(route.get(i+j).size()-1)>10000){
						route.get(i+j).set(route.get(i+j).size()-1, temp);
					
					}
					else break;
				}	
			}
		}
		//清空无用line
		for (int i=0;i<line.size();i++){
			if (line.get(i).start==-1 && line.get(i).end==-1){
				line.remove(i);
				i--;
				
			}
		}
		
		//4
		//存放所有服务器的流量需
		slneed.clear();
		sltemp.clear();
		//2
		for (int i = route.size() - 1; i >0; i--) {
			for (int j = i - 1; j >=0; j--) {
				if (route.get(i).get(0) < route.get(j).get(0)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer> e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		
		sltemp.clear();
		for (int c =0;c<route.size();c++){
			if (sltemp.indexOf(route.get(c).get(0))==-1){
			sltemp.add(route.get(c).get(0));
			slneed.add(route.get(c).get(route.get(c).size()-1));
			}
		}
		

		flag=true;
		while(flag){flag=false;
		for (int i = route.size() - 1; i >0; i--) {
			for (int j = i - 1; j >=0; j--) {
				if (route.get(i).get(0) < route.get(j).get(0)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer> e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		sl.clear();
		for (int c =0;c<route.size();c++){
			sl.add(route.get(c).get(0)); 
		}
		for (int j=0;j<route.size();j++){
		
			int cost1=0;
			List<Line> templ = new ArrayList<Line>();
			List<Integer> num = new ArrayList<Integer>();
			//done 寻找链路
			for (int i=0;i<line.size()-totalServicePoint;i++){
				int start = sl.indexOf(line.get(i).start);
				int end = sl.indexOf(line.get(i).end);
				if (start!=-1 && end !=-1){
					if (start==j || end == j){
						cost1+=line.get(i).broadReserve;
						templ.add(line.get(i));
						num.add(i);
					}
						
				}
			}
			//done 链路按COST排序
			for (int i = templ.size() - 1; i > 0; i--) {
			for (int q = i - 1; q >= 0; q--) {
				if (templ.get(i).cost < templ.get(q).cost) {
					Line e = new Line(templ.get(q).start, templ.get(q).end,
							templ.get(q).broad, templ.get(q).cost,
							templ.get(q).broadReserve);
					Line e1 = templ.set(i, e);
					templ.set(q, e1);
					int rc = num.set(i,num.get(q) );
					num.set(q,rc);
					}
				}
			}
			int needflow = 0;
			needflow =  route.get(j).get(route.get(j).size()-1);
			//CHECK 问题多发地  
			if (cost1>=route.get(j).get(route.get(j).size()-1)){
				int cost2=0;
				for (int i=0;i<templ.size();i++){
					if (needflow>templ.get(i).broadReserve)
					{
						cost2+=templ.get(i).broadReserve*templ.get(i).cost;
					     needflow -=templ.get(i).broadReserve;
					}
					else{
						int temp= templ.size();
						cost2+=needflow*templ.get(i).cost;
						int cha = templ.get(i).broadReserve-needflow;
						if (cha !=0){
						Line e = new Line(templ.get(i).start, templ.get(i).end,
								cha, templ.get(i).cost,
								cha);
						line.add(e);
						}
						templ.get(i).broadReserve  = needflow;
						for (int o=i+1;o<temp;o++){
							templ.remove(i+1);
							num.remove(i+1);
						}
						break;
					}
				}
				if (cost2<spend){
					
				    int tempflow = route.get(j).get(route.get(j).size()-2);
				    int te = sltemp.indexOf(sl.get(j));
				    if (slneed.get(te) == tempflow){
				    	sltemp.remove(te);
				    	slneed.remove(te);}
				    else {slneed.set(te, slneed.get(te)-tempflow);}
				    sl.set(j,999999);
					for (int i =0;i<templ.size();i++){
						if (tempflow>templ.get(i).broadReserve){
							List<Integer> e= new ArrayList<Integer>();
							e.addAll(route.get(j));
							if (e.get(0)!=(templ.get(i).start)){
								e.add(0,templ.get(i).start);
								e.set(e.size()-2, templ.get(i).broadReserve);
								slneed.set(sltemp.indexOf(templ.get(i).start),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).start)));
							}
							else {
								e.add(0,templ.get(i).end);
								e.set(e.size()-2, templ.get(i).broadReserve);		
								slneed.set(sltemp.indexOf(templ.get(i).end),  templ.get(i).broadReserve+slneed.get(sltemp.indexOf(templ.get(i).end)));
							}
							route.add(e);
							for (int w =0;w<sltemp.size();w++){
								for (int r =0;r<route.size();r++){
									if (route.get(r).get(0).equals(sltemp.get(w))){
										route.get(r).set(route.get(r).size()-1, slneed.get(w));
									}
								   
								}
							}
							tempflow-=templ.get(i).broadReserve;
							line.get(num.get(i)).start=-1;
							line.get(num.get(i)).end=-1;
						}
						else{
							if (route.get(j).get(0)!=templ.get(i).start){
								slneed.set(sltemp.indexOf(templ.get(i).start), tempflow+slneed.get(sltemp.indexOf(templ.get(i).start)));	
								route.get(j).add(0,templ.get(i).start);
								route.get(j).set(route.get(j).size()-2, tempflow);
							
								for (int w =0;w<sltemp.size();w++){
									for (int r =0;r<route.size();r++){
										if (route.get(r).get(0).equals(sltemp.get(w))){
											route.get(r).set(route.get(r).size()-1, slneed.get(w));
										}
									}
								}
								flag=true;
								if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
								else{
								line.get(num.get(i)).start=-1;
								line.get(num.get(i)).end=-1;
								}
								break;
								}
							else{
							
								slneed.set(sltemp.indexOf(templ.get(i).end),  tempflow+slneed.get(sltemp.indexOf(templ.get(i).end)));
								route.get(j).add(0,templ.get(i).end);
								route.get(j).set(route.get(j).size()-2, tempflow);
								
								for (int w =0;w<sltemp.size();w++){
									for (int r =0;r<route.size();r++){
										if (route.get(r).get(0).equals(sltemp.get(w))){
											route.get(r).set(route.get(r).size()-1, slneed.get(w));
										}
									}
								}
								flag=true;
								if (line.get(num.get(i)).broadReserve>tempflow) line.get(num.get(i)).broadReserve-=tempflow;
								else{
								line.get(num.get(i)).start=-1;
								line.get(num.get(i)).end=-1;
								}
								break;
							}
						}
					
						
					}
				
				}
			}
		}
		}

		//清空无用line
		for (int i=0;i<line.size();i++){
			if (line.get(i).start==-1 && line.get(i).end==-1){
				line.remove(i);
				i--;
				
			}
		}
		
		
		
		}
		}
		
		
		//5
		int countli=0;
		for (int i = route.size() - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (route.get(i).get(route.get(i).size()-1) < route.get(j).get(route.get(j).size()-1)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer>  e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		
		sl.clear();
		for (int c =0;c<route.size();c++){
			if (!Util.isOfSl(sl,route.get(c).get(0)))
			sl.add(route.get(c).get(0)); 
		}
		int dierlun=0;
	    for(int i=0;i<route.size()-dierlun;i++){
	    	
	    	sl.clear();
			for (int c =0;c<route.size();c++){
				if (!Util.isOfSl(sl,route.get(c).get(0)))
				sl.add(route.get(c).get(0)); 
			}
	    	int currentsl = route.get(i).get(0);
	    	int needflow = route.get(i).get(route.get(i).size()-1);
	    	int currentcost = 0;
	    	List<Integer> temp = new ArrayList<Integer>() ;
	    	temp.clear();
	    	temp.addAll(route.get(i));
	    	List<Integer> location = new ArrayList<Integer>() ;
	    	location.clear();
	    	
	    	for (int j =0;j<line.size()-totalServicePoint;j++){
	    		int current = temp.get(0);
	    		
	    		if (line.get(j).broadReserve>=needflow){
	    			if (line.get(j).start==current && !Util.isOfSl(location, j) ){
	    				location.add(j);
	    				currentcost+=line.get(j).cost*needflow;
	    				
	    				if (currentcost>=spend) break;
	    				if (Util.isOfSl(sl,line.get(j).end)  && line.get(j).end!=currentsl){
	    					line.get(j).broadReserve-=needflow;
	    					temp.add(0,line.get(j).end);
	    					int totalflow = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							totalflow=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
	    					route.remove(i);
	    					i--;
	    					countli++;
	    					for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
	    					System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							route.get(p).set(route.get(p).size()-1, totalflow);
	    						}
	    					}
	    					break;
	    				}
	    				else{
	    					temp.add(0,line.get(j).end);
	    					line.get(j).broadReserve-=needflow;
	    					j=0;
	    				}
	    			}
	    			else if (line.get(j).end==current && !Util.isOfSl(location, j)){
	    				location.add(j);
		    			currentcost+=line.get(j).cost*needflow;
		    			if (currentcost>=spend) break;
		    			if (Util.isOfSl(sl,line.get(j).start)  && line.get(j).start!=currentsl ){
		    				line.get(j).broadReserve-=needflow;
		    				temp.add(0,line.get(j).start);
		    				int totalflow2 = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							totalflow2=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
		    				route.remove(i);
		    				i--;
		    				countli++;
		    				for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
		    				System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							route.get(p).set(route.get(p).size()-1, totalflow2);
	    						}
	    					}
		    				break;
		    			}
		    			else{
		    				temp.add(0,line.get(j).start);
		    				line.get(j).broadReserve-=needflow;
		    				j=0;
		    			}
		    		}
	    		}
	    	}
	    }
		sl.clear();
		for (int c =0;c<route.size();c++){
			if (!Util.isOfSl(sl,route.get(c).get(0)))
			sl.add(route.get(c).get(0)); 
		}
		//第三轮
		for (int i = route.size() - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (route.get(i).get(route.get(i).size()-1) < route.get(j).get(route.get(j).size()-1)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer>  e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		sl.clear();
		for (int c =0;c<route.size();c++){
			if (!Util.isOfSl(sl,route.get(c).get(0)))
			sl.add(route.get(c).get(0)); 
		}
		int dierlun1=0;
	    for(int i=0;i<route.size()-dierlun1;i++){
	    	
	    	sl.clear();
			for (int c =0;c<route.size();c++){
				if (!Util.isOfSl(sl,route.get(c).get(0)))
				sl.add(route.get(c).get(0)); 
			}
	    	int currentsl = route.get(i).get(0);
	    	int needflow = route.get(i).get(route.get(i).size()-1);
	    	int currentcost = 0;
	    	List<Integer> temp = new ArrayList<Integer>() ;
	    	temp.clear();
	    	temp.addAll(route.get(i));
	    	List<Integer> location = new ArrayList<Integer>() ;
	    	location.clear();
	    	for (int j =0;j<line.size()-totalServicePoint;j++){
	    		int current = temp.get(0);
	    		
	    		if (line.get(j).broadReserve>=needflow){
	    	
	    			if (line.get(j).end==current && !Util.isOfSl(location, j)){
	    				location.add(j);
		    			currentcost+=line.get(j).cost*needflow;
		    			if (currentcost>=spend) break;
		    			if (Util.isOfSl(sl,line.get(j).start)  && line.get(j).start!=currentsl ){
		    				line.get(j).broadReserve-=needflow;
		    				temp.add(0,line.get(j).start);
		    				int totalflow2 = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							totalflow2=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
		    				route.remove(i);
		    				i--;
		    				countli++;
		    				for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
		    				System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun1++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							route.get(p).set(route.get(p).size()-1, totalflow2);
	    						}
	    					}
		    				break;
		    			}
		    			else{
		    				temp.add(0,line.get(j).start);
		    				line.get(j).broadReserve-=needflow;
		    				j=0;
		    			}
		    		}
	    			else if (line.get(j).start==current && !Util.isOfSl(location, j) ){
	    				location.add(j);
	    				currentcost+=line.get(j).cost*needflow;
	    				
	    				if (currentcost>=spend) break;
	    				if (Util.isOfSl(sl,line.get(j).end)  && line.get(j).end!=currentsl){
	    					line.get(j).broadReserve-=needflow;
	    					temp.add(0,line.get(j).end);
	    					int totalflow = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							totalflow=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
	    					route.remove(i);
	    					i--;
	    					countli++;
	    					for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
	    					System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun1++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							route.get(p).set(route.get(p).size()-1, totalflow);
	    						}
	    					}
	    					break;
	    				}
	    				else{
	    					temp.add(0,line.get(j).end);
	    					line.get(j).broadReserve-=needflow;
	    					j=0;
	    				}
	    			}
	    		}
	    	}
	    }
		
		sl.clear();
		for (int c =0;c<route.size();c++){
			if (!Util.isOfSl(sl,route.get(c).get(0)))
			sl.add(route.get(c).get(0)); 
		}
		//第三轮
		for (int i = route.size() - 1; i > 0; i--) {
			for (int j = i - 1; j >= 0; j--) {
				if (route.get(i).get(route.get(i).size()-1) < route.get(j).get(route.get(j).size()-1)) {
					List<Integer> e = new ArrayList<Integer>();
					e.addAll(route.get(i));
					List<Integer>  e1 = route.set(j, e);
					route.set(i, e1);
				}
			}
		}
		sl.clear();
		for (int c =0;c<route.size();c++){
			if (!Util.isOfSl(sl,route.get(c).get(0)))
			sl.add(route.get(c).get(0)); 
		}
		int dierlun2=0;
	    for(int i=0;i<route.size()-dierlun2;i++){
	    	
	    	sl.clear();
			for (int c =0;c<route.size();c++){
				if (!Util.isOfSl(sl,route.get(c).get(0)))
				sl.add(route.get(c).get(0)); 
			}
	    	int currentsl = route.get(i).get(0);
	    	int needflow = route.get(i).get(route.get(i).size()-1);
	    	int currentcost = 0;
	    	List<Integer> temp = new ArrayList<Integer>() ;
	    	temp.clear();
	    	temp.addAll(route.get(i));
	    	List<Integer> location = new ArrayList<Integer>() ;
	    	location.clear();
	    	for (int j =0;j<line.size()-totalServicePoint;j++){
	    		int current = temp.get(0);
	    		
	    		if (line.get(j).broadReserve>=needflow){
	    	
	    			if (line.get(j).end==current && !Util.isOfSl(location, j)){
	    				location.add(j);
		    			currentcost+=line.get(j).cost*needflow;
		    			if (currentcost>=spend) break;
		    			if (Util.isOfSl(sl,line.get(j).start)  && line.get(j).start!=currentsl ){
		    				line.get(j).broadReserve-=needflow;
		    				temp.add(0,line.get(j).start);
		    				int totalflow2 = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							totalflow2=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
		    				route.remove(i);
		    				i--;
		    				countli++;
		    				for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
		    				System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun2++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).start){
	    							route.get(p).set(route.get(p).size()-1, totalflow2);
	    						}
	    					}
		    				break;
		    			}
		    			else{
		    				temp.add(0,line.get(j).start);
		    				line.get(j).broadReserve-=needflow;
		    				j=0;
		    			}
		    		}
	    			else if (line.get(j).start==current && !Util.isOfSl(location, j) ){
	    				location.add(j);
	    				currentcost+=line.get(j).cost*needflow;
	    				
	    				if (currentcost>=spend) break;
	    				if (Util.isOfSl(sl,line.get(j).end)  && line.get(j).end!=currentsl){
	    					line.get(j).broadReserve-=needflow;
	    					temp.add(0,line.get(j).end);
	    					int totalflow = 0;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							totalflow=route.get(p).get(route.get(p).size()-1)+needflow;
	    							break;
	    						}
	    					}
	    					route.remove(i);
	    					i--;
	    					countli++;
	    					for (int q=0;q<temp.size()-4;q++){
	    						for (int w=q+1;w<=temp.size()-4;w++){
	    							if (temp.get(q)==temp.get(w)){
	    								for(int e=0;e<w-q;e++){
	    									temp.remove(q);
	    								}
	    								
	    							}
	    						}
	    					}
	    					System.out.println(currentcost);
	    					route.add(temp);
	    					sl.remove(sl.indexOf(currentsl));
	    					dierlun1++;
	    					for (int p =0;p<route.size();p++){
	    						if (route.get(p).get(0)==line.get(j).end){
	    							route.get(p).set(route.get(p).size()-1, totalflow);
	    						}
	    					}
	    					break;
	    				}
	    				else{
	    					temp.add(0,line.get(j).end);
	    					line.get(j).broadReserve-=needflow;
	    					j=0;
	    				}
	    			}
	    		}
	    	}
	    }
		
		sl.clear();
		slneed.clear();
		sltemp.clear();
		for (int c =0;c<route.size();c++){
			if (sl.indexOf(route.get(c).get(0))==-1){
			sl.add(route.get(c).get(0));
			slneed.add(route.get(c).get(route.get(c).size()-1));
			}
		}
		int result2 = sl.size()*spend;
		return result2;
	}
	public static String[] getOut2(List<List<Integer>> route ,List<Integer> sl){
		for (int k=0;k<route.size();k++){
			System.out.println();
			StringBuilder sb = new StringBuilder();
			sb.setLength(0);
			for (int h=0;h<route.get(k).size()-1;h++){
				System.out.print(route.get(k).get(h)+" ");
				sb.append(String.valueOf(route.get(k).get(h)));
				sb.append(" ");
				outStrings[k+2]=sb.toString();
			}
			
		}
		outStrings[0]=String.valueOf(route.size());
		outStrings[1]="\r\n";
		return outStrings;
	}
	
	public static int Calculate(int result,List<Integer> sl){
		simplexalgorithm tosolve = null;
		List<Integer> rest = null;
		int amountlimit;
		int amountx;
		int amountless;
		int amountequal;
		int a[][];
		int x[];
		rest = new ArrayList<Integer>();//得到没有服务器的网络节点REST
		for (int i = 0; i < totalPoint; i++) {
			if (Util.isOfSl(sl, i)==false){
				rest.add(i);
			}

		}
		amountlimit = line.size() + totalPoint - sl.size();
		amountx = line.size();
		amountless = line.size() - totalServicePoint;
		amountequal = totalServicePoint + totalPoint - sl.size();
		a = new int[amountlimit][amountx + 1];
		x = new int[amountx];
		for (int i = 0; i < amountx; i++) {
			for (int j = 0; j < amountx + 1; j++) {
				if (i == j) {
					a[i][j] = 1;
				}
				if (j == amountx) {
					a[i][j] = line.get(i).broad;
				}
			}
			x[i] = line.get(i).cost;
		}
		for (int l = 0; l < rest.size(); l++) {
			for (int j = 0; j < line.size(); j++) {
				if (line.get(j).start == rest.get(l)) {
					a[l + amountx][j] = 1;
				} else if (line.get(j).end == rest.get(l)) {
					a[l + amountx][j] = -1;
				} else {
					a[l + amountx][j] = 0;
				}
			}
		}
		tosolve = new simplexalgorithm(-1, amountlimit, amountx, amountless,amountequal, a, x);
		int result2 = tosolve.solve(sl);
		int [][] resultline = tosolve.getA();
		int [] basic = tosolve.getBasic();
		resultLine.add(0, resultline);
		resultbasic.add(0,basic);
		result2 = result2 + spend * sl.size();
		return result2;
	}
	
	
	
	public static String[] getOut(int [][] a,int n,int m,int[] basic,List<Integer> sl){
		int basicp[] = new int[n+1];
		List<List<Integer>> listtemp = new ArrayList<List<Integer>>();
		String[] resultStrings = new String[1000];
		for(int i = 0; i <= n; i++)
		{
			basicp[i] = 0;
		}
		for(int i = 1; i <= m; i++)
		{
			if(basic[i] >= 1 && basic[i] <= n)
			{
				basicp[basic[i]] = i;
			}
		}
		
		for (int j =0;j<totalServicePoint;j++){
			for(int i = 1;i<=n; i++)
			{
			
				if(basicp[i] != 0)
				{
					if (a[basicp[i]][0]!=0){
						
						if (line.get(i-1).end==totalPoint+j){
							List<Integer> e = new ArrayList<Integer>();
							e.add(0,line.get(i-1).broad);
							e.add(0,line.get(i-1).end-totalPoint);
							e.add(0,line.get(i-1).start);
							listtemp.add(e);
							a[basicp[i]][0]=0;
						}
					
					}
				}	
			}
			}
			int temp2=0;
			for (;;){
			boolean flaggg =true;
			for (int j = 0; j < listtemp.size(); j++) {
				for (int i = 0;i<n; i++) {
					if (basicp[i + 1] != 0) {
						if (a[basicp[i + 1]][0] != 0) {
							if (line.get(i).end == listtemp.get(j).get(0) && Util.isOfSl(sl,listtemp.get(j).get(0))==false) {
						//		System.out.println(listtemp.get(j).get(listtemp.get(j).size()-1));
						//		System.out.println(a[basicp[i + 1]][0]);
						//		System.out.println(i+1);
								temp2 = listtemp.get(j).get(listtemp.get(j).size()-1)-a[basicp[i + 1]][0];
						//		System.out.println(temp2);
								if (temp2==0){
									listtemp.get(j).add(0, line.get(i).start);
									a[basicp[i + 1]][0] = 0;
								}
								else if (temp2>0){
									List<Integer> e = new ArrayList<Integer>();
									for (int v =0;v<listtemp.get(j).size();v++){
										e.add(0,listtemp.get(j).get(listtemp.get(j).size()-1-v));
									}
										e.set(e.size()-1, temp2);
										listtemp.add(e);
										listtemp.get(j).add(0, line.get(i).start);
										listtemp.get(j).set(listtemp.get(j).size()-1,a[basicp[i + 1]][0] );
										a[basicp[i + 1]][0] = 0;
									
								}
								else{
									listtemp.get(j).add(0, line.get(i).start);
									a[basicp[i+1]][0] = -temp2;
								}
							}
						}
					}
				}

			}
			for (int q=0;q<listtemp.size();q++){
				if (!Util.isOfSl(sl,listtemp.get(q).get(0))){
					flaggg=false;
			
				}
			}
			if (flaggg == true) 	{
				
				for (int k=0;k<listtemp.size();k++){
					System.out.println();
					StringBuilder sb = new StringBuilder();
					sb.setLength(0);
					for (int h=0;h<listtemp.get(k).size();h++){
						System.out.print(listtemp.get(k).get(h)+" ");
						sb.append(String.valueOf(listtemp.get(k).get(h)));
						sb.append(" ");
						resultStrings[k+2]=sb.toString();
					}
					
				}
				resultStrings[0]=String.valueOf(listtemp.size());
				
				resultStrings[1]="\r\n";
				return resultStrings;
			}
			}
		
		
	
		
	}
	
}
class Line{
    int start;
	int end;
	int broad;
	int cost;
	int broadReserve;
	public Line(int start, int end, int broad, int cost,int broadreserve) {
		
		this.start = start;
		this.end = end;
		this.broad = broad;
		this.cost = cost;
		this.broadReserve=broadreserve;
	}
}
class Util{
	public static boolean isOfSl(List<Integer> sl,int test){
		for (int i =0;i<sl.size();i++){
			if (test==sl.get(i)){
				return true ;
			}
		}
		return false;
	}
	public static boolean isZero(int[][] arr,int n){
		for (int i =1;i<=n;i++){
			if (arr[i][0]!=0) return false;
		}
		return true;
	}
}
class simplexalgorithm {
	int amountlimit;  //约束条件的个数
	int amountx;  //变量个数
	int amountless; //<=的约束条件个数
	int amountequal;
	int judge; //判断是否是错误的
	int basic[];
	int nonbasic[];
	int a[][]; //约束条件的系数矩阵
	int mininum; //目标函数的最大值或最小值
	
	/**
	 * 
	 * @param mininum -目标
	 * @param m -约束条件的个数
	 * @param n -变量个数
	 * @param amountless -<=的约束条件个数
	 * @param amountequal -=的约束条件个数
	 * @param a -约束条件的系数矩阵
	 * @param x -目标函数的价值系数
	 */
	public simplexalgorithm(int mininum,int amountlimit,int amountx,int amountless,int amountequal,int a[][],int x[])//构造函数
	{
		int value;
		this.judge = 0;
		this.mininum = mininum;
		this.amountlimit = amountlimit;
		this.amountx = amountx;
		this.amountless = amountless;
		this.amountequal = amountequal;
		this.a = new int[amountlimit+2][];
		for(int i = 0; i < amountlimit+2; i++)
		{
			this.a[i] = new int[amountx+amountlimit+(amountlimit-amountless-amountequal)+1];
		}
		this.basic = new int[amountlimit+2];
		this.nonbasic = new int[amountx+(amountlimit-amountless-amountequal)+1];
	
		for(int j = 0; j <= amountx+(amountlimit-amountless-amountequal); j++)
		{
			nonbasic[j] = j;
		}
		for(int i = 1,j = amountx+(amountlimit-amountless-amountequal)+1; i <= amountlimit; i++,j++)
		{
			basic[i]=j;
		}
		//约束系数和右端项
		for(int i = 1; i <= amountlimit; i++)
		{
			for(int j = 1; j <= amountx; j++)
			{
				value = a[i-1][j-1];
				this.a[i][j]= value;
			}
			value = a[i-1][amountx];
			if(value<0)
			{
				judge = 1;
			}
			this.a[i][0]=value;
		}
		//输入目标函数系数
		for(int j = 1; j <= amountx; j++) 
		{
			value = x[j - 1];
			this.a[0][j] = value * mininum;
		}
		 //引入人工变量，
		for(int j = 1; j <= amountx; j++)
		{
			value = 0;
			for(int i = amountless+1; i <= amountlimit; i++)
				value+=this.a[i][j];
			this.a[amountlimit+1][j]=value;
		}
		
	}
	//选取换入变量
	public int enter(int objrow)
	{
		int col = 0;
		for(int j = 1; j <= this.amountx + (amountlimit-amountless-amountequal); j++)
		{
			if(this.nonbasic[j] <= this.amountx + this.amountless + (amountlimit-amountless-amountequal) && this.a[objrow][j] > 10e-8)
			{
				col=j;
				break;
			}
		}
		return col;
	}
	//选取换出变量
	public int leave(int col)
	{
		double temp=-1;
		int row  = 0;
		for(int i = 1; i <= this.amountlimit; i++)
		{
			double val = this.a[i][col];
			if( val > 10e-8)
			{
				val = this.a[i][0]/val;
				if(val < temp || temp == -1)
				{
					row = i;
					temp = val;
				}
			}
		}
		return row;
	}
	//交换换入换出变量
	public void swapbasic(int row,int col)
	{
		int temp = this.basic[row];
		this.basic[row] = this.nonbasic[col];
		this.nonbasic[col] = temp;
	}
	//a[row][col]为轴心，变换单纯形表
	public void trans(int row,int col)
	{
		for(int j = 0;j <= this.amountx + (amountlimit-amountless-amountequal); j++)
		{
			if(j != col)
			{
				this.a[row][j] = this.a[row][j] / this.a[row][col];
			}
		}
		this.a[row][col] = 1 / this.a[row][col];
		for(int i = 0; i <= this.amountlimit + 1; i++)
		{
			if(i != row)
			{
				for(int j = 0; j <= this.amountx + (amountlimit-amountless-amountequal); j++)
				{
					if(j != col)
					{
						this.a[i][j] = this.a[i][j] - this.a[i][col] * this.a[row][j];
						if(Math.abs(this.a[i][j]) < 10e-8)
							this.a[i][j]=0;
					}
				}
				this.a[i][col] = -this.a[i][col] * this.a[row][col];
			}
		}
		swapbasic(row,col);
	}
	//执行算法
	public int simplex(int objrow)
	{
		int row = 0;
		while(true)
		{
			int col = enter(objrow);
			if(col > 0)
			{
				row=leave(col);
			}
			else
			{
				return 0;
			}
			if(row > 0)
			{
				trans(row,col);
			}
			else
			{
				return 2;
			}
		}
	}
	//构造可行解
	public int createbase()
	{
		this.judge = simplex(this.amountlimit + 1);
		if(this.judge > 0)
		{
			return this.judge;
		}
		for(int i = 1; i <= this.amountlimit; i++)
		{
			if(this.basic[i] > this.amountx + this.amountless + (amountlimit-amountless-amountequal))
			{
				if(this.a[i][0] > 10e-8)
				{
					return 3;
				}
				for(int j = 1; j <= this.amountx; j++)
				{
					if(Math.abs(this.a[i][j]) >= 10e-8)
					{
						trans(i,j);
						break;
					}
				}
			}
		}
		return 0;
	}
	//二阶段单纯形法的计算
	public int solve(List<Integer> sl)
	{
	
		if(this.judge > 0){}
		else if(this.amountlimit != this.amountless)
		{
			this.judge = createbase();
			if(this.judge > 0){}
			else {
				judge = simplex(0);
			}
		}
		int result ;
		if (judge==0){
			result = -mininum*a[0][0];
	
		}
		else {
			result =999999;
		}
		return result;
	}
	public int[][] getA(){
		return this.a;
	}
	public int[] getBasic(){
		return this.basic;
	}
	
	

}
