package com.infotop.dao;


import java.util.List;

import com.infotop.entity.Media;

public interface MediaDao  {
    
    /**
     * 视频转码
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @param mediaPicPath    截图保存路径
     * @return
     * @throws Exception
     */
    public boolean executeCodecs(String ffmpegPath,String upFilePath, String codcFilePath, String mediaPicPath)throws Exception;
    /**
     * 检测目录是否存在
     * 不存在就新建目录
     * @param path
     */
    	public void checkPath(String path);
    /**
     * 保存文件
     * @param media
     * @return
     * @throws Exception
     */
    public boolean save(Media media);
    public void sqlClose();
	public Media find(Media media);
	public List<Media> findList(Media media);
	public boolean updateStatus(Media media);
	public boolean updateMedia(Media media);
	public boolean updateMediaTemp(Media media);
	public boolean deleteMediaTemp(int id);
	public void saveErr(Media media);
	public void saveTmp(Media media);
}