package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest extends Thread {
	
	private int a;
	
	ThreadTest(int a){
		this.a = a ;
	}
	
	public void maipiao(){
		if(a > 0){
			a--;
			System.out.println(Thread.currentThread().getName()
					+ "正在卖票,还剩" + a + "张票");
		}else{
			System.out.println("没票了");
			return;
		}
	}
	
	@Override
	public void run() {
		while (a > 0){
			maipiao();
		}
	}
	
	public static void main(String[] args)
	     {
//			ThreadTest runTicekt = new ThreadTest();//只定义了一个实例，这就只有一个Object mutex = new Object();即一个锁。
//	         Thread th1 = new Thread(runTicekt, "窗口1");//每个线程等其他线程释放该锁后，才能执行
//	         Thread th2 = new Thread(runTicekt, "窗口2");
//	         Thread th3 = new Thread(runTicekt, "窗口3");
//	         Thread th4 = new Thread(runTicekt, "窗口4");
//	         th1.start();
//	         th2.start();
//	         th3.start();
//	         th4.start();
		ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i=0;i<5;i++) {
            //执行线程
            exec.execute(new ThreadTest(10));
        }
        //线程关闭
        exec.shutdown();
        while(true){  
            if(exec.isTerminated()){  
                System.out.println("所有的子线程都结束了！");  
                break;  
            }     
        }
	     }
}
