package com.infotop.mapping;

import java.util.List;

import com.infotop.entity.Media;
/**
 * 
 * @author Administrator
 *
 */
public interface MediaMapper {
	public Media findMediaById(Media media);
	public List<Media> findMedia(Media media);
	public boolean addMedia(Media media);
	public boolean addMediaErr(Media media);
	public boolean updateMediaById(Media media);
	public boolean updateMediaTempById(Media media);
	public boolean deleteMediaById(Media media);
	public boolean updateMediaStatus(Media media);
	public boolean deleteMediaTemp(long i);
	public void addMediaTemp(Media media);
	public boolean deleteMediaById(long id);
	public boolean deleteMediaTempById(long id);
	public boolean updateMediaWeightById(long id);
}
