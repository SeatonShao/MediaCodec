package com.infotop.constants;

import java.util.List;
import java.util.Map;

import com.infotop.entity.Remote;

/**
 * 
 * @author Administrator
 *
 */
public interface ConstantsMapper {
	
	public MediaConfig findPropertiesByName(String virtualName);

	public MediaConfig findProperties(String ip, int port);

	public boolean updateConfig(MediaConfig mediaConfig);

	public List findConfig(Map params);
	public Integer findConfigCount(Map params);

	public boolean deleteConfig(int id);

	public MediaConfig find(MediaConfig mediaConfig);

	public Remote findRemote(String source);
}
