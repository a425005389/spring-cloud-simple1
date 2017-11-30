package test;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaName;
import javax.print.attribute.standard.MediaSizeName;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


public class Print {
//			File file = new File("D:\\LOL\\test.pdf");
//	File file = new File("D:\\zxing.png");
	public static final float DEFAULT_DPI=105;
	public final  static String  IMG_TYPE_JPG = "jpg";
	public final  static String  IMG_TYPE_PNG = "png";
	
	public static void main(String[] args)
    {
//		pdfToImage("D:\\LOL\\aliJava.pdf");
//		Print pdf2Image = new Print();
//		pdf2Image.pdf2img("D:\\LOL\\aliJava.pdf", "D:",IMG_TYPE_PNG);
		
		
		
		// 构造一个文件选择器，默认为当前目录


	         File file = new File("D:\\aliJava-13.png");// 获取选择的文件

	         // 构建打印请求属性集

	        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

	         // 设置打印格式，因为未确定文件类型，这里选择 AUTOSENSE

	        DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;

	         // 查找所有的可用打印服务

	        PrintService printService[] =

	            PrintServiceLookup.lookupPrintServices(flavor, pras);

	         // 定位默认的打印服务

	        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

	        // 显示打印对话框

	        PrintService service = ServiceUI.printDialog(null, 200, 200, printService

	            , defaultService, flavor, pras);

	        if (service != null)

	        {

	             try

	            {

	                 DocPrintJob job = service.createPrintJob();// 创建打印作业

	                FileInputStream fis = new FileInputStream(file);// 构造待打印的文件流

	                DocAttributeSet das = new HashDocAttributeSet();
	                das.add(MediaSizeName.ISO_A4);

	                Doc doc = new SimpleDoc(fis, flavor, das);// 建立打印文件格式

	                job.print(doc, pras);// 进行文件的打印

	            }

	            catch(Exception e)

	            {

	                 e.printStackTrace();

	            }

	        }

	    }
	
	public void pdf2img(String pdfPath,String savePath,String imgType){
         String fileName = pdfPath.substring(pdfPath.lastIndexOf("\\")+1, pdfPath.length());
         fileName = fileName.substring(0,fileName.lastIndexOf("."));
         InputStream is = null;
         PDDocument pdDocument = null;
         try {
             is = new BufferedInputStream(new FileInputStream(pdfPath));
             //创建pdf文件解析器
             PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
             parser.parse();
             //获取解析后的pdf文档
             pdDocument = parser.getPDDocument();
             //获取pdf渲染器，主要用来后面获取BufferedImage
             PDFRenderer renderer = new PDFRenderer(pdDocument);
             //获取pdf文件总页数
             int pageCount = pdDocument.getNumberOfPages();
             for (int i = 0; i < pageCount; i++) {
                 //构造保存文件名称格式
                 String saveFileName = savePath+"\\"+fileName+"-"+i+"."+imgType;
                 //获取当前页对象
                 PDPage page =  pdDocument.getPage(i);
                 //图片转换
                 pdfPage2Img(page,saveFileName,imgType,renderer,i);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }finally{
             if(pdDocument != null){
                 try {
                     pdDocument.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }
 
     /**
      * 将pdf单页转换为图片
      * @param page 当页对象
      * @param saveFileName 保存的图片名称
      * @param imgType 保存的图片类型
      * @param renderer 用于获取BufferedImage
      * @param index 页索引
      * @throws IOException
      */
     public void pdfPage2Img(PDPage page,String saveFileName,String imgType,PDFRenderer renderer,int index) throws IOException{
         //构造图片
//         BufferedImage img_temp  = renderer.renderImage(index);
    	 //构造图片更清晰
         BufferedImage img_temp = renderer.renderImageWithDPI(index, DEFAULT_DPI,ImageType.RGB);
         //设置图片格式
         Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix(imgType);
         //将文件写出
         ImageWriter writer = (ImageWriter) it.next();
         ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(saveFileName));
         writer.setOutput(imageout);
         writer.write(new IIOImage(img_temp, null, null));
     }
	
	public static void pdfToImage(String pdfPath){
        try{
            if(pdfPath==null||"".equals(pdfPath)||!pdfPath.endsWith(".pdf"))
                return;
                //图像合并使用参数
                int width = 0; // 总宽度
                int[] singleImgRGB; // 保存一张图片中的RGB数据
                int shiftHeight = 0;
                BufferedImage imageResult = null;//保存每张图片的像素值
                //利用PdfBox生成图像
                PDDocument pdDocument = PDDocument.load(new File(pdfPath));
                PDFRenderer renderer = new PDFRenderer(pdDocument);
               //循环每个页码
               for(int i=0,len=pdDocument.getNumberOfPages(); i<len; i++){
                 BufferedImage image=renderer.renderImageWithDPI(i, DEFAULT_DPI,ImageType.RGB);
                 int imageHeight=image.getHeight();
                 int imageWidth=image.getWidth();
                 if(i==0){//计算高度和偏移量
                     width=imageWidth;//使用第一张图片宽度; 
                     //保存每页图片的像素值
                     imageResult= new BufferedImage(width, imageHeight*len, BufferedImage.TYPE_INT_RGB);
                 }else{
                     shiftHeight += imageHeight; // 计算偏移高度
                 }
                 singleImgRGB= image.getRGB(0, 0, width, imageHeight, null, 0, width);
                 imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width); // 写入流中
             }
               pdDocument.close();
                File outFile = new File(pdfPath.replace(".pdf", "_"+DEFAULT_DPI+".jpg"));
                ImageIO.write(imageResult, "jpg", outFile);// 写图片
             }catch (Exception e) {
                 e.printStackTrace();
            }
	}
}
