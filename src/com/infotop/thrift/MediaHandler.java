/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.infotop.thrift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;

import com.infotop.constants.Constants;
import com.infotop.dao.DaoFactory;
import com.infotop.dao.MediaDao;
import com.infotop.thrift.resources.common.*;
import com.infotop.thrift.resources.common.Media;

public class MediaHandler implements MediaService.Iface {
	private static Logger log = LogManager.getLogger(MediaHandler.class.getName());
 
	@Override
	public String codec(Media media) throws TException {
		com.infotop.entity.Media media_info = new com.infotop.entity.Media();
		
		switch(media.getSource()){
			case "ymxt":{media_info.setTitle(Constants.findRemote(media.getSource()).getUrl());}
			default:{media_info.setTitle(Constants.findRemote("ymxt").getUrl());}
		}
		media_info.setStatus(media.getStatus());
		media_info.setName(media.getName());
		media_info.setMediaName(media.getMd5());
		media_info.setTargetUuid(media.getTarget_uuid());
		media_info.setMediaPath("");
		media_info.setMediaLength(Integer.parseInt(media.getMedia_length()==null?"0":media.getMedia_length()));
		media_info.setContentType(media.getContent_type());
		media_info.setPicPath("");
		media_info.setTitle("");
		media_info.setCreateUser(media.getCreate_user());
		// 写入到hdfs
		media_info.setBiteMax("");
		media_info.setBiteMid("");
		media_info.setBiteMin("");
		media_info.setBiteSup("");
		MediaDao mediaDao = DaoFactory.getMediaDao();
		mediaDao.save(media_info);
		if("1".equals(media.getStatus()))
		mediaDao.saveTmp(media_info);
		return "success";
	}

}
