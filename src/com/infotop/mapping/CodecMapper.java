package com.infotop.mapping;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.infotop.entity.Media;
/**
 * 
 * @author Administrator
 *
 */
public interface CodecMapper  {
	public List<Media> findMedia(Media media);
	public List<Media> findMediaTemp(int reminder);
	public List findMediaCodec(Map params);
	public Integer findMediaCodecCount(Map params);
	public Media findOneTemp(int id);
	public boolean updateMediaTemp(@Param("remainder")Integer remainder,@Param("sort")String sort, @Param("virtualName")String virtualName);
	public Media findTempCodec(String ip);
	public Integer findPlayListCount(Map params);
	public List findPlayList(Map params);
	public List findErrList(Map params);
	public Integer findErrListCount(Map params);
}
