/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trustedanalytics.platformoperations.data;

import org.trustedanalytics.cloud.cc.api.CcBuildpack;
import org.trustedanalytics.cloud.cc.api.CcOrgSummary;

import java.util.HashSet;
import java.util.List;

import lombok.Data;
import org.trustedanalytics.cloud.cc.api.CcOrgSummarySpace;

@Data
public class ControllerSummary {

    protected List<CcOrgSummary> orgs;
    protected List<OrgUser> orgUsers;
    protected List<CcBuildpack> buildpacks;
    protected long memUsedInMb;
    protected long serviceCount;
    protected long appCount;
    protected long spaceCount;
    protected long orgCount;
    protected long userCount;
    protected long buildpackCount;

    public ControllerSummary(List<CcOrgSummary> orgs, List<OrgUser> orgUsers, List<CcBuildpack> buildpacks) {
        this.orgs = orgs;
        this.orgUsers = orgUsers;
        this.buildpacks = buildpacks;
        aggregateData();
    }

    private void aggregateData() {
        this.memUsedInMb = this.orgs.stream()
            .flatMap(org -> org.getSpaces().stream())
            .mapToInt(CcOrgSummarySpace::getMemDevTotal).sum();

        this.serviceCount = this.orgs.stream()
            .flatMap(org -> org.getSpaces().stream())
            .mapToInt(CcOrgSummarySpace::getServiceCount).sum();

        this.appCount = this.orgs.stream()
            .flatMap(org -> org.getSpaces().stream())
            .mapToInt(CcOrgSummarySpace::getAppCount).sum();

        this.orgCount = this.orgs.size();

        this.spaceCount = this.orgs.stream()
            .flatMap(org -> org.getSpaces().stream())
            .count();

        HashSet<String> uniqueUsers = new HashSet<>();
        this.orgUsers.stream().forEach(user -> uniqueUsers.add(user.getUsername()));
        this.userCount = uniqueUsers.size();

        this.buildpackCount = this.buildpacks.size();
    }
}