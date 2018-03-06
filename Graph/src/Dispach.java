import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import javax.xml.crypto.Data;


class Node{
	public Node(String name){
		this.name = name;
	}
	String name;
	ArrayList<Integer> relatedNodes = new ArrayList<Integer>();
	int position = 0;
	
	public int next(){
		if(position == relatedNodes.size()){
			return -1;
		}else{
			position ++;
			return relatedNodes.get(position-1);
		}
	}
}


public class Dispach {
	
	
	int MAX = 63325;
	LinkedList<int[]> Graph = new LinkedList<int[]>();
	LinkedList<Node> Nodes = new LinkedList<Node>();
	LinkedList<String> NodesName = new LinkedList<String>();
	LinkedList<String[]> Datas = new LinkedList<String[]>();
	
	
	/*
	 * 初始化图，基于价格
	 * */
	public void initGraphByPrice(){
		//初始化图的每一个数据，为MAX， 代表这不可达
		for(int i=0;i<NodesName.size();i++){
			int row[] = new int[NodesName.size()];
			for(int j=0;j<NodesName.size();j++){
				row[j] = MAX;
			}
			Graph.add(row);
		}
		
		//根据Datas里的数据把图初始化为dat文件的数据
		for(int i=0;i<Datas.size();i++){
			int price = Integer.parseInt(Datas.get(i)[3]);
			/*for(int j=0;j<NodesName.size();j++){
				if(NodesName.get(j).name.compareTo(Datas.get(i)[0]) == 0){
					row = j;
				}
				if(NodesName.get(j).name.compareTo(Datas.get(i)[1]) == 0){
					col = j;
				}
			}*/
			
			int row = NodesName.indexOf(Datas.get(i)[0]);
			int col = NodesName.indexOf(Datas.get(i)[1]);
			Nodes.get(row).relatedNodes.add(col);
			Nodes.get(col).relatedNodes.add(row);
			Graph.get(row)[col] = price;
			Graph.get(col)[row] = price;
		}
		
	}
	public void print(){
		
		System.out.print("  ");
		for(int i=0;i<Graph.size();i++){
			System.out.print(NodesName.get(i) + "   ");
		}
		System.out.println();
		for(int i=0;i<Graph.size();i++){
			System.out.print(NodesName.get(i)+ " ");
			for(int j=0;j<Graph.size();j++){
				if(Graph.get(i)[j] == MAX){
					System.out.print("*" + " , ");
				}else 
					System.out.print(Graph.get(i)[j] + " , ");
			}
			System.out.println();
		}
		
		/*System.out.println(Nodes.get(0).name + Nodes.get(0).next());
		System.out.println(Nodes.get(0).name + Nodes.get(0).next());
		System.out.println(Nodes.get(0).name + Nodes.get(0).next());
		System.out.println(Nodes.get(0).name + Nodes.get(0).next());*/
		
	}
	
	//初始化边的数据，就是每个边的名称（ABCD这样的）
	public void initEdges(){
		
		/*for(int i=0;i<Datas.size();i++){
			for(int j=0;j<Nodes.size();j++){
				if(Nodes.get(j).name.compareTo(Datas.get(i)[0]) == 0){
					this.Nodes.add(new Node(Datas.get(i)[0]));
				}
				if(Nodes.get(j).name.compareTo(Datas.get(i)[1]) == 0){
					this.Nodes.add(new Node(Datas.get(i)[1]));
				}
			}
		
		}*/
		
		for(int i=0;i<Datas.size();i++){
			if(this.NodesName.indexOf(Datas.get(i)[0]) == -1){
				this.NodesName.add(Datas.get(i)[0]);
				this.Nodes.add(new Node(Datas.get(i)[0]));
			}
			if(this.NodesName.indexOf(Datas.get(i)[1]) == -1){
				this.NodesName.add(Datas.get(i)[1]);
				this.Nodes.add(new Node(Datas.get(i)[1]));
			}
		}	
	}
	
	//读取dat文件的数据并存入Datas链表
	public void getData() throws IOException{
		BufferedReader bf = new BufferedReader(new FileReader(new File("./src/data.dat")));
		String line = bf.readLine();
		
		while (line != null) {
			String lines[] = line.split("\\s+");
			Datas.add(lines);
			line = bf.readLine();
		}
	}
	
	public void findByDijkstra(String begin, String end){
		
		int startIndex = NodesName.indexOf(begin);
		
		int Set[] = new int[Nodes.size()];
		int Distance[] = new int[Nodes.size()];
		int Path[] = new int[Nodes.size()];
		
		for(int i=0;i<Nodes.size();i++){
			Set[i] = 0;
			Distance[i] = Graph.get(startIndex)[i];
			if(Graph.get(startIndex)[i] == MAX){
				Path[i] = -1;
			}else{
				Path[i] = startIndex;
			}
		}	
		Set[startIndex] = 1;
		
		for(int i=0;i<Nodes.size()-1;i++){
			int MIN = MAX;
			int MINIndex = 0;
			for(int j=0;j<Nodes.size();j++){
				if(Set[j] == 0 && Distance[j] < MIN){
					MINIndex = j;
					MIN = Distance[j];
				}
			}
			
			Set[MINIndex] = 1;
			for(int j=0;j<Nodes.size();j++){
				if(Set[j] == 0 && Distance[MINIndex] + Graph.get(MINIndex)[j] < Distance[j]){
					Distance[j] = Distance[MINIndex] + Graph.get(MINIndex)[j];
					Path[j] = MINIndex;
				}
			}
		}
		
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		int endIndex = NodesName.indexOf(end);
		
		int index = endIndex;
		path.add(index);
		while (true) {
			int value = Path[index];
			index = value;
			path.add(index);
			if(value == startIndex){
				break;
			}
		}
		
		ArrayList<String> finalPath = new ArrayList<String>();
		for(Integer i : path){
			finalPath.add(NodesName.get(i));
		}
		
		for(int i=finalPath.size()-1;i>0;i--){
			System.out.print(finalPath.get(i)+" --> ");
		}
		System.out.println(finalPath.get(0));
		System.out.println("最小费用为 ： "+ Distance[endIndex]);
		/*for(int i=0;i<Nodes.size();i++){
			System.out.print(Distance[i]+ "");
		}*/
		
		
		
		
		
	}
	
	public void findAllPaths(String start,String end){
		Stack<Node> stack = new Stack<Node>();
		int index = NodesName.indexOf(start);
		stack.push(Nodes.get(index)); //将第一个开始节点的Node加入stack， 
		//如果stack不是空，那么就一直执行
		while (!stack.isEmpty()) {
			Node top = stack.peek();//获取stack第一个node
			int nextIndex = top.next();//获取node的下一个邻接的node
			if(nextIndex == -1){//如果不存在下一个相邻的node，那么stack弹出栈顶元素
				stack.pop();
				top.position = 0; //重置该node的下一个相邻的node位置
				continue;
			}
			Node next = Nodes.get(nextIndex);//根据nextIndex获取下一个节点的Node实体
			if(next.name.compareTo(end) == 0){//如果该节点是最终节点，那么打印
				for(int i=0;i<stack.size();i++){
					System.out.print(stack.get(i).name + " --> ");
				}
				System.out.println(end);
				continue;
			}else if(stack.contains(next)){//如果下一个节点已经在stack了，那么继续执行
				continue;
			}else{//否则的话，压入站
				//System.out.println("---" + nextIndex);
				stack.push(next);
			}
			

		}
	}
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sr = new Scanner(System.in);
		Dispach dispach = new Dispach();
		dispach.getData();
		dispach.initEdges();
		dispach.initGraphByPrice();
		dispach.print();
		
		System.out.println("请输入开始站点，终点， 退出输入q ：");
		System.out.println("当前可供选择的站点有 ： ");
		for(int i=0;i<dispach.NodesName.size();i++){
			System.out.print(dispach.NodesName.get(i)+"  ");
		}
		System.out.println();
		String start = " ";
		while (true) {
			System.out.print("Enter start station or q for quit ： ");
			start = sr.nextLine();
			if(start.compareTo("q") == 0){
				break;
			}
			System.out.print("Enter final station ： ");
			String end = sr.nextLine();
			
			if(!dispach.NodesName.contains(start) || !dispach.NodesName.contains(end)){
				System.out.println("error station , try to enter again ");
				continue;
			}
			
			System.out.println("当前站点到目的站点有一下路径可供选择 ： ");
			dispach.findAllPaths(start, end);
			System.out.println("当前站点到目的站点在费用最低时，有以下路径 : ");
			dispach.findByDijkstra(start, end);
			System.out.println("--------------------------------");
		}

		System.out.println("BYE.....");
	}

}
