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
	public static   String[] outStrings = new String[50000];
	public static   List<List<Integer>> route = new ArrayList<List<Integer>>();
	public static   List<Integer> sl = new ArrayList<Integer>();//存放当前的服务器位置
	public static   List<Integer> slsturron = new ArrayList<Integer>();//存放当前的服务器位置
    public static String[] deployServer(String[] graphContent)
    {
    	
        /**do your work here**/	
    	getDate(graphContent);//字符串数组转成int,保存在List<Line>
		long current = System.currentTimeMillis();
	
//		System.out.print(current);
		//设置每个服务器安装在消费节点为初始解
	
		int result = 99999999;
		int temp = Calculate(result, sl);
		if (temp<result)
			result = temp;
		System.out.print(result);
		outStrings = getOut(route,sl);
		return outStrings;
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
//			Line e2 = new Line(tempInt2,tempInt1, tempInt3, tempint4,tempInt3);
//			line.add(e2); 
		}
		for (int i =0;i<totalServicePoint;i++){
			int temp=sc.nextInt();
			int temp2=sc.nextInt();
			int temp3=sc.nextInt();	
			Line e =new Line(temp2, temp+totalPoint, temp3,0,temp3);
			line.add(e);		
		}

	}

	public static int Calculate(int result,List<Integer> sl){
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
	public static String[] getOut(List<List<Integer>> route ,List<Integer> sl){
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
			if (sl.get(i).equals(test)){
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
/* 
 * 	//3
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
 * 
 * */
