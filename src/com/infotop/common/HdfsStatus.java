package com.infotop.common;

import java.io.IOException;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infotop.service.HDFSService;

public class HdfsStatus {
	
	private static Logger log = LogManager.getLogger(HdfsStatus.class.getName());
	
	/**
	 * get the information of datanode
	 * 
	 * @return
	 * @throws AccessControlException
	 */
	public static String[] getDataNodeReport() throws AccessControlException {
		try {
			FileSystem fs = FileSystem.get(HDFSService.conf);
			DistributedFileSystem hdfs = (DistributedFileSystem) fs;
			DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();

			String[] names = new String[dataNodeStats.length];
			for (int i = 0; i < dataNodeStats.length; i++) {
				names[i] = dataNodeStats[i].getHostName();
				log.info(names[i] + "---" + dataNodeStats[i].getDatanodeReport());
			}
			return names;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get the information of datanode
	 * 
	 * @return
	 * @throws AccessControlException
	 */
	public static long getDataNodeCapacity() throws AccessControlException {
		try {
			FileSystem fs = FileSystem.get(HDFSService.conf);
			DistributedFileSystem hdfs = (DistributedFileSystem) fs;
			DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();

			long remaining = 0;
			for (int i = 0; i < dataNodeStats.length; i++) {
				remaining += dataNodeStats[i].getRemaining();
			}
			return remaining;
		} catch (IOException e) {
			log.error(e);
			return 0;
		}
	}
	/**
	 * get the minimum dfs remaining  information of datanode
	 * 
	 * @return
	 * @throws AccessControlException
	 */
	public static long getDFSRemaining() throws AccessControlException {
		try {
			FileSystem fs = FileSystem.get(HDFSService.conf);
			DistributedFileSystem hdfs = (DistributedFileSystem) fs;
			DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();
			long remain = 0;
			for (int i = 0; i < dataNodeStats.length; i++) {
				if(i==1||remain >dataNodeStats[i].getRemaining()){
					remain = dataNodeStats[i].getRemaining();
				}else{
					
				}
			}
			return remain;
		} catch (IOException e) {
			log.error(e);
			return 0;
		}

	}
	/**
	 * get the node config
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNodeConfig() throws Exception {
		try {
			FileSystem fs = FileSystem.get(HDFSService.conf);
			Iterator<Entry<String, String>> entrys = fs.getConf().iterator();

			while (entrys.hasNext()) {
				Entry<String, String> item = entrys.next();
				String key = item.getKey();

				for (int i = 0; i < 60 - (item.getKey().length()); i++) {
					key = key + "-";
				}
				log.info(key + ":\t" + item.getValue());
			}
			return 1;
		} catch (Exception e) {
			log.error(e);
			return -1;
			// TODO: handle exception
		}
	}
	public static void main(String args[]){
		log.info(getDFSRemaining());
	}
}
