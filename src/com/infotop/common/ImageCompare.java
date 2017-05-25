package com.infotop.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * 选取中间一列像素点保存到数组
 * @author xiao
 *
 */
public class ImageCompare {
	/**
	* 根据BufferedImage获取图片RGB数组
	* @param bfImage
	* @return
	*/
	public static int[] getImageGRB(BufferedImage bfImage) {
	  int width =  bfImage.getWidth()/2;
	  int height = bfImage.getHeight();
	  int[] result = new int[height];
	    for (int h = 0; h < height; h++) {
	      //使用getRGB(w, h)获取该点的颜色值是ARGB，而在实际应用中使用的是RGB，所以需要将ARGB转化成RGB，即bufImg.getRGB(w, h) & 0xFFFFFF。
	      result[h] = bfImage.getRGB(width, h) & 0xFFFFFF;
	    }
	  return result;
	}
	
	public static void main(String args[]){
		BufferedImage img = null;
		File file = new File("d:/cat.jpg");// 读入文件
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		int[] rgb = getImageGRB(img);
		for(int i = 0;i<rgb.length;i++)
		System.out.println(rgb[i]);
	}
}
