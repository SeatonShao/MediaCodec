package com.infotop.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infotop.constants.Constants;


/**
 * hdfs公共服务类
 * @author sxd
 *
 */
public class HDFSService {
	private static Logger log = LogManager.getLogger(HDFSService.class.getName());
	
	public final static Configuration conf ;
	static {
		conf = new Configuration();
		/*解决找到不到对应的map和reduce类*/
		//conf.set("mapreduce.job.jar", "Media.jar");
		/*namenode的HA开始,也可引入core-site.xml、hdfs-site.xml配置文件*/
		conf.set("fs.defaultFS", "hdfs://ns1");
		conf.set("dfs.nameservices", "ns1");
		conf.set("dfs.ha.namenodes.ns1", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.ns1.nn1", Constants.service.getProperty("namenode1"));//192.168.10.231
		conf.set("dfs.namenode.rpc-address.ns1.nn2", Constants.service.getProperty("namenode2"));
		conf.set("dfs.client.failover.proxy.provider.ns1",        
			  "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		//conf.set("hadoop.tmp.dir", "/data/hadoop-2.7.3/tmp"); 
		//conf.set("yarn.resourcemanager.address", "192.168.10.231:8032");
		//conf.set("mapreduce.framework.name", "yarn");
		//conf.set("mapreduce.jobhistory.address", "192.168.10.231:10020");
		//conf.set("yarn.resourcemanager.scheduler.address", "192.168.10.231:8030");
		conf.set("mapreduce.app-submission.cross-platform", "true");
		conf.set("mapreduce.application.classpath",
				"$HADOOP_CONF_DIR," + "$HADOOP_COMMON_HOME/share/hadoop/common/*,"
						+ "$HADOOP_COMMON_HOME/share/hadoop/common/lib/*," + "$HADOOP_HDFS_HOME/share/hadoop/hdfs/*,"
						+ "$HADOOP_HDFS_HOME/share/hadoop/hdfs/lib/*," + "$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*,"
						+ "$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*," + "$HADOOP_YARN_HOME/share/hadoop/yarn/*,"
						+ "$HADOOP_YARN_HOME/share/hadoop/yarn/lib/*");
		/* 解决Timed out after 600 secs，默认超时时间10分钟 */
		conf.set("mapred.task.timeout", "0"); // 不检查超时
		/*
		 * 解决 java.lang.OutOfMemoryError: GC overhead limit
		 * exceeded,yarn关掉虚拟内存检测
		 */
		/*
		 * conf.set("yarn.nodemanager.vmem-check-enabled","false");
		 * conf.set("yarn.nodemanager.pmem-check-enabled","false");
		 */
		conf.set("mapred.child.java.opts", "-Xmx2560m");// 保证JVM*并发数为3<datanaode节点内存(8G)
		conf.set("mapreduce.map.memory.mb", "2560");// map任务的最大内存
		conf.set("mapreduce.reduce.memory.mb", "2560");// reduce任务的最大内存
		conf.set("yarn.nodemanager.vmem-pmem-ratio", "4.25");
		// 关闭speculative task
		/*
		 * conf.setBoolean("mapred.map.tasks.speculative.execution", false);
		 * conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);
		 */
		conf.set("mapred.tasktracker.reduce.tasks.maximum", "3");// 每一个tasktracker同时运行的reduce任务数,默认为2,因为reduce进程数为30,数据节点10台,设置为3可保证reduce
		//设置standby可读目前仅供测试
		//conf.setBoolean("dfs.ha.allow.stale.reads", true);
	}
	

}
