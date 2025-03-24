/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ProcessTaskCreateVo {
    private String channel;
    private String title;
    private String owner;
    private String reporter;
    private String priority;
    private JSONArray formAttributeDataList;
    private JSONArray hidecomponentList;
    private JSONArray readcomponentList;
    private String content;
    private String filePathPrefix;
    private JSONArray filePathList;
    private JSONArray fileIdList;
    private JSONObject handlerStepInfo;
    private String source;
    private String region;
    private Long newProcessTaskId;
    private JSONArray assignWorkerList;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public JSONArray getFormAttributeDataList() {
        return formAttributeDataList;
    }

    public void setFormAttributeDataList(JSONArray formAttributeDataList) {
        this.formAttributeDataList = formAttributeDataList;
    }

    public JSONArray getHidecomponentList() {
        return hidecomponentList;
    }

    public void setHidecomponentList(JSONArray hidecomponentList) {
        this.hidecomponentList = hidecomponentList;
    }

    public JSONArray getReadcomponentList() {
        return readcomponentList;
    }

    public void setReadcomponentList(JSONArray readcomponentList) {
        this.readcomponentList = readcomponentList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePathPrefix() {
        return filePathPrefix;
    }

    public void setFilePathPrefix(String filePathPrefix) {
        this.filePathPrefix = filePathPrefix;
    }

    public JSONArray getFilePathList() {
        return filePathList;
    }

    public void setFilePathList(JSONArray filePathList) {
        this.filePathList = filePathList;
    }

    public JSONArray getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(JSONArray fileIdList) {
        this.fileIdList = fileIdList;
    }

    public JSONObject getHandlerStepInfo() {
        return handlerStepInfo;
    }

    public void setHandlerStepInfo(JSONObject handlerStepInfo) {
        this.handlerStepInfo = handlerStepInfo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getNewProcessTaskId() {
        return newProcessTaskId;
    }

    public void setNewProcessTaskId(Long newProcessTaskId) {
        this.newProcessTaskId = newProcessTaskId;
    }

    public JSONArray getAssignWorkerList() {
        return assignWorkerList;
    }

    public void setAssignWorkerList(JSONArray assignWorkerList) {
        this.assignWorkerList = assignWorkerList;
    }
}
