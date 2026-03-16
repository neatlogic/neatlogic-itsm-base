/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
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
    private JSONArray focusUserUuidList;

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

    public JSONArray getFocusUserUuidList() {
        return focusUserUuidList;
    }

    public void setFocusUserUuidList(JSONArray focusUserUuidList) {
        this.focusUserUuidList = focusUserUuidList;
    }
}
